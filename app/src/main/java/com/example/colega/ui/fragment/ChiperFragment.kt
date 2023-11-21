package com.example.colega.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.example.colega.R
import com.example.colega.databinding.FragmentChiperBinding

private const val TAG = "ChiperFragment"
class ChiperFragment : Fragment() {

    private lateinit var binding: FragmentChiperBinding
    private lateinit var listener: OnSuccessVerify

    private fun encrypt(text: String, shift: Int): String {
        val result = StringBuilder()

        for (char in text) {
            if (char.isLetter()) {
                val start = if (char.isUpperCase()) 'A' else 'a'
                result.append((start + (char - start + shift) % 26))
            } else {
                result.append(char)
            }
        }

        return result.toString()
    }

    private fun decrypt(text: String, shift: Int): String {
        return encrypt(text, 26 - shift) // Decryption is just encryption with a backward shift
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
        binding = FragmentChiperBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val shift = (1..26).random()
        val type = (1..2).random()
        binding.etKey.setText(shift.toString())
        if(type == 1){
            binding.tvType.text = "Tipe: Encrypte"
            val msg = encrypt("Dwika Developer 2023!", shift)
            binding.etMsg.setText(msg)
            binding.btnDecode.isVisible = false
        }else if(type == 2){
            binding.etMsg.setText("Dwika Developer 2023!")
            binding.tvType.text = "Tipe: Decrypt"
            binding.btnEncode.isVisible = false
        }

        binding.btnEncode.setOnClickListener {
            if(encrypt(binding.etResult.text.toString(), binding.etKey.text.toString().toInt()) == binding.etMsg.text.toString()){
                listener.onSuccess()
            }
        }

        binding.btnDecode.setOnClickListener {
            if(decrypt(binding.etResult.text.toString(), binding.etKey.text.toString().toInt()) == binding.etMsg.text.toString()){
                listener.onSuccess()
            }
        }
    }
}