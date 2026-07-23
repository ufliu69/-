package com.example.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppRepository
import com.example.data.SavedMealPlan
import com.example.network.GeminiAiManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

data class GeneratedMeal(
    val mealType: String,
    val title: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val ingredients: String
)

class MealPlanViewModel(private val repository: AppRepository) : ViewModel() {

    val savedMealPlans: StateFlow<List<SavedMealPlan>> = repository.mealPlanDao.getAllMealPlans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var selectedGoalType = mutableStateOf("BULKING") // BULKING, CUTTING, MAINTENANCE
    var targetCalories = mutableStateOf(2800)
    var isGenerating = mutableStateOf(false)
    var generatedMeals = mutableStateOf<List<GeneratedMeal>>(emptyList())

    fun setGoal(goal: String) {
        selectedGoalType.value = goal
        targetCalories.value = when (goal) {
            "BULKING" -> 3000
            "CUTTING" -> 2000
            else -> 2500
        }
    }

    fun generateMealPlan(language: String) {
        isGenerating.value = true
        viewModelScope.launch {
            val prompt = """
                Generate a full 1-day nutrition meal plan for goal: ${selectedGoalType.value} (Target Calories: ${targetCalories.value} kcal).
                Language requirement: Output in $language.
                Include 4 meals: Breakfast, Lunch, Dinner, Snack.
                Return strictly JSON array:
                [
                  {
                    "mealType": "Breakfast / الفطور",
                    "title": "Meal Title",
                    "calories": 700,
                    "protein": 45,
                    "carbs": 80,
                    "fats": 20,
                    "ingredients": "Detailed ingredients with local Arabic/Middle Eastern/Healthy options"
                  }
                ]
            """.trimIndent()

            val jsonStr = GeminiAiManager.generateContent(
                prompt = prompt,
                systemInstructionText = "You are an elite sports nutritionist specialized in bodybuilding bulking & cutting diets."
            )

            try {
                val cleaned = jsonStr.substringAfter("[").substringBeforeLast("]")
                val fullArrayJson = "[" + cleaned + "]"
                val array = JSONArray(fullArrayJson)
                val list = mutableListOf<GeneratedMeal>()

                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        GeneratedMeal(
                            mealType = obj.optString("mealType", "Meal ${i + 1}"),
                            title = obj.optString("title", "Custom Fitness Meal"),
                            calories = obj.optInt("calories", 600),
                            protein = obj.optInt("protein", 40),
                            carbs = obj.optInt("carbs", 60),
                            fats = obj.optInt("fats", 18),
                            ingredients = obj.optString("ingredients", "High protein chicken, rice, healthy fats")
                        )
                    )
                }
                generatedMeals.value = list
            } catch (e: Exception) {
                // Fallback structured plan
                generatedMeals.value = getFallbackMealPlan(selectedGoalType.value, language)
            } finally {
                isGenerating.value = false
            }
        }
    }

    fun saveCurrentMealPlan() {
        val meals = generatedMeals.value
        if (meals.isEmpty()) return
        viewModelScope.launch {
            val jsonArray = JSONArray()
            meals.forEach { m ->
                val obj = JSONObject()
                obj.put("mealType", m.mealType)
                obj.put("title", m.title)
                obj.put("calories", m.calories)
                obj.put("protein", m.protein)
                obj.put("carbs", m.carbs)
                obj.put("fats", m.fats)
                obj.put("ingredients", m.ingredients)
                jsonArray.put(obj)
            }

            val plan = SavedMealPlan(
                title = "خطة ${selectedGoalType.value} (${targetCalories.value} سعرة)",
                goalType = selectedGoalType.value,
                targetCalories = targetCalories.value,
                mealsJson = jsonArray.toString()
            )
            repository.saveMealPlan(plan)
        }
    }

    fun deletePlan(id: Int) {
        viewModelScope.launch {
            repository.deleteMealPlan(id)
        }
    }

    private fun getFallbackMealPlan(goal: String, language: String): List<GeneratedMeal> {
        val isAr = language.contains("ar", ignoreCase = true)
        return if (goal == "BULKING") {
            listOf(
                GeneratedMeal(
                    if (isAr) "الفطور" else "Breakfast",
                    if (isAr) "شوفان بالبروتين والموز والعسل وزبدة الفول" else "Oatmeal with Whey, Banana, Honey & Peanut Butter",
                    750, 48, 95, 22,
                    if (isAr) "100g شوفان، 1 سكوب واي بروتين، موزة، 20g زبدة فول سوداني، عسل طبيعي" else "100g oats, 1 scoop whey, 1 banana, 20g peanut butter, honey"
                ),
                GeneratedMeal(
                    if (isAr) "الغداء" else "Lunch",
                    if (isAr) "كبسة دجاج صحية مع أرز بسمتي وسلطة" else "Healthy Chicken Kabsah with Basmati Rice & Salad",
                    850, 65, 100, 20,
                    if (isAr) "250g صدر دجاج مشوي، 200g أرز بسمتي، سلطة خضراء مع زيت زيتون" else "250g grilled chicken breast, 200g basmati rice, olive oil green salad"
                ),
                GeneratedMeal(
                    if (isAr) "العشاء" else "Dinner",
                    if (isAr) "ستيك لحم بقري مع بطاطس مهروسة وخضار" else "Beef Steak with Mashed Potatoes & Veggies",
                    800, 58, 70, 28,
                    if (isAr) "200g ستيك لحم صافي، 250g بطاطس، بروكلي مشوي" else "200g lean beef steak, 250g potatoes, roasted broccoli"
                ),
                GeneratedMeal(
                    if (isAr) "سناك تضخيم" else "Bulking Snack",
                    if (isAr) "مخفوق التضخيم السريع (Mass Gainer Shake)" else "High-Calorie Mass Gainer Smoothie",
                    600, 40, 75, 15,
                    if (isAr) "حليب كامل الدسم، واي بروتين، تمر، زبدة لوز" else "Full cream milk, whey, dates, almond butter"
                )
            )
        } else {
            listOf(
                GeneratedMeal(
                    if (isAr) "الفطور" else "Breakfast",
                    if (isAr) "بياض بيض مع صفار، جبن قريش وخيار" else "Egg White Omelette with Cottage Cheese",
                    400, 38, 15, 18,
                    if (isAr) "5 بياض بيض + 1 بيضة كاملة، 100g جبن قريش، خيار وطماطم" else "5 egg whites + 1 whole egg, 100g cottage cheese, veggies"
                ),
                GeneratedMeal(
                    if (isAr) "الغداء" else "Lunch",
                    if (isAr) "سلمون مشوي مع كينوا وبروكلي" else "Grilled Salmon with Quinoa & Steamed Broccoli",
                    550, 45, 35, 20,
                    if (isAr) "200g سلمون، 100g كينوا مطبوخة، بروكلي بالليمون" else "200g salmon fillet, 100g cooked quinoa, steamed broccoli"
                ),
                GeneratedMeal(
                    if (isAr) "العشاء" else "Dinner",
                    if (isAr) "صدور دجاج مشوية مع سلطة فتوش صحية" else "Grilled Chicken Breast with Healthy Fattoush",
                    450, 52, 20, 12,
                    if (isAr) "220g صدور دجاج متبلة، سلطة خضراء مع خيار ورمان وزيت زيتون خفيف" else "220g seasoned chicken breast, fresh green pomegranate salad"
                ),
                GeneratedMeal(
                    if (isAr) "سناك تنشيف" else "Cutting Snack",
                    if (isAr) "زبادي يوناني خالي الدسم مع توت مشكل" else "Non-Fat Greek Yogurt with Mixed Berries",
                    200, 22, 18, 2,
                    if (isAr) "170g زبادي يوناني، 50g توت أزرق وأحمر" else "170g plain Greek yogurt, 50g fresh blueberries"
                )
            )
        }
    }
}
