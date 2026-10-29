package com.example.reater.ui.compose.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reater.R
import com.example.reater.models.Coursework
import com.example.reater.ui.compose.screens.HomeScreen
import com.example.reater.ui.compose.theme.ReaterTheme
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Example of ComposeHomeFragment with full ViewModel integration
 * This demonstrates how to connect the Compose UI with existing ViewModels
 * and handle loading, success, and error states
 */
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ComposeHomeFragmentWithViewModel : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        
        // Request data from API
        requestAPIData()
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
                    HomeScreenWithViewModel(
                        mainViewModel = mainViewModel,
                        onClassClick = { coursework ->
                            // Navigate to class details
                            // Example: findNavController().navigate(
                            //     R.id.action_homeFragment_to_classDetails,
                            //     bundleOf("coursework" to coursework)
                            // )
                        },
                        onProfileClick = {
                            findNavController().navigate(
                                R.id.action_homeFragment_to_userProfileFragment
                            )
                        },
                        onError = { message ->
                            Toast.makeText(
                                requireContext(),
                                message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    }

    private fun requestAPIData() {
        mainViewModel.fetchStudentCourses()
    }

    private fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        return queries
    }
}

@Composable
fun HomeScreenWithViewModel(
    mainViewModel: MainViewModel,
    onClassClick: (Coursework) -> Unit,
    onProfileClick: () -> Unit,
    onError: (String) -> Unit
) {
    // Observe the LiveData from ViewModel
    val coursesResponse by mainViewModel.studentCoursesResponse.observeAsState()

    when (val response = coursesResponse) {
        is NetworkResult.Loading<*> -> {
            // Show loading indicator
            LoadingScreen()
        }

        is NetworkResult.Success<*> -> {
            val courses = (response as? NetworkResult.Success<List<Coursework>>)?.data ?: emptyList()

            HomeScreen(
                courses = courses,
                studentName = "Student",
                onClassClick = onClassClick,
                onProfileClick = onProfileClick
            )
        }

        is NetworkResult.Error<*> -> {
            // Show error screen
            ErrorScreen(
                message = response.message ?: "An error occurred",
                onRetry = {
                    mainViewModel.fetchStudentCourses()
                }
            )
            
            // Also show toast
            response.message?.let { onError(it) }
        }

        null -> {
            // Initial state - show loading or empty state
            LoadingScreen()
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Error: $message")
        // Add retry button if needed
    }
}
