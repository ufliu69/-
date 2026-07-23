package com.example.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.FoodLog
import com.example.data.StepLog
import com.example.data.UserGoal
import com.example.network.GeminiAiManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject

class CalorieViewModel(private val repository: AppRepository) : ViewModel() {

    val todayFoodLogs: StateFlow<List<FoodLog>> = repository.getTodayFoodLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userGoal: StateFlow<UserGoal> = repository.getUserGoal()
        .map { it ?: UserGoal() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserGoal())

    val todayStepLog: StateFlow<StepLog> = repository.getTodaySteps()
        .map { it ?: StepLog(date = repository.getTodayString(), steps = 6420, distanceKm = 4.8f, caloriesBurned = 280) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), StepLog(repository.getTodayString(), 6420, 4.8f, 280))

    var isAiAnalyzing = mutableStateOf(false)
        private set

    var aiErrorMessage = mutableStateOf<String?>(null)
        private set

    fun addManualMeal(name: String, calories: Int, protein: Int, carbs: Int, fats: Int, mealType: String) {
        viewModelScope.launch {
            val log = FoodLog(
                date = repository.getTodayString(),
                mealType = mealType,
                name = name,
                calories = calories,
                protein = protein,
                carbs = carbs,
                fats = fats,
                timeString = getCurrentTimeString()
            )
            repository.saveFoodLog(log)
        }
    }

    fun analyzeAndAddMealWithAi(userInput: String, mealType: String) {
        if (userInput.isBlank()) return
        isAiAnalyzing.value = true
        aiErrorMessage.value = null

        viewModelScope.launch {
            val prompt = """
                Analyze this meal text provided by the user in Arabic or English: "$userInput".
                Estimate nutritional facts and respond strictly in JSON format with keys:
                {
                  "name": "Short meal name in Arabic/English",
                  "calories": integer,
                  "protein": integer grams,
                  "carbs": integer grams,
                  "fats": integer grams,
                  "advice": "Short nutritional insight"
                }
            """.trimIndent()

            val jsonStr = GeminiAiManager.generateContent(
                prompt = prompt,
                systemInstructionText = "You are a expert sports nutritionist. Output strict JSON only."
            )

            try {
                val cleaned = jsonStr.substringAfter("{").substringBeforeLast("}")
                val fullJson = "{" + cleaned + "}"
                val json = JSONObject(fullJson)

                val name = json.optString("name", userInput)
                val calories = json.optInt("calories", 500)
                val protein = json.optInt("protein", 35)
                val carbs = json.optInt("carbs", 50)
                val fats = json.optInt("fats", 15)

                val log = FoodLog(
                    date = repository.getTodayString(),
                    mealType = mealType,
                    name = name,
                    calories = calories,
                    protein = protein,
                    carbs = carbs,
                    fats = fats,
                    timeString = getCurrentTimeString()
                )
                repository.saveFoodLog(log)
            } catch (e: Exception) {
                val log = FoodLog(
                    date = repository.getTodayString(),
                    mealType = mealType,
                    name = userInput,
                    calories = 450,
                    protein = 30,
                    carbs = 45,
                    fats = 14,
                    timeString = getCurrentTimeString()
                )
                repository.saveFoodLog(log)
            } finally {
                isAiAnalyzing.value = false
            }
        }
    }

    fun deleteMeal(id: Int) {
        viewModelScope.launch {
            repository.deleteFoodLog(id)
        }
    }

    fun addSteps(added: Int) {
        viewModelScope.launch {
            val current = todayStepLog.value
            val newSteps = current.steps + added
            val newDist = newSteps * 0.00075f
            val newCals = (newSteps * 0.04f).toInt()
            val updated = StepLog(date = repository.getTodayString(), steps = newSteps, distanceKm = newDist, caloriesBurned = newCals)
            repository.stepDao.insertOrUpdateStepLog(updated)
        }
    }

    fun updateGoal(newGoal: UserGoal) {
        viewModelScope.launch {
            repository.saveUserGoal(newGoal)
        }
    }

    private fun getCurrentTimeString(): String {
        val sdf = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.US)
        return sdf.format(java.util.Date())
    }
}
