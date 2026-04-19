import edu.dyds.movies.data.MoviesRepositoryImpl
import edu.dyds.movies.data.external.RemoteMovie
import edu.dyds.movies.data.external.RemoteMoviesDataSource
import edu.dyds.movies.data.external.RemoteResult
import edu.dyds.movies.data.local.LocalMoviesDataSource
import edu.dyds.movies.data.strategy.error.MovieDetailsErrorStrategy
import edu.dyds.movies.data.strategy.error.PopularMoviesErrorStrategy
import edu.dyds.movies.domain.entity.Movie
import java.io.IOException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class MoviesRepositoryImplTest {

    @Test
    fun `getPopularMovies uses cache when available`() = runTest {
        val cachedMovies = listOf(movie(id = 10))
        val localDataSource = FakeLocalMoviesDataSource(cachedMovies)
        val remoteDataSource = FakeRemoteMoviesDataSource(
            popularMoviesProvider = {
                error("Remote should not be called when cache has data")
            }
        )
        val repository = createRepository(remoteDataSource, localDataSource)

        val result = repository.getPopularMovies()

        assertEquals(cachedMovies, result)
        assertEquals(0, remoteDataSource.getPopularMoviesCalls)
    }

    @Test
    fun `getPopularMovies returns empty list for recoverable exceptions`() = runTest {
        val localDataSource = FakeLocalMoviesDataSource(emptyList())
        val remoteDataSource = FakeRemoteMoviesDataSource(
            popularMoviesProvider = { throw SerializationException("invalid payload") }
        )
        val repository = createRepository(remoteDataSource, localDataSource)

        val result = repository.getPopularMovies()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `getPopularMovies rethrows non recoverable exceptions`() = runTest {
        val localDataSource = FakeLocalMoviesDataSource(emptyList())
        val remoteDataSource = FakeRemoteMoviesDataSource(
            popularMoviesProvider = { throw IllegalStateException("unexpected bug") }
        )
        val repository = createRepository(remoteDataSource, localDataSource)

        assertFailsWith<IllegalStateException> {
            repository.getPopularMovies()
        }
    }

    @Test
    fun `getMovieDetails returns null for recoverable exceptions`() = runTest {
        val remoteDataSource = FakeRemoteMoviesDataSource(
            movieDetailsProvider = { throw IOException("network down") }
        )
        val repository = createRepository(remoteDataSource, FakeLocalMoviesDataSource(emptyList()))

        val result = repository.getMovieDetails(id = 1)

        assertNull(result)
    }

    @Test
    fun `getMovieDetails rethrows non recoverable exceptions`() = runTest {
        val remoteDataSource = FakeRemoteMoviesDataSource(
            movieDetailsProvider = { throw IllegalArgumentException("invalid input") }
        )
        val repository = createRepository(remoteDataSource, FakeLocalMoviesDataSource(emptyList()))

        assertFailsWith<IllegalArgumentException> {
            repository.getMovieDetails(id = 1)
        }
    }

    private fun createRepository(
        remoteDataSource: RemoteMoviesDataSource,
        localDataSource: LocalMoviesDataSource
    ): MoviesRepositoryImpl {
        return MoviesRepositoryImpl(
            remoteMoviesDataSource = remoteDataSource,
            localMoviesDataSource = localDataSource,
            popularMoviesErrorStrategy = PopularMoviesErrorStrategy,
            movieDetailsErrorStrategy = MovieDetailsErrorStrategy
        )
    }

    private class FakeRemoteMoviesDataSource(
        private val popularMoviesProvider: suspend () -> RemoteResult = {
            FakeRemoteResult(listOf(movie(id = 1)))
        },
        private val movieDetailsProvider: suspend () -> RemoteMovie = {
            FakeRemoteMovie(movie(id = 1))
        }
    ) : RemoteMoviesDataSource {
        var getPopularMoviesCalls = 0

        override suspend fun getPopularMovies(): RemoteResult {
            getPopularMoviesCalls += 1
            return popularMoviesProvider()
        }

        override suspend fun getMovieDetails(id: Int): RemoteMovie {
            return movieDetailsProvider()
        }
    }

    private class FakeLocalMoviesDataSource(
        initialMovies: List<Movie>
    ) : LocalMoviesDataSource {
        private val cache = initialMovies.toMutableList()

        override fun getPopularMovies(): List<Movie> {
            return cache.toList()
        }

        override fun savePopularMovies(movies: List<Movie>) {
            cache.clear()
            cache.addAll(movies)
        }
    }

    private class FakeRemoteResult(
        private val movies: List<Movie>
    ) : RemoteResult {
        override fun toDomainMovieList(): List<Movie> {
            return movies
        }
    }

    private class FakeRemoteMovie(
        private val movie: Movie
    ) : RemoteMovie {
        override fun toDomainMovie(): Movie {
            return movie
        }
    }
}

private fun movie(id: Int): Movie {
    return Movie(
        id = id,
        title = "Title $id",
        overview = "Overview $id",
        releaseDate = "2026-01-01",
        poster = "poster-$id",
        backdrop = "backdrop-$id",
        originalTitle = "Original $id",
        originalLanguage = "en",
        popularity = 1.0,
        voteAverage = 7.0
    )
}

