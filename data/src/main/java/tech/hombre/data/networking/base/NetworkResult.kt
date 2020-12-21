package tech.hombre.data.networking.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import tech.hombre.data.common.extensions.getApiError
import tech.hombre.data.database.DB_ENTRY_ERROR
import tech.hombre.data.networking.GENERAL_NETWORK_ERROR
import tech.hombre.domain.model.Failure
import tech.hombre.domain.model.HttpError
import tech.hombre.domain.model.Result
import tech.hombre.domain.model.Success
import java.io.IOException



interface DomainMapper<T : Any> {
    fun mapToDomainModel(): T
}

interface RoomMapper<out T : Any> {
    fun mapToRoomEntity(): T
}

inline fun <T : Any> Response<T>.onSuccess(action: (T) -> Unit): Response<T> {
    if (isSuccessful) body()?.run(action)
    return this
}

inline fun <T : Any> Response<T>.onFailure(action: (HttpError) -> Unit) {
    if (!isSuccessful) errorBody()?.run {
        action(HttpError(Throwable(message()), code()))
    }
}

/**
 * Cached API request
 */

inline fun <T : RoomMapper<R>, R : DomainMapper<U>, U : Any> Response<T>.getData(
    cacheAction: (R) -> Unit,
    fetchFromCacheAction: () -> R
): Result<U> {
    try {
        onSuccess {
            val databaseEntity = it.mapToRoomEntity()
            cacheAction(databaseEntity)
            return Success(databaseEntity.mapToDomainModel())
        }
        onFailure {
            val cachedModel = fetchFromCacheAction()
            if (cachedModel != null) Success(cachedModel.mapToDomainModel()) else {
                Failure(HttpError(Throwable(DB_ENTRY_ERROR)))
            }
        }
        val error = this.errorBody()?.string()?.getApiError()
        return if (error != null)
            Failure(HttpError(Throwable(error.error.title)))
        else
            Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
    } catch (e: IOException) {
        return Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
    }
}

/**
 * API request
 */
fun <T : DomainMapper<R>, R : Any> Response<T>.getData(): Result<R> {
    try {
        onSuccess { return Success(it.mapToDomainModel()) }
        onFailure {
            val error = this.errorBody()?.string()?.getApiError()
            return if (error != null) {
                if (error.error.meta != null) {
                    val result = StringBuilder()
                    error.error.meta?.let {
                        if (it.info.description_html.isNotEmpty()) {
                            result.append(it.info.description_html.joinToString (separator = "\n") { it })
                        }
                        if (it.info.amount.isNotEmpty()) {
                            if (result.isNotEmpty()) result.append("\n")
                            result.append(it.info.amount.joinToString (separator = "\n") { it })
                        }
                        if (it.info.comment.isNotEmpty()) {
                            if (result.isNotEmpty()) result.append("\n")
                            result.append(it.info.comment.joinToString (separator = "\n") { it })
                        }
                        if (it.info.expired_at.isNotEmpty()) {
                            if (result.isNotEmpty()) result.append("\n")
                            result.append(it.info.expired_at.joinToString (separator = "\n") { it })
                        }
                    }
                    Failure(HttpError(Throwable(result.toString())))
                } else {
                    Failure(HttpError(Throwable(if (error.error.detail.isNullOrEmpty()) error.error.title else error.error.detail)))
                }
            }
            else
                Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
        }
        return Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
    } catch (e: IOException) {
        return Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
    }
}

suspend fun Response<Unit>.query(): Result<Boolean> {
    val query = this
    return if (query.isSuccessful) {
        Success(true)
    } else {
        withContext(Dispatchers.IO) {
            val error = query.errorBody()?.string()?.getApiError()
            if (error != null)
                Failure(HttpError(Throwable(if (error.error.detail.isNullOrEmpty()) error.error.title else error.error.detail)))
            else
                Failure(HttpError(Throwable(GENERAL_NETWORK_ERROR)))
        }
    }
}
