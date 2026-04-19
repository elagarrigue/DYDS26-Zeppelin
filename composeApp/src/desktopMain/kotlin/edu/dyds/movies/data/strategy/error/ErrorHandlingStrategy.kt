package edu.dyds.movies.data.strategy.error

interface ErrorHandlingStrategy<T> {
    suspend fun run(operation: suspend () -> T): T
}

