package tech.hombre.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import tech.hombre.domain.model.*

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromMyProfileToJson(profile: MyProfile?): String {
        return profile?.let { gson.toJson(it) } ?: ""
    }

    @TypeConverter
    fun fromJsonToMyProfile(json: String): MyProfile {
        val type = object : TypeToken<MyProfile>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromFeedListToJson(feed: FeedList?): String {
        return feed?.let { gson.toJson(it) } ?: ""
    }

    @TypeConverter
    fun fromJsonToFeedList(json: String): FeedList {
        val type = object : TypeToken<FeedList>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromProjectsListToJson(projects: ProjectsList?): String {
        return projects?.let { gson.toJson(it) } ?: ""
    }

    @TypeConverter
    fun fromJsonToProjectsList(json: String): ProjectsList {
        val type = object : TypeToken<ProjectsList>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromContestsListListToJson(contests: ContestsList?): String {
        return contests?.let { gson.toJson(it) } ?: ""
    }

    @TypeConverter
    fun fromJsonToContestsListList(json: String): ContestsList {
        val type = object : TypeToken<ContestsList>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromCountriesToJson(countries: Countries?): String {
        return countries?.let { gson.toJson(it) } ?: ""
    }

    @TypeConverter
    fun fromJsonToCountries(json: String): Countries {
        val type = object : TypeToken<Countries>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromCitiesToJson(cities: Cities?): String {
        return cities?.let { gson.toJson(it) } ?: ""
    }

    @TypeConverter
    fun fromJsonToCities(json: String): Cities {
        val type = object : TypeToken<Cities>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromSkillsToJson(skills: SkillList?): String {
        return skills?.let { gson.toJson(it) } ?: ""
    }

    @TypeConverter
    fun fromJsonToSkills(json: String): SkillList {
        val type = object : TypeToken<SkillList>() {}.type
        return gson.fromJson(json, type)
    }
}