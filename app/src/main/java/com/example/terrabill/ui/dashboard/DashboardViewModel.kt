package com.example.terrabill.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.terrabill.data.local.DatabaseProvider
import com.example.terrabill.data.model.JobDetails
import com.example.terrabill.data.repository.JobRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val jobDao = DatabaseProvider.getDatabase(application).jobDao()
    private val repository = JobRepository(jobDao)

    val jobs: LiveData<List<JobDetails>> = repository
        .getJobDetails()
        .asLiveData()
    private val iso = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    fun filterJobsByDay(): List<JobDetails> {
        val today = LocalDate.now()
        return jobs.value?.filter {
            LocalDate.parse(it.job.startAt.substring(0, 10)) == today
        } ?: emptyList()
    }

    fun filterJobsByWeek(): List<JobDetails> {
        val now = LocalDate.now()
        val weekFields = WeekFields.of(Locale.getDefault())
        val currentWeek = now.get(weekFields.weekOfWeekBasedYear())
        val currentYear = now.year

        return jobs.value?.filter {
            val date = LocalDate.parse(it.job.startAt.substring(0, 10))
            date.get(weekFields.weekOfWeekBasedYear()) == currentWeek &&
                    date.year == currentYear
        } ?: emptyList()
    }

    fun filterJobsByMonth(): List<JobDetails> {
        val now = LocalDate.now()
        return jobs.value?.filter {
            val date = LocalDate.parse(it.job.startAt.substring(0, 10))
            date.month == now.month && date.year == now.year
        } ?: emptyList()
    }

    fun allJobs(): List<JobDetails> = jobs.value ?: emptyList()

    fun jobsOn(date: LocalDate): List<JobDetails> =
        allJobs().filter { j ->
            runCatching { LocalDateTime.parse(j.job.startAt, iso) }
                .getOrNull()
                ?.toLocalDate() == date
        }

    fun jobDates(): Set<LocalDate> =
        allJobs().mapNotNull { j ->
            runCatching { LocalDateTime.parse(j.job.startAt, iso).toLocalDate() }.getOrNull()
        }.toSet()
}