package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        FoodLog::class,
        UserGoal::class,
        StepLog::class,
        SavedMealPlan::class,
        SavedWorkoutPlan::class,
        FocusSession::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun goalDao(): GoalDao
    abstract fun stepDao(): StepDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun workoutPlanDao(): WorkoutPlanDao
    abstract fun focusDao(): FocusDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitai_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
