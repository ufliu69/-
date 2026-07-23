package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_logs WHERE date = :date ORDER BY id DESC")
    fun getFoodLogsForDate(date: String): Flow<List<FoodLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFoodLog(foodLog: FoodLog)

    @Query("DELETE FROM food_logs WHERE id = :id")
    suspend fun deleteFoodLog(id: Int)

    @Query("SELECT SUM(calories) FROM food_logs WHERE date = :date")
    fun getTotalCaloriesForDate(date: String): Flow<Int?>
}

@Dao
interface GoalDao {
    @Query("SELECT * FROM user_goals WHERE id = 1")
    fun getUserGoal(): Flow<UserGoal?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUserGoal(goal: UserGoal)
}

@Dao
interface StepDao {
    @Query("SELECT * FROM step_logs WHERE date = :date")
    fun getStepLogForDate(date: String): Flow<StepLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStepLog(stepLog: StepLog)
}

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM saved_meal_plans ORDER BY timestamp DESC")
    fun getAllMealPlans(): Flow<List<SavedMealPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(plan: SavedMealPlan)

    @Query("DELETE FROM saved_meal_plans WHERE id = :id")
    suspend fun deleteMealPlan(id: Int)
}

@Dao
interface WorkoutPlanDao {
    @Query("SELECT * FROM saved_workout_plans ORDER BY timestamp DESC")
    fun getAllWorkoutPlans(): Flow<List<SavedWorkoutPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPlan(plan: SavedWorkoutPlan)

    @Query("DELETE FROM saved_workout_plans WHERE id = :id")
    suspend fun deleteWorkoutPlan(id: Int)
}

@Dao
interface FocusDao {
    @Query("SELECT * FROM focus_sessions ORDER BY timestamp DESC LIMIT 20")
    fun getRecentFocusSessions(): Flow<List<FocusSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFocusSession(session: FocusSession)
}
