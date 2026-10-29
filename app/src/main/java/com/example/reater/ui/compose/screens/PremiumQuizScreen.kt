package com.example.reater.ui.compose.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reater.models.Quiz
import com.example.reater.ui.compose.components.PremiumQuizCard
import com.example.reater.ui.compose.components.PremiumSearchBar
import com.example.reater.ui.compose.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumQuizScreen(
    quizzes: List<Quiz>,
    onQuizClick: (Quiz) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTabIndex by remember { mutableStateOf(0) }

    val tabs = listOf("Upcoming", "Past")

    // Filter quizzes based on search and tab
    val filteredQuizzes = remember(searchQuery, quizzes, selectedTabIndex) {
        val searchFiltered = if (searchQuery.isBlank()) {
            quizzes
        } else {
            quizzes.filter {
                (it.quizname?.contains(searchQuery, ignoreCase = true) == true) ||
                        (it.syllabus?.contains(searchQuery, ignoreCase = true) == true)
            }
        }

        when (selectedTabIndex) {
            0 -> searchFiltered // Upcoming
            1 -> emptyList() // Past (empty for demo)
            else -> searchFiltered
        }
    }

    Scaffold(
        containerColor = BackgroundLight
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Premium Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                AccentPurple,
                                AccentPurple.copy(alpha = 0.95f)
                            )
                        )
                    )
                    .padding(start = 24.dp, end = 24.dp, top = 48.dp, bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Quizzes",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Test your knowledge",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Search Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-20).dp)
            ) {
                PremiumSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Search quizzes..."
                )
            }

            // Premium Tab Row with better styling
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                color = CardBackground,
                shadowElevation = 2.dp
            ) {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.Transparent,
                    contentColor = PrimaryBlue,
                    indicator = { tabPositions ->
                        if (selectedTabIndex < tabPositions.size) {
                            Box(
                                modifier = Modifier
                                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                                    .height(4.dp)
                                    .padding(horizontal = 32.dp)
                                    .background(
                                        brush = Brush.horizontalGradient(
                                            colors = listOf(AccentPurple, AccentPurpleLight)
                                        ),
                                        shape = RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = if (selectedTabIndex == index) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Medium
                                },
                                color = if (selectedTabIndex == index) {
                                    AccentPurple
                                } else {
                                    TextSecondary
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quiz List
            if (filteredQuizzes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = BackgroundLight,
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Text(
                                    text = "📝",
                                    style = MaterialTheme.typography.displayMedium
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (selectedTabIndex == 0) "No upcoming quizzes" else "No past quizzes",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Check back later for updates",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredQuizzes) { quiz ->
                        PremiumQuizCard(
                            quiz = quiz,
                            onClick = { onQuizClick(quiz) }
                        )
                    }
                }
            }
        }
    }
}

// Extension function for tab indicator offset
@Composable
private fun Modifier.tabIndicatorOffset(
    currentTabPosition: TabPosition
): Modifier {
    return this.fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = currentTabPosition.left)
        .width(currentTabPosition.width)
}
