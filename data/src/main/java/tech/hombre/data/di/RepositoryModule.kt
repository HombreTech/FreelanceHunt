package tech.hombre.data.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tech.hombre.data.common.utils.Connectivity
import tech.hombre.data.common.utils.ConnectivityImpl
import tech.hombre.data.repository.bids.BidsRepositoryImpl
import tech.hombre.data.repository.contestslist.ContestCommentsRepositoryImpl
import tech.hombre.data.repository.contestslist.ContestDetailRepositoryImpl
import tech.hombre.data.repository.contestslist.ContestsListRepositoryImpl
import tech.hombre.data.repository.countries.CountriesRepositoryImpl
import tech.hombre.data.repository.employerslist.EmployerDetailRepositoryImpl
import tech.hombre.data.repository.employerslist.EmployerReviewsRepositoryImpl
import tech.hombre.data.repository.employerslist.EmployersListRepositoryImpl
import tech.hombre.data.repository.feedlist.FeedListRepositoryImpl
import tech.hombre.data.repository.freelancerslist.FreelancerDetailRepositoryImpl
import tech.hombre.data.repository.freelancerslist.FreelancerReviewsRepositoryImpl
import tech.hombre.data.repository.freelancerslist.FreelancersRepositoryImpl
import tech.hombre.data.repository.myprofile.MyProfileRepositoryImpl
import tech.hombre.data.repository.projectslist.ProjectBidsRepositoryImpl
import tech.hombre.data.repository.projectslist.ProjectCommentsRepositoryImpl
import tech.hombre.data.repository.projectslist.ProjectDetailRepositoryImpl
import tech.hombre.data.repository.projectslist.ProjectsListRepositoryImpl
import tech.hombre.data.repository.threadslist.ThreadMessageListRepositoryImpl
import tech.hombre.data.repository.threadslist.ThreadsListRepositoryImpl
import tech.hombre.domain.repository.*

val repositoryModule = module {
    factory<Connectivity> { ConnectivityImpl(androidContext()) }
    factory<MyProfileRepository> { MyProfileRepositoryImpl(get()) }
    factory<FeedListRepository> { FeedListRepositoryImpl(get(), get()) }
    factory<ProjectsListRepository> { ProjectsListRepositoryImpl(get(), get()) }
    factory<ContestsListRepository> { ContestsListRepositoryImpl(get(), get()) }
    factory<FreelancersRepository> { FreelancersRepositoryImpl(get()) }
    factory<CountriesRepository> { CountriesRepositoryImpl(get(), get()) }
    factory<ThreadsListRepository> { ThreadsListRepositoryImpl(get()) }
    factory<BidsListRepository> { BidsRepositoryImpl(get()) }
    factory<EmployersListRepository> { EmployersListRepositoryImpl(get()) }
    factory<FreelancerDetailRepository> { FreelancerDetailRepositoryImpl(get()) }
    factory<ThreadMessageListRepository> { ThreadMessageListRepositoryImpl(get()) }
    factory<EmployerDetailRepository> { EmployerDetailRepositoryImpl(get()) }
    factory<ProjectDetailRepository> { ProjectDetailRepositoryImpl(get()) }
    factory<FreelancerReviewsRepository> { FreelancerReviewsRepositoryImpl(get()) }
    factory<EmployerReviewsRepository> { EmployerReviewsRepositoryImpl(get()) }
    factory<ContestDetailRepository> { ContestDetailRepositoryImpl(get()) }
    factory<ProjectBidsRepository> { ProjectBidsRepositoryImpl(get()) }
    factory<ProjectCommentsRepository> { ProjectCommentsRepositoryImpl(get()) }
    factory<ContestCommentsRepository> { ContestCommentsRepositoryImpl(get()) }
}