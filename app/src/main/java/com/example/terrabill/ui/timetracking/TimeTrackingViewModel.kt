package com.example.terrabill.ui.timetracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class TimeTrackingViewModel(application: Application) : AndroidViewModel(application) {

    private val _seconds = MutableLiveData<Long>(0)
    val seconds: LiveData<Long> = _seconds

    private var timerJob: Job? = null
    private var isPaused = false

    fun startTimer() {
        if (timerJob == null || isPaused) {
            isPaused = false
            timerJob = viewModelScope.launch {
                while (isActive) {
                    delay(1000)
                    _seconds.value = (_seconds.value ?: 0) + 1
                }
            }
        }
    }

    fun pauseTimer() {
        isPaused = true
        timerJob?.cancel()
        timerJob = null
    }

    fun stopTimer() {
        pauseTimer()
    }

    fun resetTimer() {
        pauseTimer()
        _seconds.value = 0
    }
}