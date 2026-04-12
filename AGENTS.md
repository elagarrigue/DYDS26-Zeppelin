# AGENTS Guide

## Scope and Intent
- This repo is a single-module Kotlin Multiplatform project focused on a desktop Compose app (`:composeApp`).
- Runtime entrypoint is `composeApp/src/desktopMain/kotlin/edu/dyds/movies/main.kt`, which opens `App()` and delegates all UI flow to `Navigation()`.

## Architecture You Need First
- The project follows a Clean MVVM Architecture, structured by feature/layer: `presentation`, `domain`, `data`, and `di`.
- UI flow is route-based: `home` and `detail/{movieId}` in `composeApp/src/desktopMain/kotlin/edu/dyds/movies/presentation/navigation/Navigation.kt`.
- `HomeRoute` triggers data load in `LaunchedEffect(Unit)` via `MoviesViewModel.getAllMovies()`.
- `DetailRoute` triggers detail load in `LaunchedEffect(id)` via `MoviesViewModel.getMovieDetail(id)`.
- Network + state logic is centralized in specific layers:
  - **Presentation**: `MoviesViewModel` (`moviesStateFlow` and `movieDetailStateFlow`) orchestrates UI states using UseCases; screens only render/trigger actions.
  - **Domain**: Encapsulates business rules. UseCases like `GetPopularMoviesUseCase` handle filtering/sorting (e.g. `MIN_VOTE_AVERAGE = 6.0`). Models like `Movie` live here.
    - **Data**: The repository `MoviesRepositoryImpl` orchestrates `data/local` and `data/external`, mapping Ktor responses to domain entities and delegating in-memory cache concerns to `MoviesLocalDataSource`.
- Dependency wiring is manual through `MoviesDependencyInjector.getMoviesViewModel()` connecting Repository, UseCases, and the ViewModel.

## Data and Integration Boundaries
- TMDB access uses Ktor `HttpClient` with `DefaultRequest` in `MoviesDependencyInjector.kt`.
- API host and auth query parameter are injected globally (`api.themoviedb.org`, `api_key`).
- API models (`RemoteMovie`, `RemoteResult`) live in `data/external/RemoteModels.kt` mapping explicitly to `domain/entity/Movie.kt`.
- Image rendering uses Coil 3 (`AsyncImage`) and TMDB image URLs built in mapper (`w185` poster, `w780` backdrop).
- Resource strings come from `composeApp/src/commonMain/composeResources/values/strings.xml`; generated `Res.string.*` is used in UI.

## Build, Run, Test Workflows
- Prefer wrapper commands from repo root:
```bash
./gradlew composeApp:desktopRun
./gradlew composeApp:desktopTest
./gradlew composeApp:build
./gradlew composeApp:packageDistributionForCurrentOS
```
- Useful desktop packaging targets are enabled (`Dmg`, `Msi`, `Deb`) in `composeApp/build.gradle.kts`.
- `mainClass` is explicitly pinned to `edu.dyds.movies.MainKt` in both `compose.desktop.application` and `JavaExec` task config.

## Project-Specific Coding Patterns
- Keep composable naming style as-is (`@file:Suppress("FunctionName")` + PascalCase composables in screen files).
- Reuse shared UI states/components: `LoadingIndicator(...)` and `NoResults { retry }` from `CommonComposables.kt`.
- Home list behavior is opinionated: movies are sorted by vote average and classified as "good" using `MIN_VOTE_AVERAGE = 6.0`.
- "Bad" movies are still shown but dimmed and open a desktop `DialogWindow` with `images/too_bad.png`.
- Error handling is intentionally fallback-oriented: network failures return empty list/null (no exception bubbling).

## Coding Rules for Agent Changes
- Avoid adding source-code comments; prefer self-explanatory names, small functions, and clear control flow.
- Follow clean-code practices in each edit: keep functions focused, remove duplication, and keep UI/state/network responsibilities separated.
- Apply SOLID in the current architecture style: screens render and trigger actions, `MoviesViewModel` coordinates state, and data mapping stays in model/mapper code.
- Write all new code in English (identifiers, function names, class names, and code-level text), even if the user conversation is in another language.

## Change Guidance for Agents
- Preserve route constants/arguments in `Navigation.kt`; deep links rely on `detail/{movieId}` + `NavType.IntType`.
- If changing API/domain fields, update both `RemoteMovie` serialization annotations and `toDomainMovie()` mapping.
- If moving strings, keep `Res.string.*` usage consistent with Compose resources plugin generation.
- Treat the current in-code API key in `MoviesDependencyInjector.kt` as sensitive; prefer environment/Gradle property injection for new work.
- Existing tests are examples only (`composeApp/src/desktopTest/kotlin/TestExample.kt`); add feature-specific tests near changed behavior.
