package com.example.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ui.localization.LocalTranslation
import com.example.ui.screens.*
import com.example.ui.theme.AthleticOrange
import com.example.ui.viewmodels.*

sealed class NavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Calorie : NavItem("calorie", Icons.Default.LocalFireDepartment)
    object Meals : NavItem("meals", Icons.Default.Restaurant)
    object Workouts : NavItem("workouts", Icons.Default.SportsMma)
    object Supplements : NavItem("supplements", Icons.Default.Medication)
    object FocusCardio : NavItem("focus_cardio", Icons.Default.Lock)
}

@Composable
fun MainNavigationShell(
    calorieVm: CalorieViewModel,
    mealVm: MealPlanViewModel,
    workoutVm: WorkoutViewModel,
    suppVm: SupplementViewModel,
    focusVm: FocusCardioViewModel
) {
    val navController = rememberNavController()
    val strings = LocalTranslation.current
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: NavItem.Calorie.route

    val items = listOf(
        NavItem.Calorie to strings.homeTab,
        NavItem.Meals to strings.mealsTab,
        NavItem.Workouts to strings.workoutsTab,
        NavItem.Supplements to strings.supplementsTab,
        NavItem.FocusCardio to strings.cardioTab
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                items.forEach { (item, title) ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            if (currentRoute != item.route) {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = title,
                                tint = if (isSelected) AthleticOrange else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        label = {
                            Text(
                                text = title,
                                fontSize = 10.sp,
                                color = if (isSelected) AthleticOrange else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        modifier = Modifier.testTag("nav_item_${item.route}")
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NavItem.Calorie.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(NavItem.Calorie.route) { CalorieScreen(calorieVm) }
            composable(NavItem.Meals.route) { MealPlanScreen(mealVm) }
            composable(NavItem.Workouts.route) { WorkoutScreen(workoutVm) }
            composable(NavItem.Supplements.route) { SupplementScreen(suppVm) }
            composable(NavItem.FocusCardio.route) { FocusCardioScreen(focusVm) }
        }
    }
}
