package tech.hombre.freelancehunt.ui.contest.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

class ContestPublicViewModel : ViewModel(), KoinComponent {

    val badgeCounter = MutableLiveData<Pair<Int, Int>>()

    val tabViewsRefresher = MutableLiveData<Int>()

    fun updateBadge(tab: Int, count: Int) {
        badgeCounter.value = Pair(tab, count)
    }

    fun updateTabViews(tab: Int) {
        tabViewsRefresher.value = tab
    }

}
