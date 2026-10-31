package com.example.reater

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reater.databinding.FragmentAuthIntroBinding

class AuthIntroFragment : Fragment() {
    private var _binding: FragmentAuthIntroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthIntroBinding.inflate(layoutInflater, container, false)

        binding.studentBox.setOnClickListener {
            findNavController().navigate(R.id.action_authIntroFragment_to_authHomeFragment)
        }

        binding.teacherBox.setOnClickListener {
            findNavController().navigate(R.id.action_authIntroFragment_to_teacherSigninFragment)
        }

        binding.adminBox.setOnClickListener {
            val adminUrl = "https://iiituna-backend.onrender.com/admin"
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(adminUrl))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Opening Admin Portal...", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
