package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.localization.ProvideTranslation
import com.example.ui.navigation.MainNavigationShell
import com.example.ui.theme.FitAiTheme
import com.example.ui.viewmodels.*

class MainActivity : ComponentActivity() {

    private lateinit var repository: AppRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Database & Repository Initialization
        val db = AppDatabase.getDatabase(applicationContext)
        repository = AppRepository(db)

        // ViewModels
        val calorieVm = CalorieViewModel(repository)
        val mealVm = MealPlanViewModel(repository)
        val workoutVm = WorkoutViewModel(repository)
        val suppVm = SupplementViewModel()
        val focusVm = FocusCardioViewModel(repository)

        setContent {
            FitAiTheme(darkTheme = true) {
                ProvideTranslation {
                    MainNavigationShell(
                        calorieVm = calorieVm,
                        mealVm = mealVm,
                        workoutVm = workoutVm,
                        suppVm = suppVm,
                        focusVm = focusVm
                    )
                }
            }
        }
    }
}
