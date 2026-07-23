package com.example.data

import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppRepository(private val db: AppDatabase) {
    val foodDao = db.foodDao()
    val goalDao = db.goalDao()
    val stepDao = db.stepDao()
    val mealPlanDao = db.mealPlanDao()
    val workoutPlanDao = db.workoutPlanDao()
    val focusDao = db.focusDao()

    fun getTodayString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    fun getTodayFoodLogs(): Flow<List<FoodLog>> {
        return foodDao.getFoodLogsForDate(getTodayString())
    }

    fun getUserGoal(): Flow<UserGoal?> = goalDao.getUserGoal()

    suspend fun saveUserGoal(goal: UserGoal) {
        goalDao.updateUserGoal(goal)
    }

    fun getTodaySteps(): Flow<StepLog?> {
        return stepDao.getStepLogForDate(getTodayString())
    }

    suspend fun addSteps(additionalSteps: Int) {
        val today = getTodayString()
        // We will query current step log and increment
        // Simple logic handled in VM or here
    }

    suspend fun saveFoodLog(log: FoodLog) {
        foodDao.insertFoodLog(log)
    }

    suspend fun deleteFoodLog(id: Int) {
        foodDao.deleteFoodLog(id)
    }

    suspend fun saveMealPlan(plan: SavedMealPlan) {
        mealPlanDao.insertMealPlan(plan)
    }

    suspend fun deleteMealPlan(id: Int) {
        mealPlanDao.deleteMealPlan(id)
    }

    suspend fun saveWorkoutPlan(plan: SavedWorkoutPlan) {
        workoutPlanDao.insertWorkoutPlan(plan)
    }

    suspend fun saveFocusSession(session: FocusSession) {
        focusDao.insertFocusSession(session)
    }
}
