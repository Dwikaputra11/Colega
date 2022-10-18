package com.example.colega.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.colega.R
import com.example.colega.models.user.DataUser
import com.example.colega.databinding.FragmentRegisterBinding
import com.example.colega.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

private val TAG = "RegisterFragment"

class RegisterFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var userVm : UserViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userVm = ViewModelProvider(this)[UserViewModel::class.java]
        setViews(view)
        binding.btnSignUp.setOnClickListener {
            GlobalScope.async { signUp() }
        }

        binding.etBirthDate.setOnClickListener {
            Log.d(TAG, "onViewCreated: clicked")
            binding.etBirthDate.transformIntoDatePicker(requireContext(), "MM/dd/yyyy")
        }
    }

    private suspend fun signUp() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val dateBirth = binding.etBirthDate.text.toString()
        val confPassword = binding.etConfirmPassword.text.toString()

        if(password == confPassword){
            if(isInputValid(username)){
                addToApi(username, password, email, dateBirth)
                requireActivity().runOnUiThread {
                    toastMessage(getString(R.string.register_success))
                }
                dismiss()
            }
        }
    }
    private fun addToApi(username: String, password: String, email: String, birthDate: String) {
        val user = DataUser(
            username = username,
            password = password,
            email = email,
            birthDate = birthDate,
            fullName = "dwika putra"
        )
        userVm.postUser(user)
        Log.d(TAG, "addToApi: Finish")
    }
    private suspend fun isInputValid(
        username: String
    ): Boolean {
        userVm.getUserResponse(username)
        return if(!username.contains(" ")){
                val isExist = GlobalScope.async { isUserExist(username) }.await()
                Log.d(TAG, "isInputValid: $isExist")
                if(!isExist){
                    Log.d(TAG, "isInputValid: Success")
                    true
                }else{
                    toastMessage(getString(R.string.username_already_exist))
                    false
                }
            }else{
                toastMessage(getString(R.string.username_field))
                false
            }
    }

//    private suspend fun findUsername(username:String) : Boolean {
//        Log.d(TAG, "findUsername: ")
//        Log.d(TAG, "findUsername: Started")
//        val waitFor =  CoroutineScope(Dispatchers.IO).async {
//            val isExist = userVm.isUserExist(username) > 0
//            isExist
//        }.await()
//        Log.d(TAG, "findUsername: outer $waitFor")
//        return waitFor
//    }

    private suspend fun isUserExist(username: String):Boolean{
        val waitFor =  withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val isExist = userVm.getUser().value != null
            isExist
        }
        Log.d(TAG, "findUsername: outer $waitFor")
        return waitFor
    }

    private fun toastMessage(msg: String){
        activity?.runOnUiThread {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
    }

    private fun setViews(view: View) {
        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)
        bottomSheetBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        showView(binding.appBarLayout,getActionBarSize())

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == newState) {
                    Log.d(TAG, "onStateChanged: Expanded")
                    showView(binding.appBarLayout, getActionBarSize());
                    hideView(binding.linearLayoutRegist);
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                    Log.d(TAG, "onStateChanged: Collapsed")
                    hideView(binding.appBarLayout);
                    showView(binding.linearLayoutRegist, getActionBarSize());
                }

                if (BottomSheetBehavior.STATE_HIDDEN == newState) {
                    Log.d(TAG, "onStateChanged: Hidden")
                    dismiss();
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                binding.clLayout.animate()
//                    .y((if (slideOffset <= 0) view.y + bottomSheetBehavior.peekHeight - binding.clLayout.height else view.height - binding.clLayout.height) as Float)
//                    .setDuration(0).start()
            }
        })

        // close the bottom sheet
        binding.tvClose.setOnClickListener {
            dismiss()
        }
    }

    private fun hideView(view: View) {
        val params = view.layoutParams
        params.height = 0
        view.layoutParams = params
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private fun getActionBarSize(): Int {
        val styledAttributes =
            requireContext().theme.obtainStyledAttributes(intArrayOf(com.google.android.material.R.attr.actionBarSize))
        return styledAttributes.getDimension(0, 0f).toInt()
    }

    private fun EditText.transformIntoDatePicker(context: Context, format: String, maxDate: Date? = null) {
        isFocusableInTouchMode = false
        isClickable = true
        isFocusable = false

        val myCalendar = Calendar.getInstance()
        val datePickerOnDataSetListener =
            OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                myCalendar.set(Calendar.YEAR, year)
                myCalendar.set(Calendar.MONTH, monthOfYear)
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(format, Locale.ENGLISH)
                setText(sdf.format(myCalendar.time))
            }

        setOnClickListener {
            DatePickerDialog(
                context, datePickerOnDataSetListener, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).run {
                maxDate?.time?.also { datePicker.maxDate = it }
                show()
            }
        }
    }

}