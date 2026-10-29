package com.example.reater.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.NotesAdapter
import com.example.reater.databinding.FragmentNotesBinding
import com.example.reater.models.Coursework
import com.example.reater.utils.Constants
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val notesAdapter by lazy { NotesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)

        setupRecyclerView()

        binding.btnRegisterNotes.setOnClickListener {
            val bottomSheet = RegisterClassBottomSheet.newInstance {
                fetchNotes()
            }
            bottomSheet.show(childFragmentManager, RegisterClassBottomSheet.TAG)
        }

        observeViewModel()
        fetchNotes()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fetchNotes()
    }

    private fun setupRecyclerView() {
        binding.notesRV.adapter = notesAdapter
        binding.notesRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun fetchNotes() {
        val coursework = arguments?.getParcelable<Coursework>(Constants.COURSEWORK_RESULT_KEY)
        mainViewModel.fetchStudyMaterials(coursework?.SubjectID)
    }

    private fun observeViewModel() {
        mainViewModel.studyMaterialsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    binding.notesProgressBar.visibility = View.VISIBLE
                }

                is NetworkResult.Error -> {
                    binding.notesProgressBar.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        response.message ?: "Failed to fetch notes",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is NetworkResult.Success -> {
                    binding.notesProgressBar.visibility = View.GONE
                    val notes = response.data ?: emptyList()
                    if (notes.isEmpty()) {
                        binding.notesRV.visibility = View.GONE
                        binding.notesEmptyCard.visibility = View.VISIBLE
                    } else {
                        binding.notesRV.visibility = View.VISIBLE
                        binding.notesEmptyCard.visibility = View.GONE
                        notesAdapter.setData(notes)
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