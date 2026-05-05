DYDS - Proyecto

## Objetivo
App de escritorio con Kotlin Multiplatform y Compose Desktop para explorar peliculas usando TMDB.

## Arquitectura
- Clean MVVM por capas: presentation, domain, data, di.
- Navegacion por rutas: home y detail/{movieId}.
- Entrada de ejecucion: `composeApp/src/desktopMain/kotlin/edu/dyds/movies/main.kt`.

## Ejecutar y testear
Desde la raiz del repo:

```bash
./gradlew composeApp:desktopRun
./gradlew composeApp:desktopTest
./gradlew composeApp:build
./gradlew composeApp:packageDistributionForCurrentOS
```

## Datos y API
- La integracion con TMDB esta en `composeApp/src/desktopMain/kotlin/edu/dyds/movies/data/external/RemoteMoviesDataSourceImpl.kt`.
- El API key hoy vive en codigo; es recomendable moverlo a una propiedad de Gradle o variable de entorno.

## Recursos
- Strings: `composeApp/src/commonMain/composeResources/values/strings.xml`.
- Imagenes: `composeApp/src/desktopMain/resources/images`.
