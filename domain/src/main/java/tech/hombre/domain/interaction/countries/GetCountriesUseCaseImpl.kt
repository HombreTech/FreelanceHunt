package tech.hombre.domain.interaction.countries

import tech.hombre.domain.repository.CountriesRepository

class GetCountriesUseCaseImpl(private val countriesRepository: CountriesRepository) :
    GetCountriesUseCase {

    override suspend operator fun invoke(param: String) = countriesRepository.getCountries()
}