# Component Hierarchy & Architecture

## 🏗️ Architecture Overview

This document explains the component hierarchy and architecture of the Jetpack Compose implementation.

---

## 📐 Component Structure

### Theme Layer (Foundation)
```
ReaterTheme
├── MaterialTheme
│   ├── colorScheme (from Color.kt)
│   ├── typography (from Type.kt)
│   └── shapes (default Material 3)
```

### Screen Layer (Top Level)
```
Screen Composables
├── HomeScreen
├── ProfileScreen
└── QuizScreen
```

### Component Layer (Reusable)
```
Reusable Components
├── ClassCard
├── QuizCard
└── ModernSearchBar
```

### Fragment Layer (Integration)
```
Fragment Wrappers
├── ComposeHomeFragment
├── ComposeProfileFragment
├── ComposeQuizFragment
└── ComposeHomeFragmentWithViewModel
```

---

## 🏠 Home Screen Hierarchy

```
ComposeHomeFragment
└── ReaterTheme
    └── HomeScreen
        └── Scaffold
            ├── TopAppBar
            │   ├── Column (Title)
            │   │   ├── Text (Greeting)
            │   │   └── Text (Subtitle)
            │   └── Surface (Profile Button)
            │       └── Icon
            │
            └── Column (Content)
                ├── ModernSearchBar
                │   └── OutlinedTextField
                │       ├── Icon (Search)
                │       └── Text (Placeholder)
                │
                ├── Row (Header)
                │   ├── Text (Title)
                │   └── Text (Count)
                │
                └── LazyColumn (Classes)
                    └── items
                        └── ClassCard
                            └── Card
                                └── Column
                                    ├── Surface (Badge)
                                    │   └── Text (Subject ID)
                                    ├── Text (Subject Name)
                                    ├── Row (Teacher)
                                    │   ├── Icon
                                    │   └── Text
                                    ├── Row (Semester)
                                    │   ├── Icon
                                    │   └── Text
                                    ├── Row (Session)
                                    │   ├── Icon
                                    │   └── Text
                                    └── Surface (Credits Badge)
                                        └── Text
```

---

## 👤 Profile Screen Hierarchy

```
ComposeProfileFragment
└── ReaterTheme
    └── ProfileScreen
        └── Scaffold
            ├── TopAppBar
            │   ├── IconButton (Back)
            │   │   └── Icon
            │   └── Text (Title)
            │
            └── LazyColumn (Content)
                ├── item (Profile Header)
                │   └── Card
                │       └── Column
                │           ├── Surface (Avatar)
                │           │   └── Icon
                │           ├── Text (Name)
                │           └── Text (Enrollment ID)
                │
                ├── item (Section Header)
                │   └── Text (Personal Information)
                │
                ├── item (Email Card)
                │   └── ProfileInfoCard
                │       └── Card
                │           └── Row
                │               ├── Surface (Icon Container)
                │               │   └── Icon
                │               └── Column
                │                   ├── Text (Label)
                │                   └── Text (Value)
                │
                ├── item (Phone Card)
                │   └── ProfileInfoCard
                │
                ├── item (DOB Card)
                │   └── ProfileInfoCard
                │
                ├── item (Section Header)
                │   └── Text (Academic Information)
                │
                ├── item (Degree Card)
                │   └── ProfileInfoCard
                │
                ├── item (Class Card)
                │   └── ProfileInfoCard
                │
                └── item (Enrollment Year Card)
                    └── ProfileInfoCard
```

---

## 📝 Quiz Screen Hierarchy

```
ComposeQuizFragment
└── ReaterTheme
    └── QuizScreen
        └── Scaffold
            ├── TopAppBar
            │   ├── IconButton (Back)
            │   │   └── Icon
            │   └── Text (Title)
            │
            └── Column (Content)
                ├── ModernSearchBar
                │   └── OutlinedTextField
                │
                ├── TabRow
                │   ├── Tab (Upcoming)
                │   │   └── Text
                │   └── Tab (Past)
                │       └── Text
                │
                └── LazyColumn (Quizzes)
                    └── items
                        └── QuizCard
                            └── Card
                                └── Column
                                    ├── Row (Header)
                                    │   ├── Surface (Icon Container)
                                    │   │   └── Icon
                                    │   └── Column
                                    │       ├── Text (Quiz Name)
                                    │       └── Text (Syllabus)
                                    │
                                    ├── HorizontalDivider
                                    │
                                    ├── Row (Details)
                                    │   ├── Column (Schedule)
                                    │   │   ├── Row
                                    │   │   │   ├── Icon
                                    │   │   │   └── Text (Label)
                                    │   │   └── Text (Value)
                                    │   │
                                    │   └── Column (Duration)
                                    │       ├── Row
                                    │       │   ├── Icon
                                    │       │   └── Text (Label)
                                    │       └── Text (Value)
                                    │
                                    └── Surface (Questions Badge)
                                        └── Text
```

---

## 🎨 Theme Hierarchy

```
ReaterTheme
└── MaterialTheme
    ├── colorScheme
    │   ├── primary (#6750A4)
    │   ├── onPrimary (#FFFFFF)
    │   ├── primaryContainer (#EADDFF)
    │   ├── onPrimaryContainer (#21005D)
    │   ├── secondary (#625B71)
    │   ├── onSecondary (#FFFFFF)
    │   ├── secondaryContainer (#E8DEF8)
    │   ├── onSecondaryContainer (#1D192B)
    │   ├── tertiary (#7D5260)
    │   ├── onTertiary (#FFFFFF)
    │   ├── tertiaryContainer (#FFD8E4)
    │   ├── onTertiaryContainer (#31111D)
    │   ├── background (#FFFBFE)
    │   ├── onBackground (#1C1B1F)
    │   ├── surface (#FFFBFE)
    │   ├── onSurface (#1C1B1F)
    │   ├── surfaceVariant (#E7E0EC)
    │   ├── onSurfaceVariant (#49454F)
    │   ├── outline (#79747E)
    │   └── outlineVariant (#CAC4D0)
    │
    ├── typography
    │   ├── displayLarge (57sp)
    │   ├── displayMedium (45sp)
    │   ├── displaySmall (36sp)
    │   ├── headlineLarge (32sp)
    │   ├── headlineMedium (28sp)
    │   ├── headlineSmall (24sp)
    │   ├── titleLarge (22sp)
    │   ├── titleMedium (16sp)
    │   ├── titleSmall (14sp)
    │   ├── bodyLarge (16sp)
    │   ├── bodyMedium (14sp)
    │   ├── bodySmall (12sp)
    │   ├── labelLarge (14sp)
    │   ├── labelMedium (12sp)
    │   └── labelSmall (11sp)
    │
    └── shapes (default Material 3)
```

---

## 🔄 Data Flow Architecture

### MVVM Pattern

```
View Layer (Compose)
    ↓ observes
ViewModel Layer
    ↓ requests
Repository Layer
    ↓ fetches
Data Source (API/Database)
```

### Detailed Flow

```
Fragment
└── ComposeView
    └── setContent
        └── ReaterTheme
            └── Screen Composable
                ├── observeAsState() ← LiveData from ViewModel
                ├── remember { } ← Local UI state
                └── LaunchedEffect { } ← Side effects
                    └── ViewModel.fetchData()
                        └── Repository.getData()
                            └── API/Database
```

### State Management

```
ViewModel (Business Logic)
├── LiveData<NetworkResult<T>>
│   ├── Loading
│   ├── Success(data)
│   └── Error(message)
│
└── Exposed to Compose via observeAsState()

Composable (UI State)
├── remember { mutableStateOf() } ← Local state
├── derivedStateOf { } ← Computed state
└── LaunchedEffect { } ← Side effects
```

---

## 🧩 Component Relationships

### ClassCard Component

```
ClassCard (Reusable)
├── Input
│   ├── coursework: Coursework (data model)
│   ├── onClick: () -> Unit (callback)
│   └── modifier: Modifier (styling)
│
├── State
│   └── None (stateless component)
│
└── Output
    └── Clickable card with course information
```

### QuizCard Component

```
QuizCard (Reusable)
├── Input
│   ├── quiz: Quiz (data model)
│   ├── onClick: () -> Unit (callback)
│   └── modifier: Modifier (styling)
│
├── State
│   └── None (stateless component)
│
└── Output
    └── Clickable card with quiz information
```

### ModernSearchBar Component

```
ModernSearchBar (Reusable)
├── Input
│   ├── query: String (current search text)
│   ├── onQueryChange: (String) -> Unit (callback)
│   ├── placeholder: String (hint text)
│   └── modifier: Modifier (styling)
│
├── State
│   └── Managed by parent (hoisted state)
│
└── Output
    └── Text field with search icon
```

---

## 🎯 State Hoisting Pattern

### Example: Search Functionality

```
Parent (HomeScreen)
├── var searchQuery by remember { mutableStateOf("") }
├── val filteredCourses = remember(searchQuery, courses) {
│       courses.filter { it.name.contains(searchQuery) }
│   }
│
└── Child (ModernSearchBar)
    ├── query = searchQuery (read)
    └── onQueryChange = { searchQuery = it } (write)
```

### Benefits
- ✅ Single source of truth
- ✅ Easier to test
- ✅ Reusable components
- ✅ Clear data flow

---

## 🔌 Integration Patterns

### Pattern 1: Fragment with Sample Data

```
ComposeHomeFragment
└── onCreate()
    └── mainViewModel = ViewModelProvider(...)
└── onCreateView()
    └── ComposeView
        └── setContent
            └── ReaterTheme
                └── HomeScreen(
                    courses = sampleCourses,
                    onClassClick = { /* navigate */ },
                    onProfileClick = { /* navigate */ }
                )
```

### Pattern 2: Fragment with ViewModel

```
ComposeHomeFragmentWithViewModel
└── onCreate()
    └── mainViewModel = ViewModelProvider(...)
    └── requestAPIData()
└── onCreateView()
    └── ComposeView
        └── setContent
            └── ReaterTheme
                └── HomeScreenWithViewModel(
                    mainViewModel = mainViewModel,
                    onClassClick = { /* navigate */ },
                    onProfileClick = { /* navigate */ },
                    onError = { /* show toast */ }
                )
```

### Pattern 3: Pure Compose Activity

```
ComposeMainActivity
└── onCreate()
    └── setContent
        └── ReaterTheme
            └── Surface
                └── ReaterApp()
                    ├── var currentScreen by remember { ... }
                    └── when (currentScreen) {
                        is Screen.Home -> HomeScreen(...)
                        is Screen.Profile -> ProfileScreen(...)
                        is Screen.Quiz -> QuizScreen(...)
                    }
```

---

## 📊 Component Reusability Matrix

| Component | Used In | Reusable | Stateless |
|-----------|---------|----------|-----------|
| ClassCard | HomeScreen | ✅ Yes | ✅ Yes |
| QuizCard | QuizScreen | ✅ Yes | ✅ Yes |
| ModernSearchBar | HomeScreen, QuizScreen | ✅ Yes | ✅ Yes |
| ProfileInfoCard | ProfileScreen | ✅ Yes | ✅ Yes |
| LoadingScreen | All screens | ✅ Yes | ✅ Yes |
| ErrorScreen | All screens | ✅ Yes | ✅ Yes |

---

## 🎨 Styling Hierarchy

### Material Theme Application

```
Component Styling
├── MaterialTheme.colorScheme.* (colors)
├── MaterialTheme.typography.* (text styles)
├── MaterialTheme.shapes.* (shapes)
└── Custom modifiers
    ├── .padding()
    ├── .size()
    ├── .fillMaxWidth()
    └── .clip()
```

### Example: ClassCard Styling

```
ClassCard
└── Card
    ├── shape = RoundedCornerShape(16.dp)
    ├── colors = CardDefaults.cardColors(
    │       containerColor = MaterialTheme.colorScheme.surface
    │   )
    └── elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
```

---

## 🔄 Lifecycle & Composition

### Composition Lifecycle

```
Initial Composition
├── @Composable function called
├── remember { } creates state
├── LaunchedEffect { } runs side effects
└── UI rendered

Recomposition (when state changes)
├── State change detected
├── Affected composables recompose
├── remember { } preserves state
└── UI updated

Disposal
├── Composable leaves composition
├── DisposableEffect cleanup runs
└── Resources released
```

### State Preservation

```
remember { }
├── Survives recomposition
└── Lost on configuration change

rememberSaveable { }
├── Survives recomposition
└── Survives configuration change

ViewModel
├── Survives recomposition
├── Survives configuration change
└── Cleared when Activity/Fragment destroyed
```

---

## 🎯 Best Practices Applied

### 1. Single Responsibility
Each component has one clear purpose:
- ClassCard: Display course information
- QuizCard: Display quiz information
- SearchBar: Handle search input

### 2. State Hoisting
State is managed at the appropriate level:
- Search query: Parent screen
- Loading state: ViewModel
- UI state: Local remember

### 3. Composition over Inheritance
Components are composed, not inherited:
- Cards use Column, Row, Text
- Screens use Scaffold, LazyColumn
- No class hierarchies

### 4. Unidirectional Data Flow
Data flows down, events flow up:
- Data: ViewModel → Screen → Component
- Events: Component → Screen → ViewModel

### 5. Immutability
Data classes are immutable:
- Coursework, Quiz, Student are data classes
- State changes create new instances
- No mutable properties

---

## 📚 Summary

The component hierarchy follows these principles:

1. **Layered Architecture**: Theme → Screen → Component → Fragment
2. **Reusability**: Components are modular and reusable
3. **State Management**: Clear state hoisting patterns
4. **Data Flow**: Unidirectional from ViewModel to UI
5. **Composition**: Components composed, not inherited
6. **Immutability**: Data classes are immutable
7. **Single Responsibility**: Each component has one purpose

This architecture ensures:
- ✅ Maintainable code
- ✅ Testable components
- ✅ Reusable UI elements
- ✅ Clear data flow
- ✅ Scalable structure

---

**Document Version**: 1.0.0  
**Last Updated**: April 2026  
**Status**: Complete ✅
