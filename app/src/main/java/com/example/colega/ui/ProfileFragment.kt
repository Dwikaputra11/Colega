package com.example.colega.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.colega.MainActivity
import com.example.colega.R
import com.example.colega.databinding.FragmentProfileBinding
import com.example.colega.databinding.UploadImageProfileProgressBinding
import com.example.colega.models.user.DataUser
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.UserViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import java.util.*


private const val REQUEST_IMAGE_CODE_PERMISSION = 100
private const val TAG = "ProfileFragment"

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var userId: String
    private lateinit var username:String
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var imgUri: Uri
    private lateinit var downloadUri: Uri

    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleCameraImage(result.data)
            }
        }

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if(result != null) imgUri = result
            Log.d(TAG, "Gallery result: $imgUri")
            saveToFirebase()
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(Utils.name, Context.MODE_PRIVATE)
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference
        setViews()
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val edit = sharedPref.edit()
                if(pos == 1){
                    edit.putString(Utils.languageApp, "id")
                    setLocale("id")
                }else{
                    edit.putString(Utils.languageApp, "en")
                    setLocale("en")
                }
                edit.apply()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.btnAddProfile.setOnClickListener {
            checkPermission()
        }
    }

    private fun setViews() {
        userVM.dataUser.observe(viewLifecycleOwner){
            if(it != null){
                username = it.username
                binding.tvName.text = it.username
                binding.tvEmail.text = it.email
                binding.tvBirth.text = it.birthDate
                Log.d(TAG, "setViews Avatar: ${it.avatar}")
                Glide.with(binding.root)
                    .load(it.avatar)
                    .placeholder(R.drawable.ic_baseline_account_circle_100)
                    .into(binding.civProfile)
            }
        }

        val language = sharedPref.getString(Utils.languageApp, null)
        if(language != null){
            if (language == "id"){
                binding.spinner.setSelection(1)
            }
        }

    }

    private fun setLocale(lang: String?) {
        val myLocale = lang?.let { Locale(it) }
        val res = resources
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, res.displayMetrics)
    }

    private fun checkPermission(){
        if (isGranted(
                requireActivity(),
                Manifest.permission.CAMERA,
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                REQUEST_IMAGE_CODE_PERMISSION,
            )
        ) {
            requestImage()
        }
    }

    private fun isGranted(
        activity: Activity,
        permission: String,
        permissions: Array<String>,
        request: Int,
    ): Boolean {
        val permissionCheck = ActivityCompat.checkSelfPermission(activity, permission)
        return if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showPermissionDeniedDialog()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Denied")
            .setMessage("Permission is denied, Please allow permissions from App Settings.")
            .setPositiveButton(
                "App Settings"
            ) { _, _ ->
                val intent = Intent()
                intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
            .show()
    }

    private fun requestImage(){
        AlertDialog.Builder(requireContext())
            .setMessage("Pilih Gambar")
            .setPositiveButton("Gallery") { _, _ -> openGallery()  }
            .setNegativeButton("Camera") { _, _ -> openCamera() }
            .show()
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraResult.launch(cameraIntent)
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        imgUri = getImageUri(requireContext(), bitmap)
        saveToFirebase()
        Log.d(TAG, "handleCameraImage: $imgUri")
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun saveToFirebase(){
        val dialogBinding = UploadImageProfileProgressBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(false) // if you want user to wait for some process to finish,
        builder.setView(dialogBinding.root)
        val dialog = builder.create()
        dialog.show()
        try {
            val reference = storageReference.child("image/" + username + "_" + UUID.randomUUID().toString())
            reference.putFile(imgUri).addOnSuccessListener {
                it.storage.downloadUrl.addOnCompleteListener { image ->
                    downloadUri = image.result
                    updateUserProfile(downloadUri.toString())
                }
                Toast.makeText(requireActivity(), "Saved Successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }.addOnFailureListener{
                Toast.makeText(requireActivity(), "Error Occurred", Toast.LENGTH_SHORT).show()
            }.addOnProgressListener{
                val progress = (100.0 * it.task.snapshot.bytesTransferred / it.task.snapshot.totalByteCount)
                dialogBinding.tvPercentProgress.text = "Uploading..." + progress + "%"
            }
        }catch (e: java.lang.Exception){
            e.stackTrace
        }
    }

    private fun updateUserProfile(downloadUri: String) {
        userVM.dataUser.observe(this){ it ->
            userId = it.userId.toString()
            val userData = DataUser(
                username = it.username,
                fullName = it.fullName,
                birthDate = it.birthDate,
                email = it.email,
                password = it.password,
                avatar = downloadUri,
            )
            userVM.updateUser(userId, userData)
            userVM.getUserData().observe(this){ user ->
                userVM.addToUserPref(user)
            }
        }
    }


}