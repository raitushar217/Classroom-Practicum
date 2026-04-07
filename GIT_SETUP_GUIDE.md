# Git Setup Guide - Push to Your GitHub

## Step 1: Remove Old Git Repository (if exists)

Open PowerShell or Command Prompt in your project folder and run:

```bash
# For PowerShell
Remove-Item -Recurse -Force .git

# OR for Command Prompt
rmdir /s /q .git
```

If you get "File Not Found", that's fine - it means there's no .git folder.

---

## Step 2: Initialize New Git Repository

```bash
git init
```

This creates a fresh new Git repository in your project.

---

## Step 3: Add All Files

```bash
git add .
```

This stages all your files for commit.

---

## Step 4: Create First Commit

```bash
git commit -m "Initial commit - Premium UI redesign with Jetpack Compose"
```

---

## Step 5: Create Repository on GitHub

1. Go to https://github.com
2. Click the "+" icon in top right
3. Select "New repository"
4. Name it (e.g., "KojoClass-Premium-UI")
5. Don't initialize with README (we already have files)
6. Click "Create repository"

---

## Step 6: Connect to GitHub

After creating the repository, GitHub will show you commands. Use these:

```bash
# Add your GitHub repository as remote
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# Push your code
git branch -M main
git push -u origin main
```

Replace `YOUR_USERNAME` and `YOUR_REPO_NAME` with your actual GitHub username and repository name.

---

## Step 7: Share with Your Friend

Once pushed, share the repository URL with your friend:
```
https://github.com/YOUR_USERNAME/YOUR_REPO_NAME
```

Your friend can then clone it:
```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
```

---

## Complete Command Sequence

Here's the complete sequence to copy-paste:

```bash
# 1. Remove old git (if exists)
rmdir /s /q .git

# 2. Initialize new repository
git init

# 3. Add all files
git add .

# 4. Create first commit
git commit -m "Initial commit - Premium UI redesign with Jetpack Compose"

# 5. Add remote (replace with your GitHub URL)
git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git

# 6. Push to GitHub
git branch -M main
git push -u origin main
```

---

## Important Files to Check Before Pushing

### 1. Remove Sensitive Information

Check if these files contain sensitive data:
- `local.properties` - Contains local SDK paths (already in .gitignore)
- `google-services.json` - Contains Firebase keys
- Any API keys or passwords

### 2. Update .gitignore

Your `.gitignore` should include:
```
*.iml
.gradle
/local.properties
/.idea/
.DS_Store
/build
/captures
.externalNativeBuild
.cxx
local.properties
google-services.json
```

---

## Optional: Add a Nice README

Create a `README.md` with project description:

```markdown
# KojoClass - Premium Education App

A modern Android education app with premium UI design using Jetpack Compose.

## Features
- 📚 Course Management
- 📝 Quizzes and Assignments
- 📢 Announcements
- 📁 Study Materials
- 👤 Student Profiles

## Tech Stack
- Kotlin
- Jetpack Compose
- Material 3 Design
- MVVM Architecture
- Firebase
- Retrofit

## Screenshots
[Add screenshots here]

## Setup
1. Clone the repository
2. Open in Android Studio
3. Add your `google-services.json` file
4. Build and run

## UI Design
This app features a premium UI redesign with:
- Modern gradient headers
- Beautiful card designs
- Smooth animations
- Consistent Material 3 theming
```

---

## Troubleshooting

### "Permission denied" error
If you get permission errors, you may need to authenticate with GitHub:
```bash
# Use GitHub CLI
gh auth login

# OR use SSH instead of HTTPS
git remote set-url origin git@github.com:YOUR_USERNAME/YOUR_REPO_NAME.git
```

### "Repository not found" error
Make sure:
1. The repository exists on GitHub
2. You have the correct URL
3. You're logged in to the correct GitHub account

### Large files error
If you have large files (>100MB), you may need to use Git LFS:
```bash
git lfs install
git lfs track "*.apk"
git lfs track "*.aab"
```

---

## After Pushing

Your friend can now:

1. **Clone the repository:**
   ```bash
   git clone https://github.com/YOUR_USERNAME/YOUR_REPO_NAME.git
   cd YOUR_REPO_NAME
   ```

2. **Open in Android Studio:**
   - File → Open → Select the cloned folder

3. **Build the project:**
   ```bash
   ./gradlew build
   ```

4. **Run on device/emulator:**
   - Click the "Run" button in Android Studio

---

## Keeping Repository Updated

When you make changes:

```bash
# Check what changed
git status

# Add changes
git add .

# Commit changes
git commit -m "Description of changes"

# Push to GitHub
git push
```

Your friend can get updates:
```bash
git pull
```

---

## Making Repository Private

If you want to keep it private:
1. Go to repository on GitHub
2. Click "Settings"
3. Scroll to "Danger Zone"
4. Click "Change visibility"
5. Select "Make private"

Then invite your friend:
1. Go to "Settings" → "Collaborators"
2. Click "Add people"
3. Enter your friend's GitHub username

---

## Summary

✅ Remove old .git folder
✅ Initialize new Git repository
✅ Add and commit all files
✅ Create repository on GitHub
✅ Connect local repo to GitHub
✅ Push code to GitHub
✅ Share repository URL with friend

That's it! Your project is now on GitHub and ready to share! 🎉
