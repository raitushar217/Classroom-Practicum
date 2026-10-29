package com.example.reater.ui.fragments

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
import androidx.navigation.fragment.navArgs
import com.example.reater.databinding.FragmentAssignmentDetailsBinding
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AssignmentDetailsFragment : Fragment() {
    private var _binding: FragmentAssignmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: AssignmentDetailsFragmentArgs by navArgs()
    private lateinit var mainViewModel: MainViewModel
    private var selectedPdfFile: File? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val fileName = getFileName(it)
            binding.assignmentUploadBox.visibility = View.VISIBLE
            binding.textView47.setText(fileName)

            try {
                val cacheFile = File(requireContext().cacheDir, "sub_${System.currentTimeMillis()}.pdf")
                requireContext().contentResolver.openInputStream(it)?.use { input ->
                    FileOutputStream(cacheFile).use { output ->
                        input.copyTo(output)
                    }
                }
                selectedPdfFile = cacheFile
                Toast.makeText(requireContext(), "File ready for submission", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error preparing file: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (index >= 0) {
                        result = cursor.getString(index)
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
        return result ?: "Unknown file"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentDetailsBinding.inflate(inflater, container, false)
        binding.assignment = args.assignment

        binding.assignmentSubmitButton.setOnClickListener {
            if (selectedPdfFile != null && selectedPdfFile!!.exists()) {
                val assignmentId = args.assignment.assignmentid ?: "1"
                mainViewModel.postStudentAssignment(assignmentId, selectedPdfFile!!)
                Toast.makeText(requireContext(), "Submitting assignment to server...", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Please select a file to upload first.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.constraintLayout6.setOnClickListener {
            getContent.launch("*/*")
        }

        args.assignment.fileUri?.let { uriString ->
            binding.ivUploadedImage.visibility = View.VISIBLE
            binding.ivUploadedImage.setImageURI(Uri.parse(uriString))
            binding.textView43.visibility = View.VISIBLE
        } ?: run {
            binding.ivUploadedImage.visibility = View.GONE
            binding.textView43.visibility = View.GONE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}