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

    private fun encryptRailFence(text: String, rails: Int): String {
        val fence = Array(rails) { StringBuilder() }
        var rail = 0
        var direction = 1

        for (char in text) {
            fence[rail].append(char)
            rail += direction

            if (rail == rails - 1 || rail == 0) {
                direction *= -1
            }
        }

        return fence.joinToString("")
    }

    fun decryptRailFence(ciphertext: String, rails: Int): String {
        val fence = Array(rails) { CharArray(ciphertext.length) }
        var rail = 0
        var direction = 1

        for (i in ciphertext.indices) {
            fence[rail][i] = 'X' // Placeholder character

            rail += direction

            if (rail == rails - 1 || rail == 0) {
                direction *= -1
            }
        }

        var index = 0
        for (i in 0 until rails) {
            for (j in ciphertext.indices) {
                if (fence[i][j] == 'X') {
                    fence[i][j] = ciphertext[index++]
                }
            }
        }

        val result = StringBuilder()
        rail = 0
        direction = 1

        for (i in ciphertext.indices) {
            result.append(fence[rail][i])
            rail += direction

            if (rail == rails - 1 || rail == 0) {
                direction *= -1
            }
        }

        return result.toString()
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
            val msg = encryptRailFence(encrypt("Dwika Developer 2023!", shift), shift)
            binding.etMsg.setText(msg)
            binding.btnDecode.isVisible = false
        }else if(type == 2){
            binding.etMsg.setText("Dwika Developer 2023!")
            binding.tvType.text = "Tipe: Decrypt"
            binding.btnEncode.isVisible = false
        }

        binding.btnEncode.setOnClickListener {
            val result = binding.etResult.text.toString()
            val key = binding.etKey.text.toString().toInt()
            if(encryptRailFence(encrypt(result, key),key) == binding.etMsg.text.toString()){
                listener.onSuccess()
            }
        }

        binding.btnDecode.setOnClickListener {
            val result = binding.etResult.text.toString()
            val key = binding.etKey.text.toString().toInt()
            if(decrypt(decryptRailFence(result, key), key) == binding.etMsg.text.toString()){
                listener.onSuccess()
            }
        }
    }
}