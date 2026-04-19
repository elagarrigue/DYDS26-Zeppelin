package edu.dyds.movies.data.strategy.error

import edu.dyds.movies.domain.entity.Movie

object MovieDetailsErrorStrategy : ErrorHandlingStrategy<Movie?> {
    override suspend fun run(operation: suspend () -> Movie?): Movie? {
        return try {
            operation()
        } catch (exception: Exception) {
            if (exception.isRecoverableDataSourceException()) {
                null
            } else {
                throw exception
            }
        }
    }
}

