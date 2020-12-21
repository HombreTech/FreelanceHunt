package tech.hombre.freelancehunt.ui.menu

import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.bottom_menu_project_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.data.database.dao.SkillsDao
import tech.hombre.domain.model.SkillList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.EXTRA_3
import tech.hombre.freelancehunt.common.extensions.launch
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment

class ProjectFilterBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_project_filter

    private val viewModel: ProjectFilterViewModel by viewModel()

    private var listener: OnSubmitProjectFilter? = null

    private var onlyMySkills = false
    private var onlyForPlus = false

    private var skills = listOf<SkillList.Data>()

    var checkedSkills = booleanArrayOf()

    var savedSkills = intArrayOf()

    override fun viewReady() {
        arguments?.let {

            onlyMySkills = it.getBoolean(EXTRA_1)
            savedSkills = it.getIntArray(EXTRA_2) ?: intArrayOf()
            onlyForPlus = it.getBoolean(EXTRA_3)

            subscribeToData()
            initViews()
            return
        }
        error(getString(R.string.init_error))
    }

    private fun initViews() {

        isForPlus.isChecked = onlyForPlus
        isOnlyMySkills.isChecked = onlyMySkills

        isOnlyMySkills.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && checkedSkills.contains(true)) {
                checkedSkills = BooleanArray(skills.size)
                updateSkillsView()
            }
        }

        skillsList.setOnClickListener {
            with(
                AlertDialog.Builder(
                    requireContext(),
                    AppHelper.getDialogTheme(appPreferences.getAppTheme())
                )
            ) {
                setTitle(getString(R.string.select_category))
                    .setMultiChoiceItems(
                        skills.map { it.name }.toTypedArray(),
                        checkedSkills
                    ) { dialog, which, isChecked ->
                        checkedSkills[which] = isChecked

                        updateSkillsView()
                    }
                setPositiveButton("OK") { dialog, which -> }
                show()
            }
        }

        buttonSubmit.setOnClickListener {
            val checked = arrayListOf<Int>()
            checkedSkills.forEachIndexed { index, b ->
                if (b) {
                    checked.add(skills[index].id)
                }
            }
            listener?.onSubmitProjectFilter(
                isOnlyMySkills.isChecked,
                checked.toIntArray(),
                isForPlus.isChecked
            )
            dismiss()
        }
    }

    private fun subscribeToData() {
        viewModel.skills.subscribe(this, {
            skills = it
            checkedSkills = BooleanArray(skills.size)
            savedSkills.forEach { savedSill ->
                checkedSkills[skills.indexOf(skills.find { it.id == savedSill })] = true
            }
            updateSkillsView()
        })
        viewModel.setSkills()
    }


    private fun updateSkillsView() {
        val checkedSize = checkedSkills.filter { it }.size
        if (checkedSize > 0) {
            var placeholder = ""
            checkedSkills.forEachIndexed { index, skill ->
                if (skill) {
                    placeholder += skills[index].name + ","
                }
            }
            skillsList.text =
                placeholder.removeRange(placeholder.length - 1, placeholder.length)
            if (isOnlyMySkills.isChecked) isOnlyMySkills.isChecked = false
        } else {
            skillsList.text = getString(R.string.select)
        }
    }

    interface OnSubmitProjectFilter {
        fun onSubmitProjectFilter(
            onlyMySkills: Boolean,
            skills: IntArray,
            onlyForPlus: Boolean
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnSubmitProjectFilter) {
            listener = parentFragment as OnSubmitProjectFilter
        } else if (context is OnSubmitProjectFilter) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @Keep
        val TAG = ProjectFilterBottomDialogFragment::class.java.simpleName

        fun newInstance(
            onlyMySkills: Boolean,
            skills: IntArray,
            onlyForPlus: Boolean
        ): ProjectFilterBottomDialogFragment {
            val fragment = ProjectFilterBottomDialogFragment()
            val extra = Bundle()
            extra.putBoolean(EXTRA_1, onlyMySkills)
            extra.putIntArray(EXTRA_2, skills)
            extra.putBoolean(EXTRA_3, onlyForPlus)
            fragment.arguments = extra
            return fragment
        }
    }

    class ProjectFilterViewModel(
        private val skillsDao: SkillsDao
    ) : ViewModel() {

        val _skills = MutableLiveData<List<SkillList.Data>>()
        val skills: LiveData<List<SkillList.Data>>
            get() = _skills

        fun setSkills() = launch {
            _skills.value = skillsDao.getSkills().skills.data
        }

    }
}