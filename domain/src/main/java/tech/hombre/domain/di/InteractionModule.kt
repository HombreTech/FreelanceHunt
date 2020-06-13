package tech.hombre.domain.di

import org.koin.dsl.module
import tech.hombre.domain.interaction.contests.GetContestsListUseCase
import tech.hombre.domain.interaction.contests.GetContestsListUseCaseImpl
import tech.hombre.domain.interaction.contests.comments.GetContestCommentsUseCase
import tech.hombre.domain.interaction.contests.comments.GetContestCommentsUseCaseImpl
import tech.hombre.domain.interaction.contests.detail.GetContestDetailUseCase
import tech.hombre.domain.interaction.contests.detail.GetContestDetailUseCaseImpl
import tech.hombre.domain.interaction.countries.GetCountriesUseCase
import tech.hombre.domain.interaction.countries.GetCountriesUseCaseImpl
import tech.hombre.domain.interaction.employerslist.GetEmployersListUseCase
import tech.hombre.domain.interaction.employerslist.GetEmployersListUseCaseImpl
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCase
import tech.hombre.domain.interaction.employerslist.detail.GetEmployerDetailUseCaseImpl
import tech.hombre.domain.interaction.employerslist.reviews.GetEmployerReviewsUseCase
import tech.hombre.domain.interaction.employerslist.reviews.GetEmployerReviewsUseCaseImpl
import tech.hombre.domain.interaction.feedlist.GetFeedListUseCase
import tech.hombre.domain.interaction.feedlist.GetFeedListUseCaseImpl
import tech.hombre.domain.interaction.feedlist.MarkFeedAsReadUseCase
import tech.hombre.domain.interaction.feedlist.MarkFeedAsReadUseCaseImpl
import tech.hombre.domain.interaction.freelancerslist.GetFreelancersListUseCase
import tech.hombre.domain.interaction.freelancerslist.GetFreelancersListUseCaseImpl
import tech.hombre.domain.interaction.freelancerslist.detail.GetFreelancerDetailUseCase
import tech.hombre.domain.interaction.freelancerslist.detail.GetFreelancerDetailUseCaseImpl
import tech.hombre.domain.interaction.freelancerslist.reviews.GetFreelancerReviewsUseCase
import tech.hombre.domain.interaction.freelancerslist.reviews.GetFreelancerReviewsUseCaseImpl
import tech.hombre.domain.interaction.mybids.GetMyBidsListUseCase
import tech.hombre.domain.interaction.mybids.GetMyBidsListUseCaseImpl
import tech.hombre.domain.interaction.mycontests.GetMyContestsListUseCase
import tech.hombre.domain.interaction.mycontests.GetMyContestsListUseCaseImpl
import tech.hombre.domain.interaction.myprofile.GetMyProfileUseCase
import tech.hombre.domain.interaction.myprofile.GetMyProfileUseCaseImpl
import tech.hombre.domain.interaction.projectslist.GetProjectsListUseCase
import tech.hombre.domain.interaction.projectslist.GetProjectsListUseCaseImpl
import tech.hombre.domain.interaction.projectslist.bids.*
import tech.hombre.domain.interaction.projectslist.comments.GetProjectCommentsUseCase
import tech.hombre.domain.interaction.projectslist.comments.GetProjectCommentsUseCaseImpl
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCaseImpl
import tech.hombre.domain.interaction.threadslist.GetThreadsListUseCase
import tech.hombre.domain.interaction.threadslist.GetThreadsListUseCaseImpl
import tech.hombre.domain.interaction.threadslist.messages.GetThreadMessageListUseCase
import tech.hombre.domain.interaction.threadslist.messages.GetThreadMessageListUseCaseImpl

val interactionModule = module {
    factory<GetMyProfileUseCase> { GetMyProfileUseCaseImpl(get()) }
    factory<GetFeedListUseCase> { GetFeedListUseCaseImpl(get()) }
    factory<GetProjectsListUseCase> { GetProjectsListUseCaseImpl(get()) }
    factory<GetContestsListUseCase> { GetContestsListUseCaseImpl(get()) }
    factory<GetFreelancersListUseCase> { GetFreelancersListUseCaseImpl(get()) }
    factory<GetCountriesUseCase> { GetCountriesUseCaseImpl(get()) }
    factory<GetThreadsListUseCase> { GetThreadsListUseCaseImpl(get()) }
    factory<GetMyBidsListUseCase> { GetMyBidsListUseCaseImpl(get()) }
    factory<GetEmployersListUseCase> { GetEmployersListUseCaseImpl(get()) }
    factory<GetFreelancerDetailUseCase> { GetFreelancerDetailUseCaseImpl(get()) }
    factory<GetThreadMessageListUseCase> { GetThreadMessageListUseCaseImpl(get()) }
    factory<GetEmployerDetailUseCase> { GetEmployerDetailUseCaseImpl(get()) }
    factory<GetProjectDetailUseCase> { GetProjectDetailUseCaseImpl(get()) }
    factory<GetFreelancerReviewsUseCase> { GetFreelancerReviewsUseCaseImpl(get()) }
    factory<GetEmployerReviewsUseCase> { GetEmployerReviewsUseCaseImpl(get()) }
    factory<GetContestDetailUseCase> { GetContestDetailUseCaseImpl(get()) }
    factory<GetProjectBidsUseCase> { GetProjectBidsUseCaseImpl(get()) }
    factory<GetProjectCommentsUseCase> { GetProjectCommentsUseCaseImpl(get()) }
    factory<GetContestCommentsUseCase> { GetContestCommentsUseCaseImpl(get()) }
    factory<GetMyContestsListUseCase> { GetMyContestsListUseCaseImpl(get()) }
    factory<MarkFeedAsReadUseCase> { MarkFeedAsReadUseCaseImpl(get()) }
    factory<AddProjectBidsUseCase> { AddProjectBidsUseCaseImpl(get()) }
    factory<RevokeProjectBidsUseCase> { RevokeProjectBidsUseCaseImpl(get()) }
    factory<RejectProjectBidsUseCase> { RejectProjectBidsUseCaseImpl(get()) }
}
