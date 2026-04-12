---
name: Refactoring Skill
description: Use this skill to perform behavior-preserving refactors in this Kotlin Multiplatform desktop Compose project.
---

# Refactoring Skill

## When to use
- Reducing duplication in `composeApp/src/desktopMain/kotlin/edu/dyds/movies`.
- Improving naming, function size, or control flow clarity without changing behavior.
- Extracting small reusable composable or view-model helpers while keeping responsibilities separated.
- Reorganizing code to align with existing architecture boundaries.

## Project constraints
- Keep route contracts in `composeApp/src/desktopMain/kotlin/edu/dyds/movies/Navigation.kt` unchanged (`home`, `detail/{movieId}`, `NavType.IntType`).
- Keep load triggers in `HomeScreen` and `DetailScreen` (`LaunchedEffect(Unit)` with `MoviesViewModel` calls).
- Keep state orchestration in `MoviesViewModel` and rendering/action triggers in screen composables.
- Keep API/domain mapping in `composeApp/src/desktopMain/kotlin/edu/dyds/movies/Movie.kt` (`RemoteMovie.toDomainMovie`).
- Keep Compose naming style used by this project (`@file:Suppress("FunctionName")` and PascalCase composables).
- Reuse `LoadingIndicator(...)` and `NoResults { retry }` for shared loading/empty states when applicable.
- Keep code-level identifiers and docs in English.

## Refactoring playbook
1. Identify the smallest safe refactor unit.
2. Preserve behavior first; avoid mixing refactors with feature changes.
3. Keep each commit a **cohesive conceptual unit**: big enough to represent a complete change (e.g., extract a composable + update all call sites), but narrow enough that it doesn't mix unrelated refactoring goals (e.g., don't combine a rename with an extraction in the same commit).
4. Verify call sites and state flow boundaries after each extraction.
5. Run desktop tests after code changes and report what was executed.
6. **Ask the user before creating a commit** with a clear message describing the single refactoring intent (e.g., `refactor: extract MovieCard composable`, `refactor: rename loadMovies to getAllMovies`).

## Do
- Keep functions small and cohesive.
- Prefer explicit names over comments.
- Remove duplication through extraction when it improves readability.
- Preserve fallback-oriented error behavior unless explicitly requested otherwise.
- **Group related changes into single commits**: each commit should be conceptually cohesive and represent one refactoring intent, including all necessary updates (e.g., if extracting a composable, include all imports and call-site updates in the same commit).
- **Avoid mixing unrelated refactoring goals** in a single commit (e.g., separate a rename from an extraction even if they touch similar code).
- **Ask the user before creating a commit**, providing a suggested message that reflects the single conceptual change.

## Do not
- Change API behavior, route formats, or model contracts as part of a refactor-only request.
- Move network responsibilities into UI composables.
- Introduce new architecture patterns that conflict with current project style.
- Add non-English code or documentation.

## Expected output format
- List changed files with a short reason per file.
- State behavior invariants that were intentionally preserved.
- Mention verification steps and any residual risk.


