package com.example.reater.teacherapp.fragments.TeacherAssignments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.reater.R
import com.example.reater.adapters.AssignmentAdapter
import com.example.reater.databinding.FragmentTeacherAssignmentBinding
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherAssignmentFragment : Fragment() {

    private var _binding: FragmentTeacherAssignmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { AssignmentAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeacherAssignmentBinding.inflate(layoutInflater, container, false)
        binding.teacherRV.adapter = mAdapter
        binding.teacherRV.layoutManager = LinearLayoutManager(requireContext())
        setupScreen()

        mAdapter.onItemClick = { assignment ->
            val reviewSheet = TeacherAssignmentReviewBottomSheet.newInstance(assignment)
            reviewSheet.show(childFragmentManager, "TeacherAssignmentReviewBottomSheet")
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_teacherAssignmentFragment_to_assignmentUploadFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getStudentAssignments()
    }

    private fun setupScreen() {
        mainViewModel.getStudentAssignments()
        mainViewModel.studentAssignments.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), result.message ?: "Network Error", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    result.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Loading -> {
                    // Show a loading indicator
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}