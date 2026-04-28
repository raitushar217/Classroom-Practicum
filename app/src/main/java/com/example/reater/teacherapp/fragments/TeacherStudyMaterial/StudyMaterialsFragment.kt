package com.example.reater.teacherapp.fragments.TeacherStudyMaterial

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.reater.R
import com.example.reater.databinding.FragmentStudyMaterialsBinding

import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.ViewModelProvider
import com.example.reater.models.StudyMaterial
import com.example.reater.viewmodels.FirebaseViewmodel
import androidx.navigation.fragment.findNavController

class StudyMaterialsFragment : Fragment() {

    private var _binding: FragmentStudyMaterialsBinding? = null
    private val binding get() = _binding!!

    private val selectedFiles = mutableListOf<Pair<String, Uri>>()
    private lateinit var firebaseViewmodel: FirebaseViewmodel
    var classID: String = "IT-E"
    var subjectID: String = "IT2125"
    var authorname = "Veena Khandelwal"
    var authordp = "sdhasdas"
    
    // Modern file picker launcher using ActivityResultContracts
    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            val fileName = getFileName(uri)
            selectedFiles.add(Pair(fileName, uri))
            updateUI()
        } else {
            Toast.makeText(requireContext(), "No file selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewmodel = ViewModelProvider(requireActivity())[FirebaseViewmodel::class.java]
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

        binding.studyMaterialRv.layoutManager = LinearLayoutManager(requireContext())
        binding.studyMaterialRv.adapter = SelectedFilesAdapter()

        binding.btnUpload.setOnClickListener {
            // Launch file picker for all file types (use "application/pdf" for PDFs, etc.)
            filePickerLauncher.launch("*/*")
        }

        binding.btnSendToClass.setOnClickListener {
            if (selectedFiles.isNotEmpty()) {
                val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time)
                
                for (fileInfo in selectedFiles) {
                    val fileLocation = UUID.randomUUID().toString()
                    val studyMaterial = StudyMaterial(fileInfo.first, timestamp, authorname, authordp, fileLocation)
                    firebaseViewmodel.addMaterial(studyMaterial, classID, subjectID, fileInfo.second, fileLocation)
                }
                
                Toast.makeText(requireContext(), "Uploading resources to class...", Toast.LENGTH_SHORT).show()
                // Clear the list after sending
                selectedFiles.clear()
                updateUI()
            }
        }
    }

    private fun updateUI() {
        if (selectedFiles.isEmpty()) {
            binding.emptyStateLayout.visibility = View.VISIBLE
            binding.studyMaterialRv.visibility = View.GONE
            binding.btnSendToClass.visibility = View.GONE
        } else {
            binding.emptyStateLayout.visibility = View.GONE
            binding.studyMaterialRv.visibility = View.VISIBLE
            binding.btnSendToClass.visibility = View.VISIBLE
            binding.studyMaterialRv.adapter?.notifyDataSetChanged()
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
        return result ?: "Unknown File"
    }

    inner class SelectedFilesAdapter : RecyclerView.Adapter<SelectedFilesAdapter.FileViewHolder>() {

        inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val fileNameText: TextView = itemView.findViewById(R.id.file_name_text)
            val btnRemove: ImageView = itemView.findViewById(R.id.btn_remove_file)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_selected_file, parent, false)
            return FileViewHolder(view)
        }

        override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
            holder.fileNameText.text = selectedFiles[position].first
            holder.btnRemove.setOnClickListener {
                selectedFiles.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, selectedFiles.size)
                updateUI()
            }
        }

        override fun getItemCount(): Int = selectedFiles.size
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}