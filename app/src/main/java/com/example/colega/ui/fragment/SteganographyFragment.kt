package com.example.colega.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback
import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback
import com.ayush.imagesteganographylibrary.Text.ImageSteganography
import com.ayush.imagesteganographylibrary.Text.TextDecoding
import com.ayush.imagesteganographylibrary.Text.TextEncoding
import com.example.colega.databinding.FragmentSteganographyBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


private const val TAG = "SteganographyFragment"
private const val REQUEST_IMAGE_CODE_PERMISSION = 100
private const val REQUEST_WRITE_EXTERNAL_STORAGE = 1

class SteganographyFragment : Fragment(), TextEncodingCallback, TextDecodingCallback {

    private lateinit var binding  : FragmentSteganographyBinding
    private lateinit var listener: OnSuccessVerify
    private lateinit var imgUri: Uri
    private lateinit var bitmap: Bitmap

    private var status = 0

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            if(result != null) imgUri = result
            binding.ivImage.setImageURI(imgUri)
            bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, imgUri)
            Log.d(TAG, "Gallery result: $imgUri")
        }


    interface OnSuccessVerify{
        fun onSuccess()
    }

    fun setOnSuccessVerify(listener: OnSuccessVerify){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSteganographyBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.ivImage.setOnClickListener {
            checkPermission()
        }
        binding.btnEncode.setOnClickListener {
            status = 1
            bitmap = binding.ivImage.drawable.toBitmap()
            val imageSteganography = ImageSteganography(
                binding.etMsg.text.toString(),
                binding.etKey.text.toString(),
                bitmap
            )
            val textEncoding = TextEncoding(
                requireActivity(),
                this
            )
            textEncoding.execute(imageSteganography)
        }

        binding.btnDecode.setOnClickListener {
            status = 2
            bitmap = binding.ivImage.drawable.toBitmap()
            val imageSteganography = ImageSteganography(
                binding.etKey.text.toString(),
                bitmap
            )
            val textDecoding = TextDecoding(
                requireActivity(),
                this
            )
            textDecoding.execute(imageSteganography)
        }

        binding.btnSave.setOnClickListener {
            checkStoragePermission()
        }
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
            openGallery()
        }
    }

    private fun openGallery() {
        requireActivity().intent.type = "image/*"
        galleryResult.launch("image/*")
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
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
                Toast.makeText(requireContext(), "Permission denied!", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(activity, permissions, request)
            }
            false
        } else {
            true
        }
    }

    override fun onStartTextEncoding() {

    }

    override fun onCompleteTextEncoding(result: ImageSteganography?) {
        if (result != null && result.isEncoded){


            //encrypted image bitmap is extracted from result object
            val encodeImage = result.encoded_image;
            Log.d(TAG, "onCompleteTextEncoding encrypt: $result")
            binding.ivResult.setImageBitmap(encodeImage)
//            listener.onSuccess()
            //set text and image to the UI component.
        }

        if(result != null && result.isDecoded){
            val decodeImage = result.encoded_image
            binding.etMsg.setText(result.message)
            binding.etKey.setText(result.secret_key)
            binding.ivResult.setImageBitmap(decodeImage)
            Log.d(TAG, "onCompleteTextEncoding decode: $result")
        }
    }

    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_WRITE_EXTERNAL_STORAGE
            )
        } else {
            // Permission already granted, proceed with saving image
            saveImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, proceed with saving image
                    saveImage()
                } else {
                    // Permission denied, show a message or handle accordingly
                    Toast.makeText(
                        requireActivity(),
                        "Permission Denied. Cannot save the image.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            // Handle other permissions if needed
        }
    }

    private fun saveImage() {
        val bitmap = getBitmapFromImageView(binding.ivResult)
        if (bitmap != null) {
            saveBitmapToStorage(bitmap)
        } else {
            Toast.makeText(requireActivity(), "Failed to obtain image from ImageView", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBitmapFromImageView(imageView: ImageView): Bitmap? {
        // Get the drawable from the ImageView
        val drawable = imageView.drawable

        // Convert the drawable to a Bitmap
        return if (drawable != null) {
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = android.graphics.Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        } else {
            null
        }
    }

    private fun saveBitmapToStorage(bitmap: Bitmap) {
        val filename = if(status == 1) "encode.jpg" else "decode.jpg"

        // Get the directory for storing images
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        // Create the file in the directory
        val file = File(dir, filename)

        try {
            // Compress the bitmap to JPEG format
            val outputStream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()

            // Notify the MediaScanner about the new file so that it appears in the gallery
            MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                file.absolutePath,
                file.name,
                file.name
            )

            Toast.makeText(requireActivity(), "Image saved successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireActivity(), "Failed to save image", Toast.LENGTH_SHORT).show()
        }
    }
}