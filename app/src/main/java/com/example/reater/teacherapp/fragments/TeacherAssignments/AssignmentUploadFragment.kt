package com.example.reater.teacherapp.fragments.TeacherAssignments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reater.databinding.FragmentAssignmentCreateBinding
import com.example.reater.utils.FilePreviewHelper
import com.example.reater.utils.NetworkResult
import com.example.reater.utils.TokenManager
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AssignmentUploadFragment : Fragment() {
    private var _binding: FragmentAssignmentCreateBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    private lateinit var mainViewModel: MainViewModel
    private var selectedPdfUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val copiedUri = copyToCache(uri)
            selectedPdfUri = copiedUri
            val fileName = getFileName(copiedUri ?: uri)
            binding.tvAttachedFileName.text = "📎 ${fileName ?: "File Attached"} (Tap to Preview, Hold to Change)"
            Toast.makeText(requireContext(), "File Attached! Tap file to preview.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyToCache(uri: Uri): Uri? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val ext = getFileExtension(uri) ?: "pdf"
            val file = java.io.File(requireContext().cacheDir, "upload_${System.currentTimeMillis()}.$ext")
            val outputStream = java.io.FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            Uri.fromFile(file)
        } catch (e: Exception) {
            null
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        return requireContext().contentResolver.getType(uri)?.substringAfterLast('/') ?: "pdf"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentCreateBinding.inflate(inflater, container, false)
        setupListeners()
        observeViewModel()
        return binding.root
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnAttachPdf.setOnClickListener {
            if (selectedPdfUri != null) {
                FilePreviewHelper.openFile(requireContext(), selectedPdfUri.toString())
            } else {
                getContent.launch("*/*")
            }
        }

        binding.btnAttachPdf.setOnLongClickListener {
            getContent.launch("*/*")
            true
        }

        binding.submitAssignment.setOnClickListener {
            publishAssignment()
        }
    }

    private fun observeViewModel() {
        mainViewModel.createAssignmentResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Loading -> {
                    binding.submitAssignment.isEnabled = false
                    binding.tvSubmitAssignment.text = "Publishing..."
                }
                is NetworkResult.Success -> {
                    binding.submitAssignment.isEnabled = true
                    binding.tvSubmitAssignment.text = "Publish Assignment"
                    Toast.makeText(requireContext(), "Assignment Published Successfully!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is NetworkResult.Error -> {
                    binding.submitAssignment.isEnabled = true
                    binding.tvSubmitAssignment.text = "Publish Assignment"
                    Toast.makeText(requireContext(), result.message ?: "Failed to publish", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun publishAssignment() {
        val title = binding.createdAssignmentName.text.toString().trim()
        val dueDate = binding.editDueDate.text.toString().trim()
        val instructions = binding.editAssignmentInstructions.text.toString().trim()
        val sectionId = binding.editSectionId.text.toString().trim().ifEmpty { "CS-A" }

        if (title.isEmpty() || dueDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill title and due date", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedPdfUri == null) {
            Toast.makeText(requireContext(), "Please attach a file", Toast.LENGTH_SHORT).show()
            return
        }

        // Get the teacher's subject code from their class list
        val courses = (mainViewModel.teacherCoursesResponse.value as? NetworkResult.Success)?.data
        val subjectCode = courses?.firstOrNull()?.SubjectID ?: "SUB1"

        val pdfFile = java.io.File(selectedPdfUri!!.path ?: "")
        if (!pdfFile.exists()) {
            Toast.makeText(requireContext(), "File not accessible. Please re-select.", Toast.LENGTH_SHORT).show()
            return
        }

        mainViewModel.createTeacherAssignment(
            name = title,
            description = instructions,
            dueDate = dueDate,
            sectionCode = sectionId,
            subjectCode = subjectCode,
            totalMarks = 100,
            pdfFile = pdfFile
        )
    }

    private fun getFileName(uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (columnIndex >= 0) {
                        result = cursor.getString(columnIndex)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}