package com.example.reater.teacherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.TeacherHomeClassAdapter
import com.example.reater.databinding.FragmentTeacherHomeBinding
import com.example.reater.models.Coursework
import com.example.reater.utils.NetworkResult
import com.example.reater.utils.TokenManager
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherHomeFragment : Fragment() {
    private var _binding: FragmentTeacherHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { TeacherHomeClassAdapter() }

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.TeacherHomeRV.adapter = mAdapter
        binding.TeacherHomeRV.layoutManager = LinearLayoutManager(requireContext())

        val savedName = tokenManager.getUserName()
        if (!savedName.isNullOrBlank()) {
            binding.teacherHomeName.text = savedName
        }

        observeViewModel()
        mainViewModel.fetchTeacherCourses()
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchTeacherCourses()
    }

    private fun observeViewModel() {
        mainViewModel.teacherCoursesResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val courses = result.data
                    if (!courses.isNullOrEmpty()) {
                        mAdapter.setData(courses)
                        val teacherName = courses[0].TeacherName
                        if (!teacherName.isNullOrBlank()) {
                            binding.teacherHomeName.text = teacherName
                            tokenManager.saveUserInfo(courses[0].TeacherID, teacherName)
                        }
                    }
                }
                is NetworkResult.Error -> {
                    // Maintain current display
                }
                is NetworkResult.Loading -> {
                    // Loading indicator
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}