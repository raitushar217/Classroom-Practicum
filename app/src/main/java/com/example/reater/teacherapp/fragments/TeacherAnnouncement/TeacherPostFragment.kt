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
import com.example.reater.models.Announcement
import com.example.reater.viewmodels.FirebaseViewmodel
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.*

class TeacherPostFragment : Fragment() {

    private var _binding: FragmentTeacherPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseViewmodel: FirebaseViewmodel
    var authordp = "SADASD"
    var authorname = "Veena Khandelwal"
    var classid: String = "IT-E"
    var subjectID: String = "IT2121"
    
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
        firebaseViewmodel = ViewModelProvider(requireActivity())[FirebaseViewmodel::class.java]
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
            addAnnouncement(binding.teacherSmSubmitButtonLayout)
        }
        
        return binding.root
    }

    private fun addAnnouncement(layout: MaterialCardView) {
        val text = binding.teacherAnnouncementText.text.toString().trim()
        if (text.isEmpty() && attachedImageUri == null) {
            Toast.makeText(requireContext(), "Please add text or an image", Toast.LENGTH_SHORT).show()
            return
        }
        
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().time)
        val announcement = Announcement(authorname, timestamp, authordp, text)
        
        firebaseViewmodel.addAnnouncement(layout, classid, subjectID, announcement)
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
