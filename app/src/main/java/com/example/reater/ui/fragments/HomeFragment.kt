package com.example.reater.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.R
import com.example.reater.adapters.ClassAdapter
import com.example.reater.databinding.FragmentHomeBinding
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { ClassAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    private var hasAutoOpenedRegister = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.cardcontainer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment)
        }

        binding.btnRegisterMore.setOnClickListener {
            openRegisterClassDialog()
        }

        binding.btnEmptyStateRegister.setOnClickListener {
            openRegisterClassDialog()
        }

        setupRecyclerView()
        observeViewModel()
        mainViewModel.fetchStudentCourses()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchStudentCourses()
    }

    private fun openRegisterClassDialog() {
        val bottomSheet = RegisterClassBottomSheet.newInstance {
            mainViewModel.fetchStudentCourses()
        }
        bottomSheet.show(childFragmentManager, RegisterClassBottomSheet.TAG)
    }

    private fun setupRecyclerView() {
        binding.classRV.adapter = mAdapter
        binding.classRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        mainViewModel.studentCoursesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    binding.homeProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    binding.homeProgressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message ?: "Failed to load classes",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Success -> {
                    binding.homeProgressBar.visibility = View.GONE
                    val courses = response.data ?: emptyList()
                    if (courses.isEmpty()) {
                        binding.classRV.visibility = View.GONE
                        binding.emptyStateCard.visibility = View.VISIBLE
                        if (!hasAutoOpenedRegister) {
                            hasAutoOpenedRegister = true
                            openRegisterClassDialog()
                        }
                    } else {
                        binding.classRV.visibility = View.VISIBLE
                        binding.emptyStateCard.visibility = View.GONE
                        mAdapter.setData(courses)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}