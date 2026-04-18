# AGENTS Guide

## Scope and Intent
- This repo is a single-module Kotlin Multiplatform project focused on a desktop Compose app (`:composeApp`).
- Runtime entrypoint is `composeApp/src/desktopMain/kotlin/edu/dyds/movies/main.kt`, which opens `App()` and delegates all UI flow to `Navigation()`.

## Architecture You Need First
- The project follows a Clean MVVM Architecture, structured by layer: `presentation`, `domain`, `data`, and `di`.
- UI flow is route-based: `home` and `detail/{movieId}` in `composeApp/src/desktopMain/kotlin/edu/dyds/movies/presentation/navigation/Navigation.kt`.
- `HomeRoute` triggers data load in `LaunchedEffect(Unit)` via `PopularMoviesViewModel.getAllMovies()`.
- `DetailRoute` triggers detail load in `LaunchedEffect(id)` via `MovieDetailsViewModel.getMovieDetail(id)`.
- Network + state logic is centralized in specific layers:
  - **Presentation**: `PopularMoviesViewModel` (`moviesStateFlow`) and `MovieDetailsViewModel` (`movieDetailStateFlow`) orchestrate UI states through UseCases; screens only render and emit actions.
  - **Domain**: Encapsulates business rules. `GetPopularMoviesUseCase` sorts by vote average and classifies movies using `minVoteAverage = 6.0`. `Movie` and `QualifiedMovie` live in `domain/entity/Movie.kt`.
  - **Data**: `MoviesRepositoryImpl` orchestrates `data/local` and `data/external`, maps remote responses to domain entities, and delegates cache responsibilities to `MoviesLocalDataSource`.
- Dependency wiring is manual through `MoviesDependencyInjector.getPopularMoviesViewModel()` and `MoviesDependencyInjector.getMovieDetailsViewModel()`.

## Data and Integration Boundaries
- TMDB access is implemented in `data/external/tmdb/TMDB.kt` using a Ktor `HttpClient` configured with `ContentNegotiation`, `DefaultRequest`, and `HttpTimeout`.
- API host and auth query parameter are injected globally (`api.themoviedb.org`, `api_key`).
- API contracts (`RemoteMovie`, `RemoteResult`, `MoviesRemoteDataSource`) live in `data/external` and concrete TMDB models (`TMDBMovie`, `TMDBResult`) live in `data/external/tmdb`, mapping explicitly to `domain/entity/Movie.kt`.
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
- Home list behavior is opinionated: movies are sorted by vote average and classified as "good" using `minVoteAverage = 6.0` in `GetPopularMoviesUseCase`.
- "Bad" movies are still shown but dimmed and open a desktop `DialogWindow` with `images/too_bad.png`.
- Error handling is intentionally fallback-oriented: network failures return empty list/null (no exception bubbling).

## Coding Rules for Agent Changes
- Avoid adding source-code comments; prefer self-explanatory names, small functions, and clear control flow.
- Follow clean-code practices in each edit: keep functions focused, remove duplication, and keep UI/state/network responsibilities separated.
- Apply SOLID in the current architecture style: screens render and trigger actions, ViewModels coordinate state, and data mapping stays in model/mapper code.
- Write all new code in English (identifiers, function names, class names, and code-level text), even if the user conversation is in another language.

## Change Guidance for Agents
- Preserve route constants/arguments in `Navigation.kt`; deep links rely on `detail/{movieId}` + `NavType.IntType`.
- If changing API/domain fields, update both `RemoteMovie` serialization annotations and `toDomainMovie()` mapping.
- If moving strings, keep `Res.string.*` usage consistent with Compose resources plugin generation.
- Treat the current in-code API key in `data/external/tmdb/TMDB.kt` as sensitive; prefer environment/Gradle property injection for new work.
- Existing tests are examples only (`composeApp/src/desktopTest/kotlin/TestExample.kt`); add feature-specific tests near changed behavior.
- For any refactoring request, use only `.github/skills/refactoring-senior/SKILL.md`.
