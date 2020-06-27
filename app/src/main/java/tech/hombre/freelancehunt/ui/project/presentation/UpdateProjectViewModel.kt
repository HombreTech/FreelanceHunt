package tech.hombre.freelancehunt.ui.project.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.hombre.data.database.dao.SkillsDao
import tech.hombre.domain.interaction.projectslist.AmendProjectUseCase
import tech.hombre.domain.interaction.projectslist.UpdateProjectUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.ui.base.BaseViewModel
import tech.hombre.freelancehunt.ui.base.Error
import tech.hombre.freelancehunt.ui.base.Success

class UpdateProjectViewModel(
    private val skillsDao: SkillsDao,
    private val updateProject: UpdateProjectUseCase,
    private val amendProject: AmendProjectUseCase
) :
    BaseViewModel<ProjectDetail>() {

    val _skills = MutableLiveData<List<SkillList.Data>>()
    val skills: LiveData<List<SkillList.Data>>
        get() = _skills

    fun setSkills() = executeUseCase {
        _skills.value = skillsDao.getSkills().skills.data
    }

    fun updateProjects(
        projectId: Int,
        name: String,
        budget: MyBidsList.Data.Attributes.Budget,
        safeType: String,
        descriptionHtml: String,
        skills: ArrayList<Int>,
        expiredAt: String
    ) = executeUseCase {
        updateProject(projectId, name, budget, safeType, descriptionHtml, skills, expiredAt)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }

    fun amendProjects(
        projectId: Int,
        budget: MyBidsList.Data.Attributes.Budget,
        updateHtml: String,
        skills: ArrayList<Int>
    ) = executeUseCase {
        amendProject(projectId, budget, updateHtml, skills)
            .onSuccess {
                _viewState.value = Success(it)
            }
            .onFailure { _viewState.value = Error(it.throwable) }
    }


}
