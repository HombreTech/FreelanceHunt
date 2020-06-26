package tech.hombre.freelancehunt.ui.project.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.data.database.dao.SkillsDao
import tech.hombre.domain.interaction.projectslist.NewPersonalProjectUseCase
import tech.hombre.domain.interaction.projectslist.NewProjectUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class NewProjectViewModel(
    private val skillsDao: SkillsDao,
    private val newPublicProject: NewProjectUseCase,
    private val newPersonalProject: NewPersonalProjectUseCase
) :
    BaseViewModel<ProjectDetail>() {

    val _skills = MutableLiveData<List<SkillList.Data>>()
    val skills: LiveData<List<SkillList.Data>>
        get() = _skills

    fun setSkills() = executeUseCase {
        _skills.value = skillsDao.getSkills().skills.data
    }

    fun addNewProject(
        name: String,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expired_at: String,
        isForPlus: Boolean
    ) = executeUseCase {
        newPublicProject(
            name,
            budget,
            safeType,
            descriptionHtml,
            skills,
            expired_at,
            isForPlus
        )
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun addNewPersonalProject(
        name: String,
        freelancerId: Int,
        is_personal: Boolean,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expired_at: String
    ) = executeUseCase {
        newPersonalProject(
            name,
            freelancerId,
            is_personal,
            budget,
            safeType,
            descriptionHtml,
            skills,
            expired_at
        )
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

}
