package com.example.reater.teacherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.reater.R
import com.example.reater.databinding.FragmentAssignmentDetailsBinding
import com.example.reater.databinding.FragmentTeacherClassDetailsBinding


class TeacherClassDetailsFragment : Fragment() {

    val args:TeacherClassDetailsFragmentArgs by navArgs()
    private var _binding: FragmentTeacherClassDetailsBinding?=null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTeacherClassDetailsBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nav = findNavController()

        binding.ClassNameID.text = args.course.SubjectName.ifBlank { "Class Details" }
        binding.btnBack.setOnClickListener {
            nav.popBackStack()
        }

        binding.cardViewA.setOnClickListener {
            val action = TeacherClassDetailsFragmentDirections.actionTeacherClassDetailsFragmentToTeacherQuizFragment(args.course)
            nav.navigate(action)
        }
        binding.cardViewB.setOnClickListener {
            nav.navigate(R.id.action_teacherClassDetailsFragment_to_studyMaterialsFragment)
        }
        binding.cardViewC.setOnClickListener {
            nav.navigate(R.id.action_teacherClassDetailsFragment_to_teacherAssignmentFragment)
        }
        binding.cardViewD.setOnClickListener {
            nav.navigate(R.id.action_teacherClassDetailsFragment_to_teacherAnnouncementsFragment)
        }
        binding.cardViewAttendance.setOnClickListener {
            val action = TeacherClassDetailsFragmentDirections.actionTeacherClassDetailsFragmentToTeacherParticipantsFragment2(
                course = args.course,
                initialTab = 1
            )
            nav.navigate(action)
        }
        binding.cardViewE.setOnClickListener {
            val action = TeacherClassDetailsFragmentDirections.actionTeacherClassDetailsFragmentToTeacherParticipantsFragment2(
                course = args.course,
                initialTab = 0
            )
            nav.navigate(action)
        }
    }



}