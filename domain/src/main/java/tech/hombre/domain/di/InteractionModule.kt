package tech.hombre.domain.di

import org.koin.dsl.module
import tech.hombre.domain.interaction.cities.GetCitiesUseCase
import tech.hombre.domain.interaction.cities.GetCitiesUseCaseImpl
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
import tech.hombre.domain.interaction.myprojects.GetMyProjectsListUseCase
import tech.hombre.domain.interaction.myprojects.GetMyProjectsListUseCaseImpl
import tech.hombre.domain.interaction.myworkspaces.*
import tech.hombre.domain.interaction.projectslist.*
import tech.hombre.domain.interaction.projectslist.bids.*
import tech.hombre.domain.interaction.projectslist.comments.GetProjectCommentsUseCase
import tech.hombre.domain.interaction.projectslist.comments.GetProjectCommentsUseCaseImpl
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCase
import tech.hombre.domain.interaction.projectslist.detail.GetProjectDetailUseCaseImpl
import tech.hombre.domain.interaction.skills.GetSkillsUseCase
import tech.hombre.domain.interaction.skills.GetSkillsUseCaseImpl
import tech.hombre.domain.interaction.threadslist.GetThreadsListUseCase
import tech.hombre.domain.interaction.threadslist.GetThreadsListUseCaseImpl
import tech.hombre.domain.interaction.threadslist.messages.*

val interactionModule = module {
    factory<GetMyProfileUseCase> { GetMyProfileUseCaseImpl(get()) }
    factory<GetFeedListUseCase> { GetFeedListUseCaseImpl(get()) }
    factory<GetProjectsListUseCase> { GetProjectsListUseCaseImpl(get()) }
    factory<GetContestsListUseCase> { GetContestsListUseCaseImpl(get()) }
    factory<GetFreelancersListUseCase> { GetFreelancersListUseCaseImpl(get()) }
    factory<GetCountriesUseCase> { GetCountriesUseCaseImpl(get()) }
    factory<GetCitiesUseCase> { GetCitiesUseCaseImpl(get()) }
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
    factory<ChooseProjectBidsUseCase> { ChooseProjectBidsUseCaseImpl(get()) }
    factory<SendThreadMessageUseCase> { SendThreadMessageUseCaseImpl(get()) }
    factory<CreateThreadUseCase> { CreateThreadUseCaseImpl(get()) }
    factory<GetMyProjectsListUseCase> { GetMyProjectsListUseCaseImpl(get()) }
    factory<GetMyWorkspacesListUseCase> { GetMyWorkspacesListUseCaseImpl(get()) }
    factory<ProposeConditionsUseCase> { ProposeConditionsUseCaseImpl(get()) }
    factory<AcceptConditionsUseCase> { AcceptConditionsUseCaseImpl(get()) }
    factory<ExtendConditionsUseCase> { ExtendConditionsUseCaseImpl(get()) }
    factory<RejectConditionsUseCase> { RejectConditionsUseCaseImpl(get()) }
    factory<RequestArbitrageUseCase> { RequestArbitrageUseCaseImpl(get()) }
    factory<WorkspaceCloseUseCase> { WorkspaceCloseUseCaseImpl(get()) }
    factory<WorkspaceCompleteUseCase> { WorkspaceCompleteUseCaseImpl(get()) }
    factory<WorkspaceIncompleteUseCase> { WorkspaceIncompleteUseCaseImpl(get()) }
    factory<WorkspaceReviewUseCase> { WorkspaceReviewUseCaseImpl(get()) }
    factory<NewProjectUseCase> { NewProjectUseCaseImpl(get()) }
    factory<NewPersonalProjectUseCase> { NewPersonalProjectUseCaseImpl(get()) }
    factory<GetSkillsUseCase> { GetSkillsUseCaseImpl(get()) }
    factory<ExtendProjectUseCase> { ExtendProjectUseCaseImpl(get()) }
    factory<UpdateProjectUseCase> { UpdateProjectUseCaseImpl(get()) }
    factory<AmendProjectUseCase> { AmendProjectUseCaseImpl(get()) }
    factory<CloseProjectUseCase> { CloseProjectUseCaseImpl(get()) }
    factory<ReopenProjectUseCase> { ReopenProjectUseCaseImpl(get()) }
}
