package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.bottom_menu_freelancers_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.data.database.dao.SkillsDao
import tech.hombre.domain.interaction.cities.GetCitiesUseCase
import tech.hombre.domain.model.*
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.EXTRA_3
import tech.hombre.freelancehunt.common.extensions.launch
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment

class FreelancersFilterBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_freelancers_filter

    private val viewModel: FreelancersFilterViewModel by viewModel()

    private var listener: OnSubmitFreelancersFilter? = null

    private var skills = listOf<SkillList.Data>()

    private var countries = listOf<Countries.Data>()

    private var cities = listOf<Cities.Data>()

    var checkedSkills = booleanArrayOf()

    var checkedCity = -1

    var checkedCountry = -1

    var savedSkills = intArrayOf()

    override fun viewReady() {
        arguments?.let {

            savedSkills = it.getIntArray(EXTRA_1) ?: intArrayOf()
            checkedCountry = it.getInt(EXTRA_2)
            checkedCity = it.getInt(EXTRA_3)

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

                        updateSpinnerView(true, false, false)
                    }
                setPositiveButton("OK") { dialog, which -> }
                show()
            }
        }

        countriesList.setOnClickListener {
            with(
                AlertDialog.Builder(
                    requireContext(),
                    AppHelper.getDialogTheme(appPreferences.getAppTheme())
                )
            ) {
                setTitle(getString(R.string.select_country))
                    .setSingleChoiceItems(
                        countries.map { it.name }.toTypedArray(),
                        countries.indexOf(countries.find { it.id == checkedCountry })
                    ) { dialog, which ->
                        checkedCountry = countries[which].id
                        checkedCity = -1
                        updateSpinnerView(false, true, false)
                        dialog.dismiss()
                    }
                setNegativeButton(getString(R.string.reset)) { dialog, which ->
                    checkedCountry = -1
                    checkedCity = -1
                    updateSpinnerView(false, true, false)
                }
                show()
            }
        }

        citiesList.setOnClickListener {
            with(
                AlertDialog.Builder(
                    requireContext(),
                    AppHelper.getDialogTheme(appPreferences.getAppTheme())
                )
            ) {
                setTitle(getString(R.string.select_city))
                    .setSingleChoiceItems(
                        cities.map { it.name }.toTypedArray(),
                        cities.indexOf(cities.find { it.id == checkedCity })
                    ) { dialog, which ->
                        checkedCity = cities[which].id
                        updateSpinnerView(false, false, true)
                        dialog.dismiss()
                    }
                setNegativeButton(getString(R.string.reset)) { dialog, which ->
                    checkedCity = -1
                    updateSpinnerView(false, false, true)
                }
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
                checked.toIntArray(),
                if (checkedCountry > 0) checkedCountry else 0,
                if (checkedCity > 0) checkedCity else 0
            )
            dismiss()
        }

        citiesList.isEnabled = checkedCountry != -1

    }

    private fun subscribeToData() {
        viewModel.skills.subscribe(this, {
            skills = it
            checkedSkills = skills.map { false }.toBooleanArray()
            savedSkills.forEach { savedSill ->
                checkedSkills[skills.indexOf(skills.find { it.id == savedSill })] = true
            }
            updateSpinnerView(true, false, false)
        })
        viewModel.countries.subscribe(this, {
            countries = it
            updateSpinnerView(false, true, false)
        })
        viewModel.cities.subscribe(this, {
            cities = it
            updateSpinnerView(false, false, true)
        })
        viewModel.setSkills()
        viewModel.setCountries()
    }


    private fun updateSpinnerView(isSkills: Boolean, isCountries: Boolean, isCities: Boolean) {
        if (isSkills) {
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
        } else if (isCountries) {
            if (checkedCountry > 0) {
                countriesList.text = countries.find { it.id == checkedCountry }?.name ?: getString(R.string.select)
                viewModel.setCities(checkedCountry)
                citiesList.text = getString(R.string.select)
                citiesList.isEnabled = true
            } else {
                countriesList.text = getString(R.string.select)
                citiesList.text = getString(R.string.select)
                citiesList.isEnabled = false
            }
        } else if (isCities) {
            if (checkedCity > 0) citiesList.text = cities.find { it.id == checkedCity }?.name ?: getString(R.string.select) else {
                citiesList.text = getString(R.string.select)
                citiesList.isEnabled = true
            }
        }
    }

    interface OnSubmitFreelancersFilter {
        fun onSubmitProjectFilter(
            skills: IntArray,
            countryId: Int,
            cityId: Int
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnSubmitFreelancersFilter) {
            listener = parentFragment as OnSubmitFreelancersFilter
        } else if (context is OnSubmitFreelancersFilter) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @Keep
        val TAG = FreelancersFilterBottomDialogFragment::class.java.simpleName

        fun newInstance(
            skills: IntArray,
            countryId: Int,
            cityId: Int
        ): FreelancersFilterBottomDialogFragment {
            val fragment = FreelancersFilterBottomDialogFragment()
            val extra = Bundle()
            extra.putIntArray(EXTRA_1, skills)
            extra.putInt(EXTRA_2, countryId)
            extra.putInt(EXTRA_3, cityId)
            fragment.arguments = extra
            return fragment
        }
    }

    class FreelancersFilterViewModel(
        private val skillsDao: SkillsDao,
        private val countriesDao: CountriesDao,
        private val getCities: GetCitiesUseCase
    ) : ViewModel() {

        val _skills = MutableLiveData<List<SkillList.Data>>()
        val skills: LiveData<List<SkillList.Data>>
            get() = _skills

        val _countries = MutableLiveData<List<Countries.Data>>()
        val countries: LiveData<List<Countries.Data>>
            get() = _countries

        val _cities = MutableLiveData<List<Cities.Data>>()
        val cities: LiveData<List<Cities.Data>>
            get() = _cities

        fun setSkills() = launch {
            _skills.value = skillsDao.getSkills().skills.data
        }

        fun setCountries() = launch {
            _countries.value = countriesDao.getCountries().countries.data
        }

        fun setCities(countryId: Int) = launch {
            getCities(countryId)
                .onSuccess {
                    _cities.value = it.data
                }
                .onFailure {}
        }

    }
}