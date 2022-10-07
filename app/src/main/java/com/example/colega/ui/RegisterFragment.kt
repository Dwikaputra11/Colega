package com.example.colega.ui

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.colega.data.User
import com.example.colega.databinding.FragmentRegisterBinding
import com.example.colega.viewmodel.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.*


class RegisterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var userVm : UserViewModel
    private val TAG = "RegisterFragment"
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
    }

    private suspend fun signUp() {
        val username = binding.etUsername.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val dateBirth = "2022-10-06"
        val confPassword = binding.etConfirmPassword.text.toString()

        if(password == confPassword){
            if(isInputValid(username)){
                addToDatabase(username, password, email, dateBirth)
                requireActivity().runOnUiThread {
                    toastMessage("Horay! Welcome to the club!")
                }
                dismiss()
            }
        }
    }
    private fun addToDatabase(username: String, password: String, email: String, birthDate: String) {
        val user = User(
            id = 0,
            username = username,
            password = password,
            email = email,
            birthDate = birthDate
        )
        userVm.addUser(user)
        Log.d(TAG, "addToDatabase: Finish")
    }
    private suspend fun isInputValid(
        username: String,
    ): Boolean {
        return if(!username.contains(" ")){
                val isExist = GlobalScope.async { findUsername(username) }.await()
                Log.d(TAG, "isInputValid: $isExist")
                if(!isExist){
                    Log.d(TAG, "isInputValid: Success")
                    true
                }else{
                    toastMessage("Username Already Exist")
                    false
                }
            }else{
                toastMessage("Username should not contain whitespace!")
                false
            }
    }

    private suspend fun findUsername(username:String) : Boolean {
        Log.d(TAG, "findUsername: ")
        Log.d(TAG, "findUsername: Started")
        val waitFor =  CoroutineScope(Dispatchers.IO).async {
            val isExist = userVm.countUser(username) > 0
            isExist
        }.await()
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
            requireContext().theme.obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        return styledAttributes.getDimension(0, 0f).toInt()
    }



}