# Jetpack Compose Quick Start Guide

## 🚀 Getting Started in 5 Minutes

### Step 1: Sync Project
```bash
./gradlew clean build
```

### Step 2: Preview Screens
Open any of these files in Android Studio to see live previews:
- `app/src/main/java/com/example/reater/ui/compose/previews/ScreenPreviews.kt`

Click the "Split" or "Design" button in Android Studio to see the preview panel.

### Step 3: Run the App

#### Option A: Using Existing Navigation (Recommended)
Replace your existing fragments in the navigation graph:

```xml
<!-- navigation/nav_graph.xml -->
<fragment
    android:id="@+id/homeFragment"
    android:name="com.example.reater.ui.compose.fragments.ComposeHomeFragment"
    android:label="Home"
    tools:layout="@layout/fragment_home" />

<fragment
    android:id="@+id/userProfileFragment"
    android:name="com.example.reater.ui.compose.fragments.ComposeProfileFragment"
    android:label="Profile"
    tools:layout="@layout/fragment_user_profile" />

<fragment
    android:id="@+id/quizFragment"
    android:name="com.example.reater.ui.compose.fragments.ComposeQuizFragment"
    android:label="Quiz"
    tools:layout="@layout/fragment_quiz" />
```

#### Option B: Pure Compose Activity
Add to `AndroidManifest.xml`:

```xml
<activity
    android:name=".ui.compose.ComposeMainActivity"
    android:exported="true"
    android:theme="@style/Theme.Reater">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

## 📱 What You Get

### Home Screen
- ✅ Modern search bar
- ✅ Class cards with all course information
- ✅ Profile button in top bar
- ✅ Empty state handling

### Profile Screen
- ✅ Centered profile avatar
- ✅ Personal information cards
- ✅ Academic information cards
- ✅ Clean, organized layout

### Quiz Screen
- ✅ Upcoming/Past tabs
- ✅ Search functionality
- ✅ Quiz cards with details
- ✅ Empty states for each tab

## 🎨 Customization

### Change Colors
Edit `app/src/main/java/com/example/reater/ui/compose/theme/Color.kt`:

```kotlin
val Primary = Color(0xFF6750A4)  // Change to your brand color
val Background = Color(0xFFFFFBFE)  // Change background
```

### Change Typography
Edit `app/src/main/java/com/example/reater/ui/compose/theme/Type.kt`:

```kotlin
val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = YourCustomFont,  // Add custom font
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    )
)
```

### Modify Components
All reusable components are in:
- `app/src/main/java/com/example/reater/ui/compose/components/`

Example - Change card corner radius:
```kotlin
// In ClassCard.kt
Card(
    shape = RoundedCornerShape(20.dp),  // Change from 16.dp to 20.dp
    // ...
)
```

## 🔌 Connect to Your Data

### Using ViewModel in Compose Fragment

```kotlin
@AndroidEntryPoint
class ComposeHomeFragment : Fragment() {
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ReaterTheme {
                    // Observe ViewModel data
                    val coursesState by mainViewModel.StudentClassResponse
                        .observeAsState()

                    HomeScreen(
                        courses = when (coursesState) {
                            is NetworkResult.Success -> 
                                coursesState.data ?: emptyList()
                            else -> emptyList()
                        },
                        studentName = "Student",
                        onClassClick = { /* Navigate */ },
                        onProfileClick = { /* Navigate */ }
                    )
                }
            }
        }
    }
}
```

### Loading States

```kotlin
when (val state = coursesState) {
    is NetworkResult.Loading -> {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    is NetworkResult.Success -> {
        HomeScreen(courses = state.data ?: emptyList(), ...)
    }
    is NetworkResult.Error -> {
        ErrorScreen(message = state.message)
    }
}
```

## 🧪 Testing

### Preview in Android Studio
1. Open any file in `previews/ScreenPreviews.kt`
2. Click "Split" view
3. See live preview of all screens

### Run on Device
1. Connect device or start emulator
2. Click Run (Shift+F10)
3. Navigate to the Compose screens

## 📋 Checklist

- [ ] Gradle sync completed successfully
- [ ] Previews visible in Android Studio
- [ ] App builds without errors
- [ ] Home screen displays with sample data
- [ ] Profile screen shows student information
- [ ] Quiz screen shows quiz list with tabs
- [ ] Search functionality works
- [ ] Navigation between screens works
- [ ] All existing data fields preserved

## 🐛 Common Issues

### Issue: "Cannot resolve symbol 'compose'"
**Fix**: Sync Gradle and invalidate caches (File > Invalidate Caches > Restart)

### Issue: Preview not showing
**Fix**: Make sure you're using `@Preview` annotation and the function is `@Composable`

### Issue: Colors not applying
**Fix**: Wrap your content in `ReaterTheme { }` and use `MaterialTheme.colorScheme`

### Issue: Navigation not working
**Fix**: Ensure you're using `findNavController()` from Fragment, not from Composable

## 📚 Next Steps

1. **Connect Real Data**: Replace sample data with ViewModel data
2. **Add Navigation**: Implement Navigation Compose or use existing Navigation Component
3. **Add Loading States**: Show progress indicators while loading
4. **Add Error Handling**: Display error messages appropriately
5. **Add Animations**: Implement transitions and animations
6. **Add Dark Theme**: Create dark color scheme
7. **Add More Screens**: Convert remaining screens to Compose

## 🎯 Key Files Reference

| File | Purpose |
|------|---------|
| `theme/Theme.kt` | Main theme configuration |
| `theme/Color.kt` | Color definitions |
| `theme/Type.kt` | Typography definitions |
| `components/ClassCard.kt` | Reusable class card |
| `components/QuizCard.kt` | Reusable quiz card |
| `components/SearchBar.kt` | Reusable search bar |
| `screens/HomeScreen.kt` | Home screen composable |
| `screens/ProfileScreen.kt` | Profile screen composable |
| `screens/QuizScreen.kt` | Quiz screen composable |
| `fragments/ComposeHomeFragment.kt` | Home fragment wrapper |
| `fragments/ComposeProfileFragment.kt` | Profile fragment wrapper |
| `fragments/ComposeQuizFragment.kt` | Quiz fragment wrapper |
| `previews/ScreenPreviews.kt` | Preview functions |

## 💡 Tips

1. **Use Previews**: Always create preview functions for your composables
2. **Keep Composables Small**: Break down large composables into smaller ones
3. **Hoist State**: Keep state in the appropriate level of the hierarchy
4. **Use remember**: Cache computed values with `remember`
5. **Use keys**: Always provide keys in `LazyColumn` items
6. **Follow Material 3**: Use Material 3 components and guidelines
7. **Test on Device**: Always test on real device, not just previews

## 🆘 Need Help?

- Check `COMPOSE_MIGRATION_GUIDE.md` for detailed documentation
- Review preview functions in `previews/ScreenPreviews.kt`
- Look at sample implementation in `ComposeMainActivity.kt`
- Refer to [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)

---

**Ready to go!** 🎉 Your modern, minimalistic UI is ready to use.
