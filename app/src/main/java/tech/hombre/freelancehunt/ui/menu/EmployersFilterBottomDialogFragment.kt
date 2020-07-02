package tech.hombre.freelancehunt.ui.menu


import android.content.Context
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.android.synthetic.main.bottom_menu_employers_filter.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import tech.hombre.data.database.dao.CountriesDao
import tech.hombre.domain.interaction.cities.GetCitiesUseCase
import tech.hombre.domain.model.Cities
import tech.hombre.domain.model.Countries
import tech.hombre.domain.model.onFailure
import tech.hombre.domain.model.onSuccess
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.EXTRA_1
import tech.hombre.freelancehunt.common.EXTRA_2
import tech.hombre.freelancehunt.common.extensions.launch
import tech.hombre.freelancehunt.common.extensions.subscribe
import tech.hombre.freelancehunt.framework.app.AppHelper
import tech.hombre.freelancehunt.ui.base.BaseBottomDialogFragment

class EmployersFilterBottomDialogFragment : BaseBottomDialogFragment() {

    override fun getLayout() = R.layout.bottom_menu_employers_filter

    private val viewModel: FreelancersFilterViewModel by viewModel()

    private var listener: OnSubmitEmployersFilter? = null

    private var countries = listOf<Countries.Data>()

    private var cities = listOf<Cities.Data>()

    var checkedCity = -1

    var checkedCountry = -1

    override fun viewReady() {
        arguments?.let {

            checkedCountry = it.getInt(EXTRA_1)
            checkedCity = it.getInt(EXTRA_2)

            subscribeToData()
            initViews()
            return
        }
        error(getString(R.string.init_error))
    }

    private fun initViews() {

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
                        updateSpinnerView(true, false)
                        dialog.dismiss()
                    }
                setNegativeButton(getString(R.string.reset)) { dialog, which ->
                    checkedCountry = -1
                    checkedCity = -1
                    updateSpinnerView(true, false)
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
                        updateSpinnerView(false, true)
                        dialog.dismiss()
                    }
                setNegativeButton(getString(R.string.reset)) { dialog, which ->
                    checkedCity = -1
                    updateSpinnerView(false, true)
                }
                show()
            }
        }

        buttonSubmit.setOnClickListener {
            listener?.onSubmitEmployersFilter(
                if (checkedCountry > 0) checkedCountry else 0,
                if (checkedCity > 0) checkedCity else 0
            )
            dismiss()
        }

        citiesList.isEnabled = checkedCountry != -1

    }

    private fun subscribeToData() {
        viewModel.countries.subscribe(this, {
            countries = it
            updateSpinnerView(true, false)
        })
        viewModel.cities.subscribe(this, {
            cities = it
            updateSpinnerView(false, true)
        })
        viewModel.setCountries()
    }


    private fun updateSpinnerView(isCountries: Boolean, isCities: Boolean) {
        if (isCountries) {
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

    interface OnSubmitEmployersFilter {
        fun onSubmitEmployersFilter(
            countryId: Int,
            cityId: Int
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment != null && parentFragment is OnSubmitEmployersFilter) {
            listener = parentFragment as OnSubmitEmployersFilter
        } else if (context is OnSubmitEmployersFilter) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @Keep
        val TAG = EmployersFilterBottomDialogFragment::class.java.simpleName

        fun newInstance(
            countryId: Int,
            cityId: Int
        ): EmployersFilterBottomDialogFragment {
            val fragment = EmployersFilterBottomDialogFragment()
            val extra = Bundle()
            extra.putInt(EXTRA_1, countryId)
            extra.putInt(EXTRA_2, cityId)
            fragment.arguments = extra
            return fragment
        }
    }

    class FreelancersFilterViewModel(
        private val countriesDao: CountriesDao,
        private val getCities: GetCitiesUseCase
    ) : ViewModel() {

        val _countries = MutableLiveData<List<Countries.Data>>()
        val countries: LiveData<List<Countries.Data>>
            get() = _countries

        val _cities = MutableLiveData<List<Cities.Data>>()
        val cities: LiveData<List<Cities.Data>>
            get() = _cities

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