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
import androidx.navigation.fragment.navArgs
import com.example.reater.databinding.FragmentAssignmentDetailsBinding


class AssignmentDetailsFragment : Fragment() {
    private var _binding:FragmentAssignmentDetailsBinding?=null
    private val binding get() = _binding!!
    private val args: AssignmentDetailsFragmentArgs by navArgs()

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            val fileName = getFileName(it)
            binding.assignmentUploadBox.visibility = View.VISIBLE
            binding.textView47.setText(fileName)
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAssignmentDetailsBinding.inflate(inflater, container, false)
        binding.assignment = args.assignment
        
        binding.assignmentSubmitButton.setOnClickListener {
            if (binding.assignmentUploadBox.visibility == View.VISIBLE) {
                Toast.makeText(requireContext(), "Assignment Turned In!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please select a file to upload first.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.constraintLayout6.setOnClickListener {
            getContent.launch("*/*")
        }

        return binding.root
    }

}