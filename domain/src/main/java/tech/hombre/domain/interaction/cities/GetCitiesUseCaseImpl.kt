package tech.hombre.domain.interaction.cities

import tech.hombre.domain.repository.CitiesRepository

class GetCitiesUseCaseImpl(private val citiesRepository: CitiesRepository) :
    GetCitiesUseCase {

    override suspend operator fun invoke(countryId: Int) = citiesRepository.getCities(countryId)
}