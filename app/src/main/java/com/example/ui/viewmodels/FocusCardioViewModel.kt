package com.example.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.FocusSession
import com.example.data.RiyadhCardioRepository
import com.example.data.RiyadhCardioSpot
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FocusCardioViewModel(private val repository: AppRepository) : ViewModel() {

    val recentSessions: StateFlow<List<FocusSession>> = repository.focusDao.getRecentFocusSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Riyadh Spots State
    var searchQuery = mutableStateOf("")
    val spotsList: List<RiyadhCardioSpot>
        get() {
            val q = searchQuery.value.lowercase().trim()
            if (q.isEmpty()) return RiyadhCardioRepository.spots
            return RiyadhCardioRepository.spots.filter {
                it.nameAr.contains(q, ignoreCase = true) ||
                it.nameEn.contains(q, ignoreCase = true) ||
                it.areaAr.contains(q, ignoreCase = true) ||
                it.areaEn.contains(q, ignoreCase = true)
            }
        }

    // Focus Blocker Mode State
    var isFocusActive = mutableStateOf(false)
    var focusType = mutableStateOf("MEAL") // MEAL, WORKOUT, BOXING, CARDIO
    var focusDurationMinutes = mutableStateOf(30)
    var secondsLeft = mutableStateOf(1800) // 30 mins default
    var blockedAppsCount = mutableStateOf(14) // Simulated blocked apps (Social Media, Games, Notifications)

    private var focusJob: Job? = null

    fun startFocusSession(type: String, durationMins: Int) {
        focusType.value = type
        focusDurationMinutes.value = durationMins
        secondsLeft.value = durationMins * 60
        isFocusActive.value = true

        focusJob?.cancel()
        focusJob = viewModelScope.launch {
            while (isFocusActive.value && secondsLeft.value > 0) {
                delay(1000L)
                secondsLeft.value -= 1
            }
            if (secondsLeft.value <= 0 && isFocusActive.value) {
                endFocusSession(completed = true)
            }
        }
    }

    fun stopFocusSession() {
        endFocusSession(completed = false)
    }

    private fun endFocusSession(completed: Boolean) {
        isFocusActive.value = false
        focusJob?.cancel()
        viewModelScope.launch {
            repository.saveFocusSession(
                FocusSession(
                    sessionType = focusType.value,
                    durationMinutes = focusDurationMinutes.value,
                    completed = completed
                )
            )
        }
    }

    fun launchGoogleMapsDirections(context: Context, spot: RiyadhCardioSpot) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${Uri.encode(spot.googleMapsQuery)}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }
        try {
            context.startActivity(mapIntent)
        } catch (e: Exception) {
            // Fallback web browser maps intent
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode(spot.googleMapsQuery)}"))
            context.startActivity(webIntent)
        }
    }
}
