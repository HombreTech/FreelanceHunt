package tech.hombre.domain.model


data class CompleteGrades(
    val quality: Int? = 0,
    val professionalism: Int? = 0,
    val cost: Int? = 0,
    val connectivity: Int? = 0,
    val schedule: Int? = 0
)

data class ReviewGrades(
    val pay: Int? = 0,
    val definition: Int? = 0,
    val requirements : Int? = 0,
    val connectivity : Int? = 0
)