package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.bottom_menu_contests_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.data.database.dao.SkillsDao
import tech.hombre.domain.model.SkillList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.extensions.launch
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment

class ContestsFilterBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_contests_filter

    private val viewModel: ContestsFilterViewModel by viewModel()

    private var listener: OnSubmitContestsFilter? = null

    private var skills = listOf<SkillList.Data>()

    var checkedSkills = booleanArrayOf()

    var savedSkills = intArrayOf()

    override fun viewReady() {
        arguments?.let {
            savedSkills = it.getIntArray(EXTRA_1) ?: intArrayOf()
            subscribeToData()
            initViews()
            return
        }
        error(getString(R.string.init_error))
    }

    private fun initViews() {

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

                        updateSpinnerView()
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
            listener?.onSubmitContestsFilter(
                checked.toIntArray()
            )
            dismiss()
        }

    }

    private fun subscribeToData() {
        viewModel.skills.subscribe(this, {
            skills = it
            checkedSkills = skills.map { false }.toBooleanArray()
            savedSkills.forEach { savedSill ->
                checkedSkills[skills.indexOf(skills.find { it.id == savedSill })] = true
            }
            updateSpinnerView()
        })
        viewModel.setSkills()
    }


    private fun updateSpinnerView() {
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
        } else {
            skillsList.text = getString(R.string.select)
        }
    }

    interface OnSubmitContestsFilter {
        fun onSubmitContestsFilter(
            skills: IntArray
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnSubmitContestsFilter) {
            listener = parentFragment as OnSubmitContestsFilter
        } else if (context is OnSubmitContestsFilter) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @Keep
        val TAG = ContestsFilterBottomDialogFragment::class.java.simpleName

        fun newInstance(
            skills: IntArray
        ): ContestsFilterBottomDialogFragment {
            val fragment = ContestsFilterBottomDialogFragment()
            val extra = Bundle()
            extra.putIntArray(EXTRA_1, skills)
            fragment.arguments = extra
            return fragment
        }
    }

    class ContestsFilterViewModel(
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