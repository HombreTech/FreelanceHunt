package tech.hombre.freelancehunt.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import org.koin.dsl.module
import tech.hombre.data.common.coroutine.CoroutineContextProvider
import tech.hombre.freelancehunt.framework.billing.BillingClientModule
import tech.hombre.freelancehunt.framework.notifications.AndroidNotificationService
import tech.hombre.freelancehunt.framework.tasks.FeedWorker
import tech.hombre.freelancehunt.framework.tasks.ProjectsWorker
import tech.hombre.freelancehunt.framework.tasks.ThreadsWorker
import tech.hombre.freelancehunt.routing.AppFragmentNavigator
import tech.hombre.freelancehunt.routing.AppNavigator
import tech.hombre.freelancehunt.ui.main.view.fragments.SettingFragment

val appModule = module {
    single { CoroutineContextProvider() }
    single { (activity: AppCompatActivity) -> AppNavigator(activity) }
    single { (activity: FragmentActivity) -> AppFragmentNavigator(activity) }
    single { BillingClientModule(get()) }
    single { SettingFragment() }
    single { AndroidNotificationService(get()) }
    single { FeedWorker(get(), get()) }
    single { ThreadsWorker(get(), get()) }
    single { ProjectsWorker(get(), get()) }
}