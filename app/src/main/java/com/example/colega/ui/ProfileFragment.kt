package com.example.colega.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.colega.MainActivity
import com.example.colega.R
import com.example.colega.databinding.FragmentProfileBinding
import com.example.colega.utils.Utils
import com.example.colega.viewmodel.UserViewModel
import java.util.*


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var userVM: UserViewModel
    private val TAG = "ProfileFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedPref = requireActivity().getSharedPreferences(Utils.name, Context.MODE_PRIVATE)
        userVM = ViewModelProvider(this)[UserViewModel::class.java]
        setViews()
        binding.ivLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton(R.string.yes) { _, _ ->
                run {
                    // when "YES" option was clicked shared pref will clear the all data that was store
//                    val exit = sharedPref.edit()
//                    exit.remove(Utils.email)
//                    exit.remove(Utils.username)
//                    exit.remove(Utils.userId)
//                    exit.remove(Utils.dateBirth)
//                    exit.remove(Utils.password)
//                    exit.apply()
                    userVM.clearUserPref()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            }
            builder.setNegativeButton(getString(R.string.no)) { _, _ -> }
            builder.setTitle(getString(R.string.logout_account))
            builder.setMessage(getString(R.string.confirm_logout))
            builder.create().show()
        }
    }

    private fun setViews() {
        userVM.dataUser.observe(viewLifecycleOwner){
            if(it != null){
                binding.tvName.text = it.username
                binding.tvEmail.text = it.email
                binding.tvBirth.text = it.birthDate
            }
        }

        val language = sharedPref.getString(Utils.languageApp, null)
        if(language != null){
            if (language == "id"){
                binding.spinner.setSelection(1)
            }
        }

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
    }

    private fun setLocale(lang: String?) {
        val myLocale = lang?.let { Locale(it) }
        val res = resources
        val conf = res.configuration
        conf.locale = myLocale
        res.updateConfiguration(conf, res.displayMetrics)
    }

}