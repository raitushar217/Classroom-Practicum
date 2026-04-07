package com.example.reater.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.reater.models.Announcement
import com.example.reater.models.StudyMaterial
import com.example.reater.ui.compose.screens.PremiumCourseDetailsScreen
import com.example.reater.ui.compose.theme.ReaterTheme
import com.example.reater.utils.NetworkResult
import com.example.reater.viewmodels.FirebaseViewmodel

class DetailsActivity : AppCompatActivity() {
    private val args: DetailsActivityArgs by navArgs()
    private lateinit var firebaseViewmodel: FirebaseViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        firebaseViewmodel = ViewModelProvider(this)[FirebaseViewmodel::class.java]
        val coursework = args.coursework
        
        // Request data
        firebaseViewmodel.getAnnouncementsRequest(coursework.ClassID, coursework.SubjectID)
        firebaseViewmodel.getStudyMaterials(coursework.ClassID, coursework.SubjectID)
        
        setContent {
            ReaterTheme {
                val announcementsResponse by firebaseViewmodel.AnnouncementsResponse.observeAsState()
                val filesResponse by firebaseViewmodel.filesResponse.observeAsState()
                
                var announcements by remember { mutableStateOf(emptyList<Announcement>()) }
                var studyMaterials by remember { mutableStateOf(emptyList<StudyMaterial>()) }
                
                // Handle announcements response
                LaunchedEffect(announcementsResponse) {
                    when (announcementsResponse) {
                        is NetworkResult.Success<*> -> {
                            (announcementsResponse as? NetworkResult.Success<List<Announcement>>)?.data?.let {
                                announcements = it
                            }
                        }
                        is NetworkResult.Error<*> -> {
                            Toast.makeText(
                                this@DetailsActivity,
                                (announcementsResponse as NetworkResult.Error<*>).message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is NetworkResult.Loading<*> -> {
                            // Show loading
                        }
                        else -> {}
                    }
                }
                
                // Handle files response
                LaunchedEffect(filesResponse) {
                    when (filesResponse) {
                        is NetworkResult.Success<*> -> {
                            (filesResponse as? NetworkResult.Success<List<StudyMaterial>>)?.data?.let {
                                studyMaterials = it
                            }
                        }
                        is NetworkResult.Error<*> -> {
                            Toast.makeText(
                                this@DetailsActivity,
                                (filesResponse as NetworkResult.Error<*>).message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is NetworkResult.Loading<*> -> {
                            // Show loading
                        }
                        else -> {}
                    }
                }
                
                PremiumCourseDetailsScreen(
                    coursework = coursework,
                    announcements = announcements,
                    studyMaterials = studyMaterials,
                    onBackClick = {
                        finish()
                    },
                    onAnnouncementClick = { announcement ->
                        // Handle announcement click
                        Toast.makeText(
                            this@DetailsActivity,
                            announcement.author,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onFileClick = { material ->
                        // Handle file click - open file
                        Toast.makeText(
                            this@DetailsActivity,
                            "Opening ${material.filename}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}