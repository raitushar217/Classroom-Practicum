package com.example.reater.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.AvailableClassAdapter
import com.example.reater.databinding.DialogRegisterClassesBinding
import com.example.reater.models.RegisterClassRequest
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RegisterClassBottomSheet(
    private val onRegistrationSuccess: (() -> Unit)? = null
) : BottomSheetDialogFragment() {

    private var _binding: DialogRegisterClassesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var adapter: AvailableClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRegisterClassesBinding.inflate(inflater, container, false)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        adapter = AvailableClassAdapter { availableClass ->
            mainViewModel.registerForClass(
                RegisterClassRequest(
                    teachingAssignmentId = availableClass.id,
                    subjectCode = availableClass.subjectCode
                )
            )
        }

        binding.availableClassesRV.layoutManager = LinearLayoutManager(requireContext())
        binding.availableClassesRV.adapter = adapter

        observeViewModel()
        mainViewModel.fetchAvailableClasses()
    }

    private fun observeViewModel() {
        mainViewModel.availableClassesResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.loadingBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.loadingBar.visibility = View.GONE
                    result.data?.let {
                        adapter.setItems(it)
                    }
                }
                is NetworkResult.Error -> {
                    binding.loadingBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message ?: "Failed to load classes", Toast.LENGTH_SHORT).show()
                }
            }
        }

        mainViewModel.registerClassResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.loadingBar.visibility = View.VISIBLE
                }
                is NetworkResult.Success -> {
                    binding.loadingBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Successfully registered for class!", Toast.LENGTH_SHORT).show()
                    mainViewModel.fetchAvailableClasses()
                    mainViewModel.fetchStudentCourses()
                    onRegistrationSuccess?.invoke()
                }
                is NetworkResult.Error -> {
                    binding.loadingBar.visibility = View.GONE
                    Toast.makeText(requireContext(), result.message ?: "Registration error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "RegisterClassBottomSheet"
        fun newInstance(onSuccess: (() -> Unit)? = null): RegisterClassBottomSheet {
            return RegisterClassBottomSheet(onSuccess)
        }
    }
}
