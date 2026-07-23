package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_logs")
data class FoodLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String, // YYYY-MM-DD
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val name: String,
    val calories: Int,
    val protein: Int, // grams
    val carbs: Int,   // grams
    val fats: Int,    // grams
    val timeString: String = ""
)

@Entity(tableName = "user_goals")
data class UserGoal(
    @PrimaryKey val id: Int = 1,
    val calorieGoal: Int = 2500,
    val proteinGoal: Int = 160,
    val carbsGoal: Int = 280,
    val fatsGoal: Int = 70,
    val stepGoal: Int = 10000,
    val waterGoalMl: Int = 3000,
    val currentWeightKg: Float = 75.0f,
    val targetWeightKg: Float = 80.0f,
    val goalType: String = "BULKING" // BULKING, CUTTING, MAINTENANCE
)

@Entity(tableName = "step_logs")
data class StepLog(
    @PrimaryKey val date: String, // YYYY-MM-DD
    val steps: Int,
    val distanceKm: Float,
    val caloriesBurned: Int
)

@Entity(tableName = "saved_meal_plans")
data class SavedMealPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val goalType: String, // BULKING, CUTTING, MAINTENANCE
    val targetCalories: Int,
    val mealsJson: String, // JSON array of meal objects
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "saved_workout_plans")
data class SavedWorkoutPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String, // GYM, BOXING, CARDIO
    val goalType: String, // BULKING, CUTTING, MAINTENANCE, BOXING
    val detailsJson: String, // JSON structure of rounds/exercises
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "focus_sessions")
data class FocusSession(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sessionType: String, // MEAL, WORKOUT, BOXING, CARDIO
    val durationMinutes: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val completed: Boolean = true
)
