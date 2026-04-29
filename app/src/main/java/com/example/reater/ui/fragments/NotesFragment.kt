package com.example.reater.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.R
import com.example.reater.adapters.NotesAdapter
import com.example.reater.databinding.FragmentNotesBinding
import com.example.reater.models.Coursework
import com.example.reater.utils.Constants
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.FirebaseViewmodel


class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseViewmodel: FirebaseViewmodel
    private val notesAdapter by lazy { NotesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewmodel = ViewModelProvider(requireActivity())[FirebaseViewmodel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        
        setupRecyclerView()
        notesAdapter.loadSampleData()

        // Fetch coursework data if passed via bundle, or use test data
        val coursework = arguments?.getParcelable<Coursework>(Constants.COURSEWORK_RESULT_KEY)
        if (coursework != null) {
            requestData(coursework.ClassID, coursework.SubjectID)
        } else {
            // Using test data for now
            requestData("IT-E", "IT2121")
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.notesRV.adapter = notesAdapter
        binding.notesRV.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestData(classID: String, subjectID: String) {
        firebaseViewmodel.getStudyMaterials(classID, subjectID)
        firebaseViewmodel.filesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let {
                        notesAdapter.setData(it)
                    } ?: run {
                        Toast.makeText(requireContext(), "No notes found.", Toast.LENGTH_SHORT).show()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message ?: "Failed to fetch notes", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    // Show a loader ideally
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}