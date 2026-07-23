package com.example.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.SavedWorkoutPlan
import com.example.network.GeminiAiManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

data class WorkoutExercise(
    val name: String,
    val setsReps: String,
    val restSec: Int,
    val targetMuscle: String,
    val tips: String
)

data class BoxingRound(
    val roundNumber: Int,
    val title: String,
    val comboCue: String, // e.g. "1-2-3 (Jab, Cross, Left Hook)"
    val instructions: String
)

class WorkoutViewModel(private val repository: AppRepository) : ViewModel() {

    val savedWorkouts: StateFlow<List<SavedWorkoutPlan>> = repository.workoutPlanDao.getAllWorkoutPlans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var selectedMode = mutableStateOf("BOXING") // BOXING or GYM
    var selectedGoal = mutableStateOf("BOXING_CARDIO") // BOXING_CARDIO, HYPERTROPHY, STRENGTH, CUTTING
    var isGenerating = mutableStateOf(false)

    var gymExercises = mutableStateOf<List<WorkoutExercise>>(emptyList())
    var boxingRounds = mutableStateOf<List<BoxingRound>>(emptyList())

    // Boxing Timer State
    var isTimerRunning = mutableStateOf(false)
    var currentRoundIndex = mutableStateOf(0)
    var isRestPeriod = mutableStateOf(false)
    var secondsRemaining = mutableStateOf(180) // 3 mins default round
    private var timerJob: Job? = null

    val totalRoundDuration = 180 // 3 minutes
    val restDuration = 60 // 1 minute rest

    init {
        // Default seeding of Boxing Drills
        loadDefaultBoxingRounds()
    }

    fun setWorkoutMode(mode: String) {
        selectedMode.value = mode
    }

