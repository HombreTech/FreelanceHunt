package tech.hombre.freelancehunt.ui.main.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

class MainPublicViewModel : ViewModel(), KoinComponent {

    val feedBadgeCounter = MutableLiveData<Int>()

    val messagesCounter = MutableLiveData<Boolean>()

    fun setFeedBadgeCounter(count: Int) {
        feedBadgeCounter.value = count
    }

    fun setMessagesCounter(newMessages: Boolean) {
        messagesCounter.value = newMessages
    }
}