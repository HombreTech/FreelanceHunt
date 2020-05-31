package tech.hombre.freelancehunt.ui.freelancers.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

class FreelancerPublicViewModel : ViewModel(), KoinComponent {

    val badgeCounter = MutableLiveData<Pair<Int, Int>>()

    fun updateBadge(tab: Int, count: Int) {
        badgeCounter.value = Pair(tab, count)
    }

}
