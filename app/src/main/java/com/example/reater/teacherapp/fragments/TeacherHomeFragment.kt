package com.example.reater.teacherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.R
import com.example.reater.adapters.TeacherHomeClassAdapter
import com.example.reater.databinding.FragmentTeacherHomeBinding
import com.example.reater.models.Coursework
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.FirebaseViewmodel


class TeacherHomeFragment : Fragment() {
    private var _binding:FragmentTeacherHomeBinding?=null
    private val binding get() = _binding!!
    private lateinit var firebaseViewmodel: FirebaseViewmodel
    var teacherID="12"
    val mAdapter by lazy {TeacherHomeClassAdapter()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewmodel=ViewModelProvider(requireActivity())[FirebaseViewmodel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding= FragmentTeacherHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Immediately show dummy data while testing if Firebase triggers
        binding.TeacherHomeRV.adapter = mAdapter
        binding.TeacherHomeRV.layoutManager = LinearLayoutManager(requireContext())
        showDummyData()
        
        fetchClases()
    }

    private fun fetchClases() {
        firebaseViewmodel.fetchTeacherClasses(teacherID)
        firebaseViewmodel.classlistResponse.observe(viewLifecycleOwner,{result->
            when(result){
                is NetworkResult.Error->{
                    showDummyData()
                }
                is NetworkResult.Success->{
                    if (result.data.isNullOrEmpty()) {
                        showDummyData()
                    } else {
                        mAdapter.setData(result.data!!)
                    }
                }
                is NetworkResult.Loading -> {
                    // Show a loading indicator
                }
            }
        })
    }

    private fun showDummyData() {
        val dummyData = listOf(
            Coursework(SubjectID = "CS101", SubjectName = "Introduction to Computer Science", ClassID = "B.Tech CSE - A", TeacherID = "12", TeacherName = "Veena Khandelwal", Credits = "4", Semester = "1", AcademicSession = "2023-2024"),
            Coursework(SubjectID = "CS102", SubjectName = "Data Structures", ClassID = "B.Tech CSE - B", TeacherID = "12", TeacherName = "Veena Khandelwal", Credits = "4", Semester = "2", AcademicSession = "2023-2024"),
            Coursework(SubjectID = "CS103", SubjectName = "Algorithms", ClassID = "B.Tech CSE - C", TeacherID = "12", TeacherName = "Veena Khandelwal", Credits = "4", Semester = "3", AcademicSession = "2023-2024")
        )
        mAdapter.setData(dummyData)
    }
}