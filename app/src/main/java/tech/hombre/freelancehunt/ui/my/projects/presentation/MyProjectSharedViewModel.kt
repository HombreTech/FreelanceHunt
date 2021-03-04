package tech.hombre.freelancehunt.ui.my.projects.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.component.KoinComponent
import tech.hombre.domain.model.ProjectDetail


class MyProjectSharedViewModel(
) :
    ViewModel(), KoinComponent {

    val _action = MutableLiveData<Pair<Int, ProjectDetail>>()
    val action: LiveData<Pair<Int, ProjectDetail>>
        get() = _action

    fun sendAction(actions: Pair<Int, ProjectDetail>) {
        _action.value = actions
    }

}