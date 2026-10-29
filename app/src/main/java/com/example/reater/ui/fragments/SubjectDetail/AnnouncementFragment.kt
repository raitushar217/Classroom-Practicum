package com.example.reater.ui.fragments.SubjectDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.AnnouncementsAdapter
import com.example.reater.databinding.FragmentAnnouncementsBinding
import com.example.reater.models.Announcement
import com.example.reater.models.Coursework
import com.example.reater.utils.Constants
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnnouncementFragment : Fragment() {

    private var _binding: FragmentAnnouncementsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private val announcementAdapter by lazy { AnnouncementsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAnnouncementsBinding.inflate(inflater, container, false)

        setupRecyclerView()
        observeViewModel()
        mainViewModel.fetchNotices()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.fetchNotices()
    }

    private fun setupRecyclerView() {
        binding.announcementRv.adapter = announcementAdapter
        binding.announcementRv.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        mainViewModel.noticesResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Loading -> {
                    // Show loading if progress bar available
                }
                is NetworkResult.Success -> {
                    val notices = response.data ?: emptyList()
                    // Convert Notice → Announcement for display
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
                    Toast.makeText(
                        requireContext(),
                        response.message ?: "Failed to load announcements",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}