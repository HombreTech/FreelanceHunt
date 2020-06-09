package tech.hombre.freelancehunt.framework.notifications

import tech.hombre.freelancehunt.routing.ScreenType


interface NotificationService {
    fun notify(msg : SimpleNotification)
}

data class SimpleNotification(
    val title : String,
    val messages : List<String>,
    val screenType: ScreenType,
    val count: Int,
    val id: Int
)