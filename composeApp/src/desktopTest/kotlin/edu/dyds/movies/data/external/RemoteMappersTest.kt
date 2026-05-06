package edu.dyds.movies.data.external

import edu.dyds.movies.fakes.remoteMovie
import edu.dyds.movies.fakes.remoteResult
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RemoteMappersTest {

    @Test
    fun `toDomainMovie should map urls and nullable backdrop`() = runTest {
        // arrange
        val remote = remoteMovie(id = 1, posterPath = "/poster.png", backdropPath = null)

        // act
        val result = remote.toDomainMovie()

        // assert
        assertEquals(1, result.id)
        assertEquals("https://image.tmdb.org/t/p/w185/poster.png", result.poster)
        assertNull(result.backdrop)
    }

    @Test
    fun `toDomainMovie should map backdrop url when present`() = runTest {
        // arrange
        val remote = remoteMovie(id = 1, backdropPath = "/backdrop.png")

        // act
        val result = remote.toDomainMovie()

        // assert
        assertEquals("https://image.tmdb.org/t/p/w780/backdrop.png", result.backdrop)
    }

    @Test
    fun `toDomainMovieList should map all items`() = runTest {
        // arrange
        val remote = listOf(remoteMovie(id = 1), remoteMovie(id = 2))
        val result = remoteResult(results = remote)

        // act
        val mapped = result.toDomainMovieList()

        // assert
        assertEquals(listOf(1, 2), mapped.map { it.id })
    }
}
