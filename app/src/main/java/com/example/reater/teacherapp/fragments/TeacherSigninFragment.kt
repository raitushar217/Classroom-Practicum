package com.example.reater.teacherapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.reater.databinding.FragmentTeacherSigninBinding
import com.example.reater.models.LoginRequest
import com.example.reater.teacherapp.TeacherMainActivity
import com.example.reater.utils.NetworkResult
import com.example.reater.utils.TokenManager
import com.example.reater.viewmodels.AuthenticationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherSigninFragment : Fragment() {

    private var _binding: FragmentTeacherSigninBinding? = null
    private val binding get() = _binding!!
    private lateinit var authenticationViewModel: AuthenticationViewModel

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticationViewModel = ViewModelProvider(requireActivity())[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherSigninBinding.inflate(inflater, container, false)
        binding.teacherSigninProgressBar.visibility = View.INVISIBLE

        binding.teacherLoginBox.setOnClickListener {
            val id = binding.teacherEmailEditText.text.toString().trim()
            val password = binding.teacherPasswordEditText.text.toString().trim()
            if (id.isNotEmpty() && password.isNotEmpty()) {
                loginTeacher(LoginRequest(id, password))
            } else {
                Toast.makeText(requireContext(), "Please enter ID and password", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }

    private fun loginTeacher(loginRequest: LoginRequest) {
        authenticationViewModel.getTeacherProfile(loginRequest)

        authenticationViewModel.TeacherProfileResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    binding.teacherSigninProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    binding.teacherSigninProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Success -> {
                    binding.teacherSigninProgressBar.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), "Successfully Logged In", Toast.LENGTH_SHORT).show()
                    response.data?.let {
                        tokenManager.saveToken(it.token)
                        tokenManager.saveUserInfo(it.user.identificationNumber, it.user.name ?: "Teacher")
                    }
                    val intent = Intent(requireContext(), TeacherMainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}