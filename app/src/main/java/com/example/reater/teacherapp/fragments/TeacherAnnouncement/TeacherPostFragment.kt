package com.example.reater.teacherapp.fragments.TeacherAnnouncement

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reater.R
import com.example.reater.databinding.FragmentTeacherPostBinding
import com.example.reater.utils.NetworkResult
import com.example.reater.utils.TokenManager
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherPostFragment : Fragment() {

    private var _binding: FragmentTeacherPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var tokenManager: TokenManager

    private var attachedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            attachedImageUri = uri
            val fileName = getFileName(uri)
            binding.tvAttachedImageName.text = "Attached: $fileName"
            binding.ivImagePreview.setImageURI(uri)
            binding.ivImagePreview.visibility = View.VISIBLE
            Toast.makeText(requireContext(), "Image attached!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTeacherPostBinding.inflate(layoutInflater, container, false)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAttachImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.teacherSmSubmitButtonLayout.setOnClickListener {
            postAnnouncement()
        }

        observeViewModel()

        return binding.root
    }

    private fun postAnnouncement() {
        val text = binding.teacherAnnouncementText.text.toString().trim()
        if (text.isEmpty()) {
            Toast.makeText(requireContext(), "Please add announcement text", Toast.LENGTH_SHORT).show()
            return
        }

        // Use teacher's name from TokenManager as title prefix
        val teacherName = tokenManager.getUserName() ?: "Teacher"
        val title = "Announcement by $teacherName"
        mainViewModel.createNotice(title = title, description = text)
    }

    private fun observeViewModel() {
        mainViewModel.createNoticeResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.teacherSmSubmitButtonLayout.isEnabled = false
                }
                is NetworkResult.Success -> {
                    binding.teacherSmSubmitButtonLayout.isEnabled = true
                    Toast.makeText(requireContext(), "Announcement posted successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_teacherPostFragment_to_teacherAnnouncementsFragment)
                }
                is NetworkResult.Error -> {
                    binding.teacherSmSubmitButtonLayout.isEnabled = true
                    Toast.makeText(requireContext(), result.message ?: "Failed to post", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = cursor.getString(index)
                    }
                }
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != null && cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "Unknown Image"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