    fun generateWorkoutWithAi(language: String) {
        isGenerating.value = true
        viewModelScope.launch {
            val mode = selectedMode.value
            val isAr = language.contains("ar", ignoreCase = true)
            
            val prompt = if (mode == "BOXING") {
                """
                Generate a 5-round boxing workout session. Language: $language.
                Include combo cues like (1-2 Jab Cross, 1-2-3 Jab Cross Hook, 1-2-5 Jab Cross Lead Uppercut).
                Return strict JSON array:
                [
                  {
                    "roundNumber": 1,
                    "title": "Round 1: Shadowboxing & Footwork / الإحماء والرجلين",
                    "comboCue": "1-2 (Jab, Cross)",
                    "instructions": "Focus on high speed foot movement, double jab, slip to the right"
                  }
                ]
                """.trimIndent()
            } else {
                """
                Generate a 5-exercise strength/hypertrophy workout routine for goal: ${selectedGoal.value}. Language: $language.
                Return strict JSON array:
                [
                  {
                    "name": "Exercise Name",
                    "setsReps": "4 sets x 8-10 reps",
                    "restSec": 90,
                    "targetMuscle": "Chest / الصدر",
                    "tips": "Keep elbows at 45 degrees, explode up"
                  }
                ]
                """.trimIndent()
            }

            val jsonStr = GeminiAiManager.generateContent(
                prompt = prompt,
                systemInstructionText = "You are a professional boxing coach and strength trainer."
            )

            try {
                val cleaned = jsonStr.substringAfter("[").substringBeforeLast("]")
                val fullArrayJson = "[" + cleaned + "]"
                val array = JSONArray(fullArrayJson)

                if (mode == "BOXING") {
                    val rounds = mutableListOf<BoxingRound>()
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        rounds.add(
                            BoxingRound(
                                roundNumber = obj.optInt("roundNumber", i + 1),
                                title = obj.optString("title", "Round ${i + 1}"),
                                comboCue = obj.optString("comboCue", "1-2-3 (Jab-Cross-Hook)"),
                                instructions = obj.optString("instructions", "Keep hands up and stay on toes")
                            )
                        )
                    }
                    if (rounds.isNotEmpty()) boxingRounds.value = rounds
                } else {
                    val exList = mutableListOf<WorkoutExercise>()
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        exList.add(
                            WorkoutExercise(
                                name = obj.optString("name", "Exercise ${i + 1}"),
                                setsReps = obj.optString("setsReps", "3 x 10"),
                                restSec = obj.optInt("restSec", 60),
                                targetMuscle = obj.optString("targetMuscle", "Full Body"),
                                tips = obj.optString("tips", "Maintain good form")
                            )
                        )
                    }
                    if (exList.isNotEmpty()) gymExercises.value = exList
                }
            } catch (e: Exception) {
                if (mode == "BOXING") {
                    loadDefaultBoxingRounds()
                } else {
                    loadDefaultGymExercises()
                }
            } finally {
                isGenerating.value = false
            }
        }
    }

    // Timer Logic
    fun startBoxingTimer() {
        if (isTimerRunning.value) return
        isTimerRunning.value = true
        timerJob = viewModelScope.launch {
            while (isTimerRunning.value) {
                delay(1000L)
                if (secondsRemaining.value > 1) {
                    secondsRemaining.value -= 1
                } else {
                    // Switch between Work and Rest
                    if (!isRestPeriod.value) {
                        // Work ended -> Start Rest
                        isRestPeriod.value = true
                        secondsRemaining.value = restDuration
                    } else {
                        // Rest ended -> Start Next Round
                        isRestPeriod.value = false
                        val nextRound = currentRoundIndex.value + 1
                        val totalRounds = if (boxingRounds.value.isEmpty()) 5 else boxingRounds.value.size
                        if (nextRound < totalRounds) {
                            currentRoundIndex.value = nextRound
                            secondsRemaining.value = totalRoundDuration
                        } else {
                            // Session finished
                            isTimerRunning.value = false
                            currentRoundIndex.value = 0
                            secondsRemaining.value = totalRoundDuration
                        }
                    }
                }
            }
        }
    }

    fun pauseBoxingTimer() {
        isTimerRunning.value = false
        timerJob?.cancel()
    }

    fun resetBoxingTimer() {
        pauseBoxingTimer()
        currentRoundIndex.value = 0
        isRestPeriod.value = false
        secondsRemaining.value = totalRoundDuration
    }

    fun saveCurrentWorkout(title: String) {
        viewModelScope.launch {
            val jsonArray = JSONArray()
            if (selectedMode.value == "BOXING") {
                boxingRounds.value.forEach { r ->
                    val obj = JSONObject()
                    obj.put("roundNumber", r.roundNumber)
                    obj.put("title", r.title)
                    obj.put("comboCue", r.comboCue)
                    obj.put("instructions", r.instructions)
                    jsonArray.put(obj)
                }
            } else {
                gymExercises.value.forEach { e ->
                    val obj = JSONObject()
                    obj.put("name", e.name)
                    obj.put("setsReps", e.setsReps)
                    obj.put("restSec", e.restSec)
                    obj.put("targetMuscle", e.targetMuscle)
                    obj.put("tips", e.tips)
                    jsonArray.put(obj)
                }
            }

            val plan = SavedWorkoutPlan(
                title = title,
                category = selectedMode.value,
                goalType = selectedGoal.value,
                detailsJson = jsonArray.toString()
            )
            repository.saveWorkoutPlan(plan)
        }
    }

    private fun loadDefaultBoxingRounds() {
        boxingRounds.value = listOf(
            BoxingRound(1, "الجولة 1: الإحماء واللكمات الأساسية (Jab & Cross)", "1 - 2 (Jab, Cross)", "تركيز على سرعة القدمين والتسديد المستقيم مع حماية الوجه باليد الأخرى."),
            BoxingRound(2, "الجولة 2: دمج الهوك العكسي (Lead Hook)", "1 - 2 - 3 (Jab, Cross, Left Hook)", "تسديد الجاب والكروس ثم الدوران بالوركين للتسديد بهوك خطير على الفك."),
            BoxingRound(3, "الجولة 3: اللكمات الصاعدة وتفادي اللكمات (Slip & Uppercut)", "1 - Slip Right - 2 - 5 (Lead Uppercut)", "تفادي لكمة الخصم لليمين ثم الرد بكروس ولكمة صاعدة قوية."),
            BoxingRound(4, "الجولة 4: ضغط الكيس الثقيل وتدفق اللكمات (Heavy Bag Burnout)", "1 - 2 - 1 - 2 (High Speed Volume)", "تسديد لكمات سريعة متتالية على الكيس الثقيل لمدة 3 دقائق دون توقف."),
            BoxingRound(5, "الجولة 5: جولة البطل النهائية (Championship Finish)", "Free Style Combos + Body Shots", "تنوع بين لكمات الجسم والوجه مع حركة دائرية مستمرة في الحلبة.")
        )
    }

    private fun loadDefaultGymExercises() {
        gymExercises.value = listOf(
            WorkoutExercise("ضغط الصدر بالبار (Barbell Bench Press)", "4 مجموعات × 8 عدات", 90, "الصدر العضلي العلوي والسفلي", "حافظ على ثبات الكتفين واضغط بقوة أثناء الزفير."),
            WorkoutExercise("العقلة بالأوزان (Weighted Pull-Ups)", "4 مجموعات × 8 عدات", 90, "عضلات الظهر واللأتس", "اسحب الصدر لنقطة البار وارتفع بثبات دون تأرجح."),
            WorkoutExercise("السكوات الخلفي (Barbell Back Squat)", "4 مجموعات × 10 عدات", 120, "عضلات الأرجل والأربطة", "انزل بزاوية 90 درجة مع دفع الأرض بالكعبين."),
            WorkoutExercise("ضغط الكتف العسكري (Overhead Shoulder Press)", "3 مجموعات × 10 عدات", 90, "الأكتاف الأمامية والجانبية", "اشدد عضلات البطن أثناء الدفع للأعلى."),
            WorkoutExercise("تمريرة البايسبس والترايسبس سوبرسيت", "3 مجموعات × 12 عادة", 60, "عضلات الذراعين", "تركيز تام على الانقباض والانبساط البطين.")
        )
    }
}
