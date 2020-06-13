package tech.hombre.freelancehunt.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import tech.hombre.freelancehunt.ui.contest.presentation.ContestCommentsViewModel
import tech.hombre.freelancehunt.ui.contest.presentation.ContestDetailViewModel
import tech.hombre.freelancehunt.ui.contest.presentation.ContestOverviewViewModel
import tech.hombre.freelancehunt.ui.contest.presentation.ContestPublicViewModel
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerDetailViewModel
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerPublicViewModel
import tech.hombre.freelancehunt.ui.employers.presentation.EmployerReviewsViewModel
import tech.hombre.freelancehunt.ui.employers.presentation.EmployersViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerDetailViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerPublicViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancerReviewsViewModel
import tech.hombre.freelancehunt.ui.freelancers.presentation.FreelancersViewModel
import tech.hombre.freelancehunt.ui.login.presentation.LoginViewModel
import tech.hombre.freelancehunt.ui.main.presentation.*
import tech.hombre.freelancehunt.ui.my.bids.presentation.MyBidsViewModel
import tech.hombre.freelancehunt.ui.my.contests.presentation.MyContestsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectBidsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectCommentsViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectDetailViewModel
import tech.hombre.freelancehunt.ui.project.presentation.ProjectPublicViewModel
import tech.hombre.freelancehunt.ui.threads.presentation.ThreadMessagesViewModel
import tech.hombre.freelancehunt.ui.threads.presentation.ThreadsViewModel

val presentationModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get(), get(), get()) }
    viewModel { FeedViewModel(get(), get(), get()) }
    viewModel { ProjectsViewModel(get(), get()) }
    viewModel { ContestsViewModel(get(), get()) }
    viewModel { FreelancersViewModel(get(), get()) }
    viewModel { ThreadsViewModel(get()) }
    viewModel { MyBidsViewModel(get(), get()) }
    viewModel { EmployersViewModel(get(), get(), get()) }
    viewModel { FreelancerDetailViewModel(get(), get()) }
    viewModel { ThreadMessagesViewModel(get()) }
    viewModel { EmployerDetailViewModel(get(), get()) }
    viewModel { ProjectDetailViewModel(get(), get()) }
    viewModel { FreelancerReviewsViewModel(get(), get(), get()) }
    viewModel { EmployerReviewsViewModel(get(), get(), get()) }
    viewModel { ContestDetailViewModel(get(), get()) }
    viewModel { ProjectBidsViewModel(get(), get(), get(), get(), get()) }
    viewModel { ProjectCommentsViewModel(get(), get(), get()) }
    viewModel { MainPublicViewModel() }
    viewModel { ProjectPublicViewModel() }
    viewModel { FreelancerPublicViewModel() }
    viewModel { EmployerPublicViewModel() }
    viewModel { ContestCommentsViewModel(get(), get(), get()) }
    viewModel { ContestPublicViewModel() }
    viewModel { ContestOverviewViewModel(get()) }
    viewModel { MyContestsViewModel(get(), get()) }

}