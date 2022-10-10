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
import com.example.colega.HomeActivity
import com.example.colega.R
import com.example.colega.databinding.FragmentLoginBinding
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private val TAG = "LoginFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.clLayout.rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(Utils.name,Context.MODE_PRIVATE)
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        binding.btnSignIn.setOnClickListener {
            GlobalScope.async {
                loginAccount()
            }
        }
        binding.btnSignUp.setOnClickListener {
            val registerFragment = RegisterFragment()
            registerFragment.show(requireActivity().supportFragmentManager, registerFragment.tag)
        }
    }

    private suspend fun loginAccount() {
        val username = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        if(isExist(username)){
            // if auth success username will be store in shared pref so
            // when user open the app, it will goes to home page immediately

            // cause login() run in different thread so when we go back to the ui thread it cannot work
            // so we have to declare the that the function we want tu run in ui thread like below
            requireActivity().runOnUiThread {
                addToSharedPref(username, password)
            }
        }else{
            toastMessage(getString(R.string.username_status))
        }
    }

    private suspend fun isExist(username: String):Boolean{
        val usernameSharedPref = sharedPref.getString(Utils.username, "")
        return if (usernameSharedPref != null) {
            if (usernameSharedPref.isNotBlank()) {
                if (usernameSharedPref == username) {
                    true
                } else {
                    Log.d(TAG, "isExist: Search to database user pref")
                    findInDatabase(username)
                }
            } else {
                Log.d(TAG, "isExist: Search to database user pref null")
                findInDatabase(username)
            }
        }else false
    }

    private suspend fun findInDatabase(username: String): Boolean {
        val status = CoroutineScope(Dispatchers.IO).async{
            val isExist = userVM.countUser(username) > 0
            Log.d(TAG, "findInDatabase: $isExist")
            isExist
        }.await()
        Log.d(TAG, "findInDatabase: status $status")
        return status
    }
    private fun addToSharedPref(username: String, password: String) {
        userVM.findUser(username).observe(viewLifecycleOwner){ user ->
            val addData = sharedPref.edit()
            Log.d("Register", "Username: ${user.username}")
            Log.d("Register", "Password: ${user.password}")
            Log.d("Register", "Email: ${user.email}")
            Log.d("Register", "User Id: ${user.id}")
            if(password == user.password){
                addData.putString(Utils.email, user.email)
                addData.putString(Utils.username, username)
                addData.putString(Utils.password, user.password)
                addData.putInt(Utils.userId, user.id)
                addData.putString(Utils.dateBirth, user.birthDate)
                // when password is correct go to home page
                startActivity(Intent(requireActivity(), HomeActivity::class.java))
            }else{
                toastMessage(getString(R.string.password_status))
            }
            addData.apply()
        }
    }

    private fun toastMessage(msg:String){
        requireActivity().runOnUiThread {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

}