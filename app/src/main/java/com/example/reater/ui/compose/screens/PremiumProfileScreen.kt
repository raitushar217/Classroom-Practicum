package com.example.reater.ui.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reater.models.Student
import com.example.reater.ui.compose.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumProfileScreen(
    student: Student?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = BackgroundLight
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Premium Profile Header with gradient background
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Gradient Background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        GradientStart,
                                        GradientEnd
                                    )
                                )
                            )
                    )

                    // Profile Card
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 120.dp)
                            .shadow(
                                elevation = 8.dp,
                                shape = RoundedCornerShape(24.dp),
                                spotColor = ShadowColor
                            ),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardBackground
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Avatar positioned to overlap
                            Surface(
                                modifier = Modifier
                                    .size(100.dp)
                                    .offset(y = (-70).dp)
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = CircleShape,
                                        spotColor = ShadowColor
                                    ),
                                shape = CircleShape,
                                color = CardBackground
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            brush = Brush.linearGradient(
                                                colors = listOf(PrimaryBlue, AccentPurple)
                                            )
                                        )
                                ) {
                                    Text(
                                        text = student?.fullName?.firstOrNull()?.uppercase() ?: "S",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Name and ID (adjusted spacing)
                            Column(
                                modifier = Modifier.offset(y = (-50).dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = student?.fullName ?: "Student Name",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Enrollment ID Badge
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = PrimaryBlueLight.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = student?.enrollmentID ?: "Enrollment ID",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = PrimaryBlue,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Add spacing after header
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Personal Information Section
            item {
                SectionHeader(
                    title = "Personal Information",
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            item {
                PremiumInfoCard(
                    icon = Icons.Outlined.Email,
                    label = "Email Address",
                    value = student?.email ?: "Not available",
                    iconColor = PrimaryBlue,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            item {
                PremiumInfoCard(
                    icon = Icons.Outlined.Phone,
                    label = "Phone Number",
                    value = student?.phoneNumber ?: "Not available",
                    iconColor = AccentPurple,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            item {
                PremiumInfoCard(
                    icon = Icons.Outlined.Cake,
                    label = "Date of Birth",
                    value = student?.dob ?: "Not available",
                    iconColor = AccentOrange,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            // Academic Information Section
            item {
                SectionHeader(
                    title = "Academic Information",
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }

            item {
                PremiumInfoCard(
                    icon = Icons.Outlined.School,
                    label = "Degree Program",
                    value = student?.degreeID ?: "Not available",
                    iconColor = SuccessGreen,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            item {
                PremiumInfoCard(
                    icon = Icons.Outlined.Class,
                    label = "Class ID",
                    value = student?.classID ?: "Not available",
                    iconColor = PrimaryBlue,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            item {
                PremiumInfoCard(
                    icon = Icons.Outlined.CalendarToday,
                    label = "Enrollment Year",
                    value = student?.enrollmentYear ?: "Not available",
                    iconColor = AccentPurple,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        color = TextPrimary,
        fontWeight = FontWeight.Bold,
        modifier = modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun PremiumInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    iconColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = ShadowColor
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = iconColor.copy(alpha = 0.12f),
                modifier = Modifier.size(52.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = label,
                        tint = iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
