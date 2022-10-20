package com.example.colega.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.colega.HomeActivity
import com.example.colega.R
import com.example.colega.databinding.FragmentLoginBinding
import com.example.colega.models.user.DataUser
import com.example.colega.models.user.UserResponseItem
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.SourceViewModel
import com.example.colega.viewmodel.UserViewModel
import kotlinx.coroutines.*

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private var usernameSharedPref = ""
    private lateinit var sourceVM: SourceViewModel
    private val TAG = "LoginFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.clLayout.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(requireContext())
            .load(Utils.loginImage)
            .into(binding.ivLogin)
        sharedPref = requireActivity().getSharedPreferences(Utils.name,Context.MODE_PRIVATE)
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        sourceVM = ViewModelProvider(this)[SourceViewModel::class.java]
        binding.btnSignIn.setOnClickListener {
            Log.d(TAG, "onViewCreated: Clicked")
            GlobalScope.async {
                loginAccount()
            }
        }
        userVM.dataUser.observe(viewLifecycleOwner){
            usernameSharedPref = it.username
            Log.d(TAG, "isExist: ${it.username}")
        }
        binding.btnSignUp.setOnClickListener {
            val registerFragment = RegisterFragment()
            registerFragment.show(requireActivity().supportFragmentManager, registerFragment.tag)
        }
    }

    private suspend fun loginAccount() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        withContext(Dispatchers.IO) {
            userVM.getUserResponse(username)
        }
        if(isExist(username)){
            // if auth success username will be store in shared pref so
            // when user open the app, it will goes to home page immediately

            // cause login() run in different thread so when we go back to the ui thread it cannot work
            // so we have to declare the that the function we want tu run in ui thread like below
            requireActivity().runOnUiThread {
                addToSharedPref(password)
            }
            Log.d(TAG, "loginAccount: Exist")
        }else{
            toastMessage(getString(R.string.username_status))
        }
    }

    private suspend fun isExist(username: String):Boolean{
        Log.d(TAG, "isExist: $usernameSharedPref")
        return if (usernameSharedPref.isNotBlank()) {
            if (usernameSharedPref == username) {
                true
            } else {
                Log.d(TAG, "isExist: Search to database user pref")
                findInApi()
            }
        } else {
            Log.d(TAG, "isExist: Search to database user pref null")
            findInApi()
        }
    }

    private suspend fun findInApi(): Boolean {
        val status = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            val isExist = userVM.getUser().value?.username != null
            Log.d(TAG, "findInApi: $isExist")
            isExist
        }
        Log.d(TAG, "findInApi: status $status")
        return status
    }
    private fun addToSharedPref(password: String) {
        Log.d(TAG, "addToSharedPref: Started")
        userVM.getUser().observe(viewLifecycleOwner){
            if(it != null){
                if(password == it.password){
                    Log.d("Register", "Username: ${it.username}")
                    Log.d("Register", "Password: ${it.password}")
                    Log.d("Register", "Email: ${it.email}")
                    Log.d("Register", "User Id: ${it.id}")
                    Log.d(TAG, "addToSharedPref: Password Same")
                    userVM.addToUserPref(it)
                    startActivity(Intent(requireActivity(), HomeActivity::class.java))
                }else{
                    Log.d(TAG, "addToSharedPref: Your Password is wrong")
                    toastMessage(getString(R.string.password_status))
                }
            }
        }
    }

    private fun toastMessage(msg:String){
        requireActivity().runOnUiThread {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

}