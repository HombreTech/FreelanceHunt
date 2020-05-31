package tech.hombre.freelancehunt.common.extensions

fun getIconIdBySkillId(id: Int): Int {
    return when (id) {
        12 -> 2
        29 -> 2
        7 -> 2
        22 -> 4
        20 -> 4
        24 -> 3
        else -> 1
    }
}

fun currencyToChar(currency: String): String {
    return when (currency) {
        "UAH" -> "₴"
        "RUB" -> "₽"
        else -> currency
    }
}