package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        if (value == null) return "[]"
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, String::class.java)
        return moshi.adapter<List<String>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromEducationList(value: List<Education>?): String {
        val type = Types.newParameterizedType(List::class.java, Education::class.java)
        return moshi.adapter<List<Education>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toEducationList(value: String?): List<Education> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, Education::class.java)
        return moshi.adapter<List<Education>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromExperienceList(value: List<WorkExperience>?): String {
        val type = Types.newParameterizedType(List::class.java, WorkExperience::class.java)
        return moshi.adapter<List<WorkExperience>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toExperienceList(value: String?): List<WorkExperience> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, WorkExperience::class.java)
        return moshi.adapter<List<WorkExperience>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromProjectList(value: List<ProjectItem>?): String {
        val type = Types.newParameterizedType(List::class.java, ProjectItem::class.java)
        return moshi.adapter<List<ProjectItem>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toProjectList(value: String?): List<ProjectItem> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, ProjectItem::class.java)
        return moshi.adapter<List<ProjectItem>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromSkillList(value: List<SkillItem>?): String {
        val type = Types.newParameterizedType(List::class.java, SkillItem::class.java)
        return moshi.adapter<List<SkillItem>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toSkillList(value: String?): List<SkillItem> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, SkillItem::class.java)
        return moshi.adapter<List<SkillItem>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromCertList(value: List<CertificationItem>?): String {
        val type = Types.newParameterizedType(List::class.java, CertificationItem::class.java)
        return moshi.adapter<List<CertificationItem>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toCertList(value: String?): List<CertificationItem> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, CertificationItem::class.java)
        return moshi.adapter<List<CertificationItem>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromBreakdown(value: ResumeFitBreakdown?): String {
        return moshi.adapter(ResumeFitBreakdown::class.java).toJson(value ?: ResumeFitBreakdown())
    }

    @TypeConverter
    fun toBreakdown(value: String?): ResumeFitBreakdown {
        if (value.isNullOrEmpty()) return ResumeFitBreakdown()
        return moshi.adapter(ResumeFitBreakdown::class.java).fromJson(value) ?: ResumeFitBreakdown()
    }

    @TypeConverter
    fun fromCriteriaList(value: List<RequirementCriterion>?): String {
        val type = Types.newParameterizedType(List::class.java, RequirementCriterion::class.java)
        return moshi.adapter<List<RequirementCriterion>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toCriteriaList(value: String?): List<RequirementCriterion> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, RequirementCriterion::class.java)
        return moshi.adapter<List<RequirementCriterion>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromFixesList(value: List<ResumeFixSuggestion>?): String {
        val type = Types.newParameterizedType(List::class.java, ResumeFixSuggestion::class.java)
        return moshi.adapter<List<ResumeFixSuggestion>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toFixesList(value: String?): List<ResumeFixSuggestion> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, ResumeFixSuggestion::class.java)
        return moshi.adapter<List<ResumeFixSuggestion>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromLearningPlan(value: List<LearningPlanDay>?): String {
        val type = Types.newParameterizedType(List::class.java, LearningPlanDay::class.java)
        return moshi.adapter<List<LearningPlanDay>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toLearningPlan(value: String?): List<LearningPlanDay> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, LearningPlanDay::class.java)
        return moshi.adapter<List<LearningPlanDay>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromInterviewQuestions(value: List<InterviewQuestionItem>?): String {
        val type = Types.newParameterizedType(List::class.java, InterviewQuestionItem::class.java)
        return moshi.adapter<List<InterviewQuestionItem>>(type).toJson(value ?: emptyList())
    }

    @TypeConverter
    fun toInterviewQuestions(value: String?): List<InterviewQuestionItem> {
        if (value.isNullOrEmpty()) return emptyList()
        val type = Types.newParameterizedType(List::class.java, InterviewQuestionItem::class.java)
        return moshi.adapter<List<InterviewQuestionItem>>(type).fromJson(value) ?: emptyList()
    }

    @TypeConverter
    fun fromAppStatus(status: ApplicationStatus?): String {
        return (status ?: ApplicationStatus.APPLIED).name
    }

    @TypeConverter
    fun toAppStatus(value: String?): ApplicationStatus {
        return try {
            ApplicationStatus.valueOf(value ?: ApplicationStatus.APPLIED.name)
        } catch (_: Exception) {
            ApplicationStatus.APPLIED
        }
    }
}
