package com.example.reater.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.AssignmentAdapter
import com.example.reater.databinding.FragmentAssignmentBinding
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AssignmentFragment : Fragment() {

    private var _binding: FragmentAssignmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val mAdapter by lazy { AssignmentAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentBinding.inflate(inflater, container, false)
        setupRecyclerView()

        binding.btnRegisterAssignment.setOnClickListener {
            val bottomSheet = RegisterClassBottomSheet.newInstance {
                mainViewModel.getStudentAssignments()
            }
            bottomSheet.show(childFragmentManager, RegisterClassBottomSheet.TAG)
        }

        observeViewModel()
        mainViewModel.getStudentAssignments()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getStudentAssignments()
    }

    private fun setupRecyclerView() {
        binding.assignmentRV.adapter = mAdapter
        binding.assignmentRV.layoutManager = LinearLayoutManager(requireContext())
        mAdapter.onItemClick = { assignment ->
            try {
                val action = AssignmentFragmentDirections.actionAssignmentFragmentToAssignmentDetailsFragment(assignment)
                findNavController().navigate(action)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), assignment.assignmentName ?: "Assignment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        mainViewModel.studentAssignments.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    binding.assignmentProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    binding.assignmentProgressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message ?: "Failed to load assignments",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Success -> {
                    binding.assignmentProgressBar.visibility = View.GONE
                    val assignments = response.data ?: emptyList()
                    if (assignments.isEmpty()) {
                        binding.assignmentRV.visibility = View.GONE
                        binding.assignmentEmptyCard.visibility = View.VISIBLE
                    } else {
                        binding.assignmentRV.visibility = View.VISIBLE
                        binding.assignmentEmptyCard.visibility = View.GONE
                        mAdapter.setData(assignments)
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