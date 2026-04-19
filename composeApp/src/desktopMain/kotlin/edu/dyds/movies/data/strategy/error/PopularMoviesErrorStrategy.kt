package edu.dyds.movies.data.strategy.error

import edu.dyds.movies.domain.entity.Movie

object PopularMoviesErrorStrategy : ErrorHandlingStrategy<List<Movie>> {
    override suspend fun run(operation: suspend () -> List<Movie>): List<Movie> {
        return try {
            operation()
        } catch (exception: Exception) {
            if (exception.isRecoverableDataSourceException()) {
                emptyList()
            } else {
                throw exception
            }
        }
    }
}

