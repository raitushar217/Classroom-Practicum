package com.example.reater.ui.compose.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reater.R
import com.example.reater.models.Coursework
import com.example.reater.ui.compose.screens.PremiumHomeScreen
import com.example.reater.ui.compose.theme.ReaterTheme
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ComposeHomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ReaterTheme {
                    // Sample data - replace with actual data from ViewModel
                    val courses = listOf(
                        Coursework(
                            "IT2101",
                            "Computer Networks",
                            "IT-E",
                            "TID001",
                            "Dr. Sudhir Sharma",
                            "4",
                            "3rd Semester",
                            "JUL-NOV 2021"
                        ),
                        Coursework(
                            "IT2104",
                            "Data Communications",
                            "IT-E",
                            "TID0054",
                            "Dr. Pankaj Vyas",
                            "3",
                            "5th Semester",
                            "JUL-NOV 2021"
                        ),
                        Coursework(
                            "IT2130",
                            "Fundamentals of Data Science",
                            "IT-E",
                            "TID0052",
                            "Dr. Veena Khandelwal",
                            "4",
                            "3rd Semester",
                            "JUL-NOV 2022"
                        ),
                        Coursework(
                            "IT2131",
                            "Operating Systems",
                            "IT-E",
                            "TID005",
                            "Dr. Devesh Choudhary",
                            "4",
                            "4th Semester",
                            "JUL-NOV 2022"
                        ),
                        Coursework(
                            "MA2101",
                            "Engineering Mathematics III",
                            "IT-E",
                            "TID001",
                            "Dr. Sudhir Sharma",
                            "4",
                            "5th Semester",
                            "JUL-NOV 2022"
                        )
                    )

                    PremiumHomeScreen(
                        courses = courses,
                        studentName = "Alex Johnson",
                        onClassClick = { coursework ->
                            // Navigate to class details
                            // You can use Navigation component here
                        },
                        onProfileClick = {
                            findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment)
                        }
                    )
                }
            }
        }
    }
}
