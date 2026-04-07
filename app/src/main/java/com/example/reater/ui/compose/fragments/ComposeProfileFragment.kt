package com.example.reater.ui.compose.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.reater.models.Student
import com.example.reater.ui.compose.screens.PremiumProfileScreen
import com.example.reater.ui.compose.theme.ReaterTheme

class ComposeProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReaterTheme {
                    // Sample student data - replace with actual data from ViewModel
                    val student = Student(
                        enrollmentID = "2021IT001",
                        fullName = "Alex Johnson",
                        degreeID = "B.Tech Information Technology",
                        classID = "IT-E",
                        phoneNumber = "+91 9876543210",
                        enrollmentYear = "2021",
                        email = "alex.johnson@university.edu",
                        dob = "15 August 2003"
                    )

                    PremiumProfileScreen(
                        student = student,
                        onBackClick = {
                            findNavController().navigateUp()
                        }
                    )
                }
            }
        }
    }
}
