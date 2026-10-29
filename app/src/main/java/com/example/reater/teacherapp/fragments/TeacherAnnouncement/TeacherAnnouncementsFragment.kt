package com.example.reater.teacherapp.fragments.TeacherAnnouncement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.R
import com.example.reater.adapters.AnnouncementsAdapter
import com.example.reater.databinding.FragmentTeacherAnnouncementsBinding
import com.example.reater.models.Announcement
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TeacherAnnouncementsFragment : Fragment() {
    private var _binding: FragmentTeacherAnnouncementsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val announcementAdapter by lazy { AnnouncementsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTeacherAnnouncementsBinding.inflate(layoutInflater, container, false)

        binding.teacherAnnouncementsRv.adapter = announcementAdapter
        binding.teacherAnnouncementsRv.layoutManager = LinearLayoutManager(requireContext())

        binding.floatingActionButtonannouncement.setOnClickListener {
            // Navigate using safe args if available, otherwise direct
            try {
                val action = TeacherAnnouncementsFragmentDirections
                    .actionTeacherAnnouncementsFragmentToTeacherPostFragment("CS-A", "SUB1", "General")
                findNavController().navigate(action)
            } catch (e: Exception) {
                findNavController().navigate(R.id.action_teacherAnnouncementsFragment_to_teacherPostFragment)
            }
        }

        observeViewModel()
        mainViewModel.fetchNotices()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchNotices()
    }

    private fun observeViewModel() {
        mainViewModel.noticesResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val notices = result.data ?: emptyList()
                    val announcements = notices.map { notice ->
                        val authorName = when {
                            notice.author.isNotBlank() -> notice.author
                            notice.teacherName.isNotBlank() -> notice.teacherName
                            notice.teacher == "111" -> "Dr. John Doe"
                            notice.teacher == "222" -> "Dr. Jane Smith"
                            notice.teacher == "333" -> "Dr. Alan Turing"
                            else -> "Teacher ${notice.teacher}"
                        }
                        Announcement(
                            author = authorName,
                            timeStamp = if (notice.createdAt.length >= 10) notice.createdAt.take(10) else notice.createdAt,
                            authorpicture = "",
                            description = "${notice.title}\n\n${notice.description}"
                        )
                    }
                    announcementAdapter.setData(announcements)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), result.message ?: "Failed to load", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> { }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}