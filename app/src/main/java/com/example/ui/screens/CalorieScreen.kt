package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.LanguageState
import com.example.ui.localization.LocalTranslation
import com.example.ui.theme.*
import com.example.ui.viewmodels.CalorieViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalorieScreen(viewModel: CalorieViewModel) {
    val strings = LocalTranslation.current
    val todayLogs by viewModel.todayFoodLogs.collectAsState()
    val goal by viewModel.userGoal.collectAsState()
    val stepLog by viewModel.todayStepLog.collectAsState()

    var quickMealText by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("Breakfast") }
    var showAddDialog by remember { mutableStateOf(false) }

    val totalCalories = todayLogs.sumOf { it.calories }
    val totalProtein = todayLogs.sumOf { it.protein }
    val totalCarbs = todayLogs.sumOf { it.carbs }
    val totalFats = todayLogs.sumOf { it.fats }

    val calsRemaining = (goal.calorieGoal - totalCalories).coerceAtLeast(0)
    val progress = (totalCalories.toFloat() / goal.calorieGoal.toFloat()).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(1000), label = "CalorieProgress")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocalFireDepartment,
                            contentDescription = "Calories",
                            tint = AthleticOrange,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.dailyOverview,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },
                actions = {
                    // Language Switcher Button
                    IconButton(
                        onClick = { LanguageState.toggleLanguage() },
                        modifier = Modifier.testTag("lang_toggle_button")
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = AthleticOrange.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AthleticOrange)
                        ) {
                            Text(
                                text = if (LanguageState.isArabic) "EN" else "عربي",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AthleticOrange
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Calorie Progress Ring Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(180.dp)
                                .padding(8.dp)
                        ) {
                            // Ring Canvas
                            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                drawArc(
                                    color = DarkCardBorder,
                                    startAngle = 135f,
                                    sweepAngle = 270f,
                                    useCenter = false,
                                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                                )
                                drawArc(
                                    color = AthleticOrange,
                                    startAngle = 135f,
                                    sweepAngle = 270f * animatedProgress,
                                    useCenter = false,
                                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$totalCalories",
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AthleticOrange
                                )
                                Text(
                                    text = "/ ${goal.calorieGoal} ${strings.calories}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = EmeraldGreen.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "${strings.caloriesRemaining}: $calsRemaining",
                                        fontSize = 11.sp,
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Macros Breakdown Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            MacroBarItem(
                                label = strings.protein,
                                value = "$totalProtein / ${goal.proteinGoal}g",
                                progress = totalProtein.toFloat() / goal.proteinGoal.toFloat(),
                                color = AthleticOrange
                            )
                            MacroBarItem(
                                label = strings.carbs,
                                value = "$totalCarbs / ${goal.carbsGoal}g",
                                progress = totalCarbs.toFloat() / goal.carbsGoal.toFloat(),
                                color = CyanAccent
                            )
                            MacroBarItem(
                                label = strings.fats,
                                value = "$totalFats / ${goal.fatsGoal}g",
                                progress = totalFats.toFloat() / goal.fatsGoal.toFloat(),
                                color = EmeraldGreen
                            )
                        }
                    }
                }
            }

            // 2. Steps Tracker Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = CyanAccent.copy(alpha = 0.2f),
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.DirectionsWalk,
                                    contentDescription = "Steps",
                                    tint = CyanAccent,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = strings.stepProgress,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${stepLog.steps} / ${goal.stepGoal} ${strings.steps} (${String.format("%.1f", stepLog.distanceKm)} ${strings.distance})",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.addSteps(1000) },
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("add_steps_button")
                        ) {
                            Text("+1000", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }
            }

            // 3. Quick AI Meal Logger Section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, AthleticOrange.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = AthleticOrange)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = strings.quickAddMeal,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = AthleticOrange
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = quickMealText,
                            onValueChange = { quickMealText = it },
                            placeholder = { Text(strings.quickAddMealHint, fontSize = 12.sp) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_meal_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = false,
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    if (quickMealText.isNotBlank()) {
                                        viewModel.analyzeAndAddMealWithAi(quickMealText, selectedMealType)
                                        quickMealText = ""
                                    }
                                },
                                enabled = !viewModel.isAiAnalyzing.value && quickMealText.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = AthleticOrange, contentColor = Color.Black),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("analyze_meal_btn")
                            ) {
                                if (viewModel.isAiAnalyzing.value) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.Black, strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                } else {
                                    Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(strings.aiCalorieEstimate, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            IconButton(
                                onClick = { showAddDialog = true },
                                modifier = Modifier.testTag("manual_add_meal_btn")
                            ) {
                                Icon(Icons.Default.AddCircle, contentDescription = "Manual Add", tint = EmeraldGreen, modifier = Modifier.size(32.dp))
                            }
                        }
                    }
                }
            }

            // 4. Today's Meals Header
            item {
                Text(
                    text = strings.todayMeals,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Meal Items List
            if (todayLogs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "لم تسجل أي وجبات اليوم بعد. استخدم الذكاء الاصطناعي لإضافة وجبتك الأولى!",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                items(todayLogs, key = { it.id }) { log ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = log.name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text(text = "${log.protein}g P", fontSize = 11.sp, color = AthleticOrange, fontWeight = FontWeight.Bold)
                                    Text(text = "${log.carbs}g C", fontSize = 11.sp, color = CyanAccent, fontWeight = FontWeight.Bold)
                                    Text(text = "${log.fats}g F", fontSize = 11.sp, color = EmeraldGreen, fontWeight = FontWeight.Bold)
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "${log.calories} ${strings.calories}", fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = AthleticOrange)
                                IconButton(onClick = { viewModel.deleteMeal(log.id) }) {
                                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Manual Add Dialog
    if (showAddDialog) {
        var name by remember { mutableStateOf("") }
        var cals by remember { mutableStateOf("450") }
        var prot by remember { mutableStateOf("30") }
        var carb by remember { mutableStateOf("50") }
        var fat by remember { mutableStateOf("12") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("إضافة وجبة يدوياً", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("اسم الوجبة") })
                    OutlinedTextField(value = cals, onValueChange = { cals = it }, label = { Text("السعرات") })
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = prot, onValueChange = { prot = it }, label = { Text("بروتين g") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = carb, onValueChange = { carb = it }, label = { Text("كارب g") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = fat, onValueChange = { fat = it }, label = { Text("دهون g") }, modifier = Modifier.weight(1f))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.addManualMeal(
                            name = name.ifBlank { "وجبة جديدة" },
                            calories = cals.toIntOrNull() ?: 400,
                            protein = prot.toIntOrNull() ?: 30,
                            carbs = carb.toIntOrNull() ?: 45,
                            fats = fat.toIntOrNull() ?: 12,
                            mealType = selectedMealType
                        )
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AthleticOrange)
                ) {
                    Text("حفظ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("إلغاء") }
            }
        )
    }
}

@Composable
fun MacroBarItem(label: String, value: String, progress: Float, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
        Text(text = value, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = color)
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .width(80.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = color,
            trackColor = DarkCardBorder
        )
    }
}
