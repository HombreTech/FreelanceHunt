package tech.hombre.freelancehunt.ui.employers.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

class EmployerPublicViewModel : ViewModel(), KoinComponent {

    val badgeCounter = MutableLiveData<Pair<Int, Int>>()

    fun updateBadge(tab: Int, count: Int) {
        badgeCounter.value = Pair(tab, count)
    }

}
