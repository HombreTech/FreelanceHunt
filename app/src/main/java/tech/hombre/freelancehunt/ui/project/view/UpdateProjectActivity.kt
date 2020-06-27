package tech.hombre.freelancehunt.ui.project.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_update_job.*
import kotlinx.android.synthetic.main.appbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.SkillList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.*
import tech.hombre.freelancehunt.common.extensions.gone
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.my.projects.presentation.MyProjectSharedViewModel
import tech.hombre.freelancehunt.ui.project.presentation.UpdateProjectViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateProjectActivity : BaseActivity() {

    private val viewModel: UpdateProjectViewModel by viewModel()

    private val sharedViewModel: MyProjectSharedViewModel by viewModel()

    private var skills = listOf<SkillList.Data>()

    var checkedSkills = booleanArrayOf()

    private var projectId = 0

    private var isAmend = false

    private var endDate = ""

    var projectDetails: ProjectDetail.Data? = null

    override fun viewReady() {
        setContentView(R.layout.activity_update_job)
        setSupportActionBar(toolbar)
        setTitle(R.string.update_project)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent?.extras?.let {
            projectId = it.getInt(EXTRA_1, 0)
            isAmend = it.getBoolean(EXTRA_2, false)
            projectDetails = it.getParcelable<ProjectDetail.Data>(EXTRA_3)
            subscribeToData()
            initViews(projectDetails)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_update_project, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_open -> openUrl(this, "https://freelancehunt.com/project/update/$projectId")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews(projectDetails: ProjectDetail.Data?) {
        if (projectDetails == null) {
            handleError(getString(R.string.project_init_error))
            return
        }
        val projectAttributes = projectDetails.attributes

        if (isAmend) {
            description.hint = getString(R.string.project_amend_description_hint)
            description.setText("")
            projectTitle.gone()
            safeView.gone()
            endDateView.gone()
        } else {
            projectTitle.setText(projectAttributes.name)
            description.setText(projectAttributes.description_html)
        }

        budgetValue.setText((projectAttributes.budget?.amount ?: "").toString())

        budgetType.setSelection(
            CurrencyType.values().find { it.currency == projectAttributes.budget?.currency }?.ordinal ?: 0
        )
        safeType.setSelection(
            SafeType.values().find { it.type == projectAttributes.safe_type }?.ordinal ?: 0
        )

        endDate = projectAttributes.expired_at

        description.setOnTouchListener { view, event ->
            view.parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> view.parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }

        skillsList.setOnClickListener {
            with(
                AlertDialog.Builder(
                    this,
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

                show()
            }
        }

        endDateButton.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()

            calendar.time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(endDate)

            val yy: Int = calendar.get(Calendar.YEAR)
            val mm: Int = calendar.get(Calendar.MONTH)
            val dd: Int = calendar.get(Calendar.DAY_OF_MONTH)

            val picker = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                    val calendar = Calendar.getInstance()
                    calendar.set(year, month, dayOfMonth)

                    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
                    endDate = format.format(calendar.time)

                    endDateButton.text = String.format(
                        "%s/%s/%s",
                        if (dayOfMonth > 9) dayOfMonth else "0$dayOfMonth",
                        if (month + 1 > 9) month + 1 else "0${month + 1}",
                        year
                    )

                },
                yy,
                mm,
                dd
            )
            picker.datePicker.minDate = System.currentTimeMillis() - 1000
            picker.show()
        }

        val calendar: Calendar = Calendar.getInstance()
        calendar.time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).parse(endDate)
        val yy: Int = calendar.get(Calendar.YEAR)
        val mm: Int = calendar.get(Calendar.MONTH) + 1
        val dd: Int = calendar.get(Calendar.DAY_OF_MONTH)

        endDateButton.text = String.format("%s/%s/%s", if (dd > 9) dd else "0$dd", if (mm > 9) mm else "0$mm", yy)

        buttonSubmit.setOnClickListener {
            if (correctInputs()) {
                val currency = CurrencyType.values()[budgetType.selectedItemPosition]
                val budget = MyBidsList.Data.Attributes.Budget(
                    budgetValue.text.toString().toInt(),
                    currency.currency
                )
                val safe = SafeType.values()[safeType.selectedItemPosition]
                val checkedSkill = arrayListOf<Int>()
                checkedSkills.forEachIndexed { index, skill ->
                    if (skill) {
                        checkedSkill.add(skills[index].id)
                    }
                }

                if (isAmend)
                    viewModel.amendProjects(
                        projectId,
                        budget,
                        description.savedText.toString(),
                        checkedSkill
                    ) else
                    viewModel.updateProjects(
                        projectId,
                        projectTitle.text.toString(),
                        budget,
                        safe.type ?: "employer",
                        description.savedText.toString(),
                        checkedSkill,
                        endDate
                    )

            } else {
                showError(getString(R.string.check_inputs))
            }
        }
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
        } else skillsList.text = getString(R.string.select)
    }

    private fun correctInputs(): Boolean {
        return description.savedText.isNotEmpty() && !budgetValue.text.isNullOrEmpty() && checkedSkills.any { it }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun subscribeToData() {
        viewModel.viewState.subscribe(this, ::handleViewState)
        viewModel.skills.subscribe(this, {
            skills = it
            checkedSkills = skills.map { false }.toBooleanArray()
            projectDetails?.let {
                skills.forEachIndexed { index, skill ->
                    val source = it.attributes.skills.any { it.id == skill.id }
                    if (source) {
                        checkedSkills[index] = true
                    }
                }
                updateSkillsView()
            }
            hideLoading(progressBar)
        })
        viewModel.setSkills()
    }

    private fun handleViewState(viewState: ViewState<ProjectDetail>) {
        when (viewState) {
            is Loading -> showLoading(progressBar)
            is Success -> {
                hideLoading(progressBar)
                sharedViewModel.sendAction(Pair(projectId, viewState.data))
                finish()
            }
            is Error -> {
                handleError(viewState.error.localizedMessage)
            }
            is NoInternetState -> showNoInternetError()
        }
    }

    private fun handleError(error: String) {
        hideLoading(progressBar)
        showError(error)
    }


    private fun showNoInternetError() {
        hideLoading(progressBar)
        snackbar(getString(R.string.no_internet_error_message), newProjectContainer)
    }

    override fun onBackPressed() {
        if (canExit()) super.onBackPressed()
    }

    companion object {

        fun startActivity(
            context: Context,
            projectId: Int,
            isAmend: Boolean,
            projectDetails: ProjectDetail.Data?
        ) {
            val intent = Intent(context, UpdateProjectActivity::class.java)
            intent.putExtra(EXTRA_1, projectId)
            intent.putExtra(EXTRA_2, isAmend)
            intent.putExtra(EXTRA_3, projectDetails)
            context.startActivity(intent)
        }
    }


}