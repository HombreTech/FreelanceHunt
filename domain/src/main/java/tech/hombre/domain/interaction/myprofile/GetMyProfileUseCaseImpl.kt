package tech.hombre.domain.interaction.myprofile

import tech.hombre.domain.repository.MyProfileRepository

class GetMyProfileUseCaseImpl(private val myprofileRepository: MyProfileRepository) :
    GetMyProfileUseCase {

    override suspend operator fun invoke(param: String) = myprofileRepository.getMyProfile()
}