package tech.hombre.freelancehunt.ui.project.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import kotlinx.android.synthetic.main.activity_new_job.*
import kotlinx.android.synthetic.main.appbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.domain.model.MyBidsList
import tech.hombre.domain.model.ProjectDetail
import tech.hombre.domain.model.SkillList
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.CurrencyType
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.SafeType
import tech.hombre.freelancehunt.common.extensions.gone
import tech.hombre.freelancehunt.common.extensions.snackbar
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.common.extensions.visible
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.ui.base.*
import tech.hombre.freelancehunt.ui.project.presentation.NewProjectViewModel
import java.text.SimpleDateFormat
import java.util.*


class NewProjectActivity : BaseActivity() {

    private val viewModel: NewProjectViewModel by viewModel()

    private var skills = listOf<SkillList.Data>()

    var checkedSkills = booleanArrayOf()

    private var freelancerId = 0

    private var isPersonal = false

    private var endDate = ""

    override fun viewReady() {
        setContentView(R.layout.activity_new_job)
        setSupportActionBar(toolbar)
        setTitle(R.string.new_project)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent?.extras?.let {
            subscribeToData()
            isPersonal = it.getBoolean(EXTRA_1, false)
            freelancerId = it.getInt(EXTRA_2, 0)
            initViews()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_new_project, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_open -> openUrl(this, "https://freelancehunt.com/project/add")
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initViews() {
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

        if (!isPersonal) {
            plusView.visible()
            isForPlus.isEnabled = appPreferences.getCurrentUserProfile()?.is_plus_active ?: false
        } else plusView.gone()

        val calendar: Calendar = Calendar.getInstance()
        val yy: Int = calendar.get(Calendar.YEAR)
        val mm: Int = calendar.get(Calendar.MONTH) + 1
        val dd: Int = calendar.get(Calendar.DAY_OF_MONTH)
        endDateButton.text = String.format("%s/%s/%s", if (dd > 9) dd else "0$dd", if (mm > 9) mm else "0$mm", yy)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        endDate = format.format(calendar.time)

        buttonSubmit.setOnClickListener {
            if (correctInputs()) {
                val currency = CurrencyType.values()[budgetType.selectedItemPosition]
                val budget = MyBidsList.Data.Attributes.Budget(
                    budgetValue.text.toString(),
                    currency.currency
                )
                val safe = SafeType.values()[safeType.selectedItemPosition]
                val checkedSkill = arrayListOf<Int>()
                checkedSkills.forEachIndexed { index, skill ->
                    if (skill) {
                        checkedSkill.add(skills[index].id)
                    }
                }

                if (isPersonal)
                    viewModel.addNewPersonalProject(
                        projectTitle.text.toString(),
                        freelancerId,
                        isPersonal,
                        budget,
                        safe.type ?: "employer",
                        description.savedText.toString(),
                        checkedSkill,
                        endDate
                    ) else
                    viewModel.addNewProject(
                        projectTitle.text.toString(),
                        budget,
                        safe.type ?: "employer",
                        description.savedText.toString(),
                        checkedSkill,
                        endDate,
                        isForPlus.isChecked
                    )

            } else {
                showError(getString(R.string.check_inputs))
            }
        }
    }

    private fun correctInputs(): Boolean {
        return endDate.isNotEmpty() && !projectTitle.text.isNullOrEmpty() && description.savedText.isNotEmpty() && checkedSkills.any { it }
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
            hideLoading(progressBar)
        })
        viewModel.setSkills()
    }

    private fun handleViewState(viewState: ViewState<ProjectDetail>) {
        when (viewState) {
            is Loading -> showLoading(progressBar)
            is Success -> {
                hideLoading(progressBar)
                finish()
                appNavigator.showProjectDetails(viewState.data.data)
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

        fun startActivity(context: Context, isPersonal: Int, freelancerId: Int) {
            val intent = Intent(context, NewProjectActivity::class.java)
            intent.putExtra(EXTRA_1, isPersonal)
            intent.putExtra(EXTRA_2, freelancerId)
            context.startActivity(intent)
        }
    }


}