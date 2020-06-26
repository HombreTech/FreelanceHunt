package tech.hombre.data.networking.model

import tech.hombre.domain.model.ReviewGrades

data class ReviewBody(
    val grades: ReviewGrades,
    val review: String
)