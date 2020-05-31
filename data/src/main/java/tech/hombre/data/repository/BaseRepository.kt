package tech.hombre.data.repository

import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import tech.hombre.data.common.coroutine.CoroutineContextProvider
import tech.hombre.data.common.utils.Connectivity
import tech.hombre.data.database.DB_ENTRY_ERROR
import tech.hombre.data.networking.GENERAL_NETWORK_ERROR
import tech.hombre.data.networking.base.DomainMapper
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Success

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
                            Throwable(GENERAL_NETWORK_ERROR)
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
    }

    /**
     * API request
     */
    protected suspend fun fetchData(dataProvider: suspend () -> Result<T>): Result<T> {
        return if (connectivity.hasNetworkAccess()) {
            withContext(contextProvider.io) {
                dataProvider()
            }
        } else {
            Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
        }
    }
}