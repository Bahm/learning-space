# Project Overview
Android app written in Kotlin. Minimum SDK 26, target SDK 34.

# Build
./gradlew assembleDebug

# Project Structure
- app/src/main/kotlin/com/example/learningspace/ — Kotlin source files
- app/src/main/res/ — layouts, drawables, strings
- app/src/main/AndroidManifest.xml

# Conventions
- ViewBinding only, no DataBinding
- Single Activity architecture
- No third-party libraries beyond Jetpack/AndroidX unless explicitly requested
- All strings in strings.xml, no hardcoded strings
- Navigation: Safe Args plugin (androidx.navigation.safeargs.kotlin); add actions to nav_graph.xml and use generated Directions classes
- Room migrations required when adding/changing entity fields; always use addMigrations() in the database builder, never fallbackToDestructiveMigration()
- Package name: com.example.learningspace
- No tests currently exist

# PR Instructions
Always create the PR yourself using: gh pr create --base main
Never give the user a link to create the PR manually.

**Note:** `gh pr create` requires `claude_args: "--allowedTools 'Bash(gh pr create:*)'"` in `.github/workflows/claude.yml` (see issue #13). Until the workflow is updated, provide a manual PR creation link as a fallback.