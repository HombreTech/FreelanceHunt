package tech.hombre.data.repository

import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.hombre.data.common.coroutine.CoroutineContextProvider
import tech.hombre.data.common.utils.Connectivity
import tech.hombre.data.database.DB_ENTRY_ERROR
import tech.hombre.data.networking.CONNECTION_ERROR
import tech.hombre.data.networking.NOT_HAVE_INTERNET_CONNECTION
import tech.hombre.data.networking.TIMEOUT_ERROR
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseRepository<T : Any, R : DomainMapper<T>> : KoinComponent {
    private val connectivity: Connectivity by inject()
    private val contextProvider: CoroutineContextProvider by inject()

    /**
     * Cached API request
     */
    protected suspend fun fetchData(
        apiDataProvider: suspend () -> Result<T>,
        dbDataProvider: suspend () -> R,
        fromCache: Boolean = false
    ): Result<T> {
        try {
            return if (connectivity.hasNetworkAccess() && !fromCache) {
                withContext(contextProvider.io) {
                    apiDataProvider()
                }
            } else {
                withContext(contextProvider.io) {
                    val dbResult = dbDataProvider()
                    if (dbResult != null) Success(dbResult.mapToDomainModel()) else if (fromCache) {
                        if (connectivity.hasNetworkAccess()) apiDataProvider() else Failure(
                            HttpError(
                                Throwable(NOT_HAVE_INTERNET_CONNECTION)
                            )
                        )
                    } else {
                        Failure(
                            HttpError(
                                Throwable(DB_ENTRY_ERROR)
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            val error = when (e) {
                is SocketTimeoutException -> TIMEOUT_ERROR
                is UnknownHostException -> CONNECTION_ERROR
                else -> CONNECTION_ERROR
            }
            return Failure(
                HttpError(
                    Throwable(error)
                )
            )
        }
    }

    /**
     * API request
     */
    protected suspend fun fetchData(dataProvider: suspend () -> Result<T>): Result<T> {
        return try {
            if (connectivity.hasNetworkAccess()) {
                withContext(contextProvider.io) {
                    dataProvider()
                }
            } else {
                Failure(HttpError(Throwable(NOT_HAVE_INTERNET_CONNECTION)))
            }
        } catch (e: Exception) {
            val error = when (e) {
                is SocketTimeoutException -> TIMEOUT_ERROR
                is UnknownHostException -> CONNECTION_ERROR
                else -> CONNECTION_ERROR
            }
            return Failure(
                HttpError(
                    Throwable(error)
                )
            )
        }
    }
}