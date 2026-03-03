# Android Skill

## Architecture
This app uses Single Activity architecture. `MainActivity` hosts a `NavHostFragment`.
All screens are Fragments, never additional Activities.

- **MVVM**: Fragment → ViewModel → Repository → Room DAO
- **ViewBinding** only (no DataBinding)
- **Coroutines** for async database operations
- **LiveData** for observing data in Fragments
- **Navigation Component** for screen transitions (`res/navigation/nav_graph.xml`)

## New screen
1. Create a layout XML in `app/src/main/res/layout/` (e.g. `fragment_my_screen.xml`)
2. Create a Fragment in `app/src/main/kotlin/com/example/learningspace/ui/`
   - Use `by viewModels()` delegate
   - Use ViewBinding: inflate in `onCreateView`, access views in `onViewCreated`
   - Release binding in `onDestroyView`
3. Create a ViewModel in `app/src/main/kotlin/com/example/learningspace/viewmodel/`
   - Extend `ViewModel`
   - Use `viewModelScope.launch` for coroutines
4. Register the fragment in `app/src/main/res/navigation/nav_graph.xml`
   - Add a `<fragment>` entry with `android:name`, `android:label`, and `tools:layout`
   - Declare any `<argument>` elements with `app:argType`
5. Add `<action>` entries in the source fragment's nav_graph entry to navigate to the new screen
6. No changes to `AndroidManifest.xml` for new screens (Single Activity — only `MainActivity` is registered)

## New string
Always add to `app/src/main/res/values/strings.xml`. Never hardcode strings in layouts or Kotlin code.

## New data entity
1. Create a `@Entity` data class in `app/src/main/kotlin/com/example/learningspace/data/`
2. Add a `@Dao` interface (or extend an existing one) with the required `@Query`/`@Insert`/`@Update`/`@Delete` methods
3. Create or update the Repository class to expose LiveData or suspend functions
4. Register the new entity in `FlashCardDatabase.kt`: add it to the `entities` list and **increment the database version**
5. Provide a `Migration` or use `fallbackToDestructiveMigration()` during development

## Build verification
Always run `./gradlew assembleDebug` before committing.
Fix all errors and warnings before opening a PR.
