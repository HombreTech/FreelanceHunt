package tech.hombre.data.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import tech.hombre.data.database.*

private const val FEED_LIST_DB = "feedlist-database"
private const val PROJECTS_LIST_DB = "projectslist-database"
private const val CONTESTS_LIST_DB = "contestslist-database"
private const val COUNTRIES_DB = "countries-database"
private const val SKILLS_DB = "skills-database"

val databaseModule = module {
    single {
        Room.databaseBuilder(androidContext(), FeedListDatabase::class.java, FEED_LIST_DB)
            .fallbackToDestructiveMigration().build()
    }
    single {
        Room.databaseBuilder(androidContext(), ProjectsListDatabase::class.java, PROJECTS_LIST_DB)
            .fallbackToDestructiveMigration().build()
    }
    single {
        Room.databaseBuilder(androidContext(), ContestsListDatabase::class.java, CONTESTS_LIST_DB)
            .fallbackToDestructiveMigration().build()
    }
    single {
        Room.databaseBuilder(androidContext(), CountriesDatabase::class.java, COUNTRIES_DB)
            .fallbackToDestructiveMigration().build()
    }
    single {
        Room.databaseBuilder(androidContext(), SkillsDatabase::class.java, SKILLS_DB)
            .fallbackToDestructiveMigration().build()
    }
    factory { get<FeedListDatabase>().feedListDao() }
    factory { get<ProjectsListDatabase>().projectsListDao() }
    factory { get<ContestsListDatabase>().contestsListDao() }
    factory { get<CountriesDatabase>().countriesDao() }
    factory { get<SkillsDatabase>().skillsDao() }
}