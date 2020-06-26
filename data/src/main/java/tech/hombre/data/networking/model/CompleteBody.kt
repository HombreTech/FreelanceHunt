package tech.hombre.data.networking.model

import tech.hombre.domain.model.CompleteGrades

data class CompleteBody(
    val grades: CompleteGrades,
    val review: String
)