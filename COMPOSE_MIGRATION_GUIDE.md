# Jetpack Compose UI Migration Guide

## Overview
This document outlines the modern, minimalistic UI redesign of the Reater Android application using Jetpack Compose and Material 3 design system.

## What's New

### Design System
- **Material 3 Design**: Modern, clean aesthetic with proper elevation and spacing
- **8dp Grid System**: Consistent spacing throughout the app
- **Light Theme**: Soft colors with subtle elevations
- **Typography Hierarchy**: Clear visual hierarchy with proper font weights and sizes

### Key Features
- ✅ Fully modular and reusable composables
- ✅ Compatible with existing ViewModels (MVVM architecture)
- ✅ All existing fields and data preserved
- ✅ Modern search functionality
- ✅ Tab-based navigation for quizzes
- ✅ Smooth animations and transitions
- ✅ Material Icons for consistent iconography

## Project Structure

```
app/src/main/java/com/example/reater/ui/compose/
├── theme/
│   ├── Color.kt          # Material 3 color scheme
│   ├── Type.kt           # Typography definitions
│   └── Theme.kt          # Theme configuration
├── components/
│   ├── ClassCard.kt      # Reusable class/course card
│   ├── QuizCard.kt       # Reusable quiz card
│   └── SearchBar.kt      # Modern rounded search bar
├── screens/
│   ├── HomeScreen.kt     # Home screen with classes list
│   ├── ProfileScreen.kt  # Student profile screen
│   └── QuizScreen.kt     # Quiz screen with tabs
└── fragments/
    ├── ComposeHomeFragment.kt     # Compose wrapper for Home
    ├── ComposeProfileFragment.kt  # Compose wrapper for Profile
    └── ComposeQuizFragment.kt     # Compose wrapper for Quiz
```

## Dependencies Added

The following dependencies have been added to `app/build.gradle`:

```gradle
// Jetpack Compose
def composeBom = platform('androidx.compose:compose-bom:2024.02.00')
implementation composeBom
androidTestImplementation composeBom

implementation 'androidx.compose.material3:material3'
implementation 'androidx.compose.ui:ui'
implementation 'androidx.compose.ui:ui-tooling-preview'
debugImplementation 'androidx.compose.ui:ui-tooling'
implementation 'androidx.compose.material:material-icons-extended'
implementation 'androidx.activity:activity-compose:1.8.2'
implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0'
implementation 'androidx.lifecycle:lifecycle-runtime-compose:2.7.0'
implementation 'androidx.navigation:navigation-compose:2.7.7'
implementation 'io.coil-kt:coil-compose:2.6.0'
implementation 'androidx.hilt:hilt-navigation-compose:1.2.0'
```

## Screens Redesigned

### 1. Home Screen (`HomeScreen.kt`)

**Features:**
- Modern top app bar with greeting and profile button
- Rounded search bar for filtering classes
- Grid/List of class cards with course information
- Empty state handling

**Data Fields (Preserved):**
- SubjectID
- SubjectName
- ClassID
- TeacherID
- TeacherName
- Credits
- Semester
- AcademicSession

**Usage:**
```kotlin
HomeScreen(
    courses = coursesList,
    studentName = "John Doe",
    onClassClick = { coursework -> /* Navigate to details */ },
    onProfileClick = { /* Navigate to profile */ }
)
```

### 2. Profile Screen (`ProfileScreen.kt`)

**Features:**
- Centered profile avatar with student name
- Organized information cards with icons
- Sections for Personal and Academic information
- Clean, readable layout

**Data Fields (Preserved):**
- enrollmentID
- fullName
- degreeID
- classID
- phoneNumber
- enrollmentYear
- email
- dob

**Usage:**
```kotlin
ProfileScreen(
    student = studentData,
    onBackClick = { /* Navigate back */ }
)
```

### 3. Quiz Screen (`QuizScreen.kt`)

**Features:**
- Tab row for Upcoming/Past quizzes
- Search functionality
- Quiz cards with schedule, duration, and question count
- Empty state for each tab

**Data Fields (Preserved):**
- quizSchedule
- syllabus
- quizDuration
- subjectId
- quizList
- quizname
- classId

**Usage:**
```kotlin
QuizScreen(
    quizzes = quizzesList,
    onQuizClick = { quiz -> /* Start quiz */ },
    onBackClick = { /* Navigate back */ }
)
```

## Reusable Components

### ClassCard
A card component displaying course information with:
- Subject ID badge
- Subject name
- Teacher information with icon
- Semester and session details
- Credits badge

### QuizCard
A card component displaying quiz information with:
- Quiz icon and name
- Syllabus information
- Schedule and duration
- Question count badge

### ModernSearchBar
A rounded search bar with:
- Search icon
- Placeholder text
- Material 3 styling
- Single line input

## Integration with Existing Code

### Using Compose Fragments

The Compose screens are wrapped in Fragment classes for easy integration with existing Navigation Component:

```kotlin
// In your navigation graph, replace:
<fragment
    android:id="@+id/homeFragment"
    android:name="com.example.reater.ui.fragments.HomeFragment"
    ... />

// With:
<fragment
    android:id="@+id/homeFragment"
    android:name="com.example.reater.ui.compose.fragments.ComposeHomeFragment"
    ... />
```

### ViewModel Integration

The Compose fragments use the existing ViewModels:

```kotlin
@AndroidEntryPoint
class ComposeHomeFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }
    
    // Use mainViewModel to observe data and pass to Composables
}
```

### Observing LiveData in Compose

To observe LiveData from ViewModels in Compose:

```kotlin
val courses by mainViewModel.StudentClassResponse.observeAsState()

HomeScreen(
    courses = courses?.data ?: emptyList(),
    // ...
)
```

## Migration Steps

### Step 1: Sync Gradle
After adding dependencies, sync your project:
```bash
./gradlew clean build
```

### Step 2: Test Individual Screens
Test each Compose screen independently:
1. Run `ComposeHomeFragment`
2. Run `ComposeProfileFragment`
3. Run `ComposeQuizFragment`

### Step 3: Update Navigation
Update your navigation graph to use Compose fragments:
- Replace fragment class names
- Keep all navigation actions and arguments

### Step 4: Connect ViewModels
Replace sample data with actual ViewModel data:
```kotlin
// Observe ViewModel data
mainViewModel.getStudentClasses(queries)
mainViewModel.StudentClassResponse.observe(viewLifecycleOwner) { response ->
    when (response) {
        is NetworkResult.Success -> {
            // Update Compose state
        }
        // Handle other cases
    }
}
```

### Step 5: Handle Navigation
Implement navigation actions in click handlers:
```kotlin
onClassClick = { coursework ->
    val action = ComposeHomeFragmentDirections
        .actionHomeFragmentToClassDetails(coursework)
    findNavController().navigate(action)
}
```

## Design Specifications

### Colors
- **Primary**: #6750A4 (Purple)
- **Primary Container**: #EADDFF (Light Purple)
- **Secondary**: #625B71 (Gray Purple)
- **Background**: #FFFBFE (Off White)
- **Surface**: #FFFBFE (Off White)

### Typography
- **Display**: 36-57sp, Normal weight
- **Headline**: 24-32sp, SemiBold weight
- **Title**: 14-22sp, SemiBold weight
- **Body**: 12-16sp, Normal weight
- **Label**: 11-14sp, SemiBold weight

### Spacing
- **Extra Small**: 4dp
- **Small**: 8dp
- **Medium**: 16dp
- **Large**: 24dp
- **Extra Large**: 32dp

### Corner Radius
- **Small**: 8dp
- **Medium**: 12dp
- **Large**: 16dp
- **Extra Large**: 20dp
- **Full**: 28dp (for search bar)

## Best Practices

### 1. State Management
```kotlin
// Use remember for UI state
var searchQuery by remember { mutableStateOf("") }

// Use derivedStateOf for computed values
val filteredList = remember(searchQuery, items) {
    items.filter { it.name.contains(searchQuery, ignoreCase = true) }
}
```

### 2. Performance
```kotlin
// Use keys in LazyColumn
LazyColumn {
    items(courses, key = { it.SubjectID }) { course ->
        ClassCard(coursework = course, onClick = { })
    }
}
```

### 3. Theming
```kotlin
// Always wrap content in theme
ReaterTheme {
    HomeScreen(/* ... */)
}
```

### 4. Preview
```kotlin
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ReaterTheme {
        HomeScreen(
            courses = sampleCourses,
            studentName = "John Doe",
            onClassClick = {},
            onProfileClick = {}
        )
    }
}
```

## Testing

### Unit Tests
Test composables with Compose Testing:
```kotlin
@Test
fun homeScreen_displaysClasses() {
    composeTestRule.setContent {
        ReaterTheme {
            HomeScreen(courses = testCourses, ...)
        }
    }
    
    composeTestRule.onNodeWithText("COMPUTER-NETWORKS").assertExists()
}
```

### UI Tests
Test navigation and interactions:
```kotlin
@Test
fun clickingClass_navigatesToDetails() {
    // Test navigation behavior
}
```

## Troubleshooting

### Issue: Compose not compiling
**Solution**: Ensure Kotlin version is compatible (1.9.x for Compose 1.5.x)

### Issue: Colors not applying
**Solution**: Verify you're using `MaterialTheme.colorScheme` not `MaterialTheme.colors`

### Issue: Navigation not working
**Solution**: Ensure `findNavController()` is called from Fragment, not Composable

### Issue: ViewModel not updating UI
**Solution**: Use `observeAsState()` or `collectAsState()` to observe LiveData/Flow

## Future Enhancements

1. **Dark Theme Support**: Add dark color scheme
2. **Animations**: Add shared element transitions
3. **Accessibility**: Improve content descriptions and semantics
4. **Offline Support**: Add loading and error states
5. **Pull to Refresh**: Add swipe refresh functionality
6. **Skeleton Loading**: Add shimmer effects while loading

## Resources

- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Material 3 Design](https://m3.material.io/)
- [Compose Samples](https://github.com/android/compose-samples)
- [Migration Guide](https://developer.android.com/jetpack/compose/migrate)

## Support

For questions or issues with the Compose implementation, please refer to:
- Android Developers Slack: #compose
- Stack Overflow: [android-jetpack-compose]
- GitHub Issues: [Project Repository]

---

**Last Updated**: April 2026
**Version**: 1.0.0
**Author**: Kiro AI Assistant
