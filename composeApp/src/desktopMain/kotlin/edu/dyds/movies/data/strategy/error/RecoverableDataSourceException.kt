package edu.dyds.movies.data.strategy.error

import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import java.io.IOException
import kotlinx.serialization.SerializationException

fun Throwable.isRecoverableDataSourceException(): Boolean {
    return this is IOException ||
        this is ResponseException ||
        this is HttpRequestTimeoutException ||
        this is SerializationException
}

