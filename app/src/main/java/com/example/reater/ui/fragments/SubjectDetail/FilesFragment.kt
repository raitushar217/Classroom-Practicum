package com.example.reater.ui.fragments.SubjectDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.FilesAdapter
import com.example.reater.databinding.FragmentFilesBinding
import com.example.reater.models.Coursework
import com.example.reater.utils.Constants.Companion.COURSEWORK_RESULT_KEY
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilesFragment : Fragment() {

    private var _binding: FragmentFilesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val filesAdapter by lazy { FilesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)

        setupRecyclerView()

        val coursework = arguments?.getParcelable<Coursework>(COURSEWORK_RESULT_KEY)
        val subjectCode = coursework?.SubjectID

        observeViewModel()
        mainViewModel.fetchStudyMaterials(subjectCode)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val coursework = arguments?.getParcelable<Coursework>(COURSEWORK_RESULT_KEY)
        mainViewModel.fetchStudyMaterials(coursework?.SubjectID)
    }

    private fun setupRecyclerView() {
        binding.fileRV.adapter = filesAdapter
        binding.fileRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        mainViewModel.studyMaterialsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val materials = response.data ?: emptyList()
                    filesAdapter.setData(materials)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(
                        requireContext(),
                        response.message ?: "Failed to load files",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    // loading state
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}