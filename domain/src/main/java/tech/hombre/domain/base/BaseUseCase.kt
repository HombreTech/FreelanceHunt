package tech.hombre.domain.base

import tech.hombre.domain.model.Result

interface BaseUseCase<T : Any, R : Any> {
    suspend operator fun invoke(param: T): Result<R>
}