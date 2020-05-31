package tech.hombre.freelancehunt.ui.project.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class ProjectDetailViewModel(
    private val getProjectDetail: GetProjectDetailUseCase,
    private val countriesDao: CountriesDao
) :
    BaseViewModel<ProjectDetail>() {

    val _countries = MutableLiveData<List<Countries.Data>>()
    val countries: LiveData<List<Countries.Data>>
        get() = _countries

    fun getProjectDetails(url: String) = executeUseCase {
        getProjectDetail(url)
            .onSuccess { _viewState.value = Success(it) }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun setCountries() = executeUseCase {
        _countries.value = countriesDao.getCountries().countries.data
    }
}
