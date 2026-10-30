package com.example.reater.teacherapp.fragments.TeacherStudyMaterial

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reater.adapters.FilesAdapter
import com.example.reater.databinding.FragmentStudyMaterialsBinding
import com.example.reater.models.StudyMaterial
import com.example.reater.utils.FilePreviewHelper
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class StudyMaterialsFragment : Fragment() {

    private var _binding: FragmentStudyMaterialsBinding? = null
    private val binding get() = _binding!!

    private val args: StudyMaterialsFragmentArgs by navArgs()
    private lateinit var mainViewModel: MainViewModel
    private val filesAdapter by lazy { FilesAdapter() }

    private var selectedFileUri: Uri? = null
    private var selectedFileName: String? = null

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            selectedFileUri = uri
            val name = getFileName(uri)
            selectedFileName = name
            binding.tvSelectedFileName.text = name

            if (binding.etCaption.text.isNullOrBlank()) {
                val cleanName = name.substringBeforeLast(".")
                binding.etCaption.setText(cleanName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudyMaterialsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val course = args.course
        val subjectCode = course?.SubjectID ?: "SUB1"
        val subjectName = course?.SubjectName ?: "Subject"

        binding.tvCourseSubtitle.text = "$subjectCode • $subjectName"
        binding.studyToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        setupRecyclerView()
        setupListeners(subjectCode)
        observeViewModel()

        mainViewModel.fetchStudyMaterials(subjectCode)
    }

    private fun setupRecyclerView() {
        binding.studyMaterialRv.layoutManager = LinearLayoutManager(requireContext())
        binding.studyMaterialRv.adapter = filesAdapter
        filesAdapter.onItemClick = { material ->
            FilePreviewHelper.openFile(requireContext(), material.fileLocation)
        }
    }

    private fun setupListeners(subjectCode: String) {
        binding.btnUpload.setOnClickListener {
            filePickerLauncher.launch("*/*")
        }

        binding.btnSendToClass.setOnClickListener {
            val uri = selectedFileUri
            if (uri == null) {
                Toast.makeText(requireContext(), "Please select a file first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val caption = binding.etCaption.text?.toString()?.trim() ?: ""
            val finalCaption = if (caption.isNotBlank()) caption else (selectedFileName ?: "Study Material")

            val cacheFile = copyUriToCache(uri, selectedFileName ?: "material.pdf")
            if (cacheFile == null) {
                Toast.makeText(requireContext(), "Failed to read selected file", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.uploadProgressBar.visibility = View.VISIBLE
            binding.btnSendToClass.isEnabled = false

            mainViewModel.uploadStudyMaterial(subjectCode, finalCaption, cacheFile) { success ->
                binding.uploadProgressBar.visibility = View.GONE
                binding.btnSendToClass.isEnabled = true

                if (success) {
                    Toast.makeText(requireContext(), "Resource published to class!", Toast.LENGTH_SHORT).show()
                    selectedFileUri = null
                    selectedFileName = null
                    binding.tvSelectedFileName.text = "No file selected"
                    binding.etCaption.text?.clear()
                    mainViewModel.fetchStudyMaterials(subjectCode)
                } else {
                    Toast.makeText(requireContext(), "Failed to upload resource. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeViewModel() {
        mainViewModel.studyMaterialsResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val materials = response.data ?: emptyList()
                    if (materials.isEmpty()) {
                        binding.emptyStateLayout.visibility = View.VISIBLE
                        binding.studyMaterialRv.visibility = View.GONE
                        binding.tvResourceCount.text = "0 resources"
                    } else {
                        binding.emptyStateLayout.visibility = View.GONE
                        binding.studyMaterialRv.visibility = View.VISIBLE
                        binding.tvResourceCount.text = "${materials.size} resources"
                        filesAdapter.setData(materials)
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message ?: "Failed to load materials", Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    // Handled if needed
                }
            }
        }
    }

    private fun copyUriToCache(uri: Uri, name: String): File? {
        return try {
            val ext = if (name.contains(".")) ".${name.substringAfterLast(".")}" else ".pdf"
            val tempFile = File(requireContext().cacheDir, "upload_${System.currentTimeMillis()}$ext")
            requireContext().contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(tempFile).use { output ->
                    input.copyTo(output)
                }
            }
            tempFile
        } catch (e: Exception) {
            null
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
        return result ?: "Document.pdf"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}