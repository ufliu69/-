package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.localization.LanguageState
import com.example.ui.localization.LocalTranslation
import com.example.ui.theme.*
import com.example.ui.viewmodels.MealPlanViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealPlanScreen(viewModel: MealPlanViewModel) {
    val strings = LocalTranslation.current
    val generatedMeals by viewModel.generatedMeals
    val isGenerating by viewModel.isGenerating
    val savedPlans by viewModel.savedMealPlans.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: AI Generator, 1: Saved Plans

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.mealPlannerTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    IconButton(onClick = { LanguageState.toggleLanguage() }) {
                        Surface(
                            shape = androidx.compose.foundation.shape.CircleShape,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Selector
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = AthleticOrange
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("مولد الخطط الذكي", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("الخطط المحفوظة (${savedPlans.size})", fontWeight = FontWeight.Bold) }
                )
            }

            if (selectedTab == 0) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Hero Image Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(modifier = Modifier.height(140.dp)) {
                                Image(
                                    painter = painterResource(id = R.drawable.hero_nutrition_1784767405877),
                                    contentDescription = "Nutrition Hero",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(14.dp)
                                ) {
                                    Text("تغذية احترافية للتضخيم والتنشيف", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text("جدول بأصناف محلية وقياسات دقيقة للمكروز", color = Color.LightGray, fontSize = 12.sp)
                                }
                            }
                        }
                    }

                    // Goal Type Selector
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("اختر الهدف الغذائي:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    GoalChip(
                                        title = strings.bulking,
                                        isSelected = viewModel.selectedGoalType.value == "BULKING",
                                        onClick = { viewModel.setGoal("BULKING") },
                                        color = AthleticOrange,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GoalChip(
                                        title = strings.cutting,
                                        isSelected = viewModel.selectedGoalType.value == "CUTTING",
                                        onClick = { viewModel.setGoal("CUTTING") },
                                        color = CyanAccent,
                                        modifier = Modifier.weight(1f)
                                    )
                                    GoalChip(
                                        title = strings.maintenance,
                                        isSelected = viewModel.selectedGoalType.value == "MAINTENANCE",
                                        onClick = { viewModel.setGoal("MAINTENANCE") },
                                        color = EmeraldGreen,
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(14.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("${strings.targetCaloriesLabel}:", fontSize = 13.sp)
                                    Text("${viewModel.targetCalories.value} ${strings.calories}", fontWeight = FontWeight.ExtraBold, color = AthleticOrange)
                                }

                                Button(
                                    onClick = { viewModel.generateMealPlan(if (LanguageState.isArabic) "Arabic" else "English") },
                                    enabled = !isGenerating,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp)
                                        .testTag("generate_meal_plan_btn"),
                                    colors = ButtonDefaults.buttonColors(containerColor = AthleticOrange, contentColor = Color.Black),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    if (isGenerating) {
                                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                                        Spacer(modifier = Modifier.width(8.dp))
                                    } else {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Text(strings.generateMealPlanBtn, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Generated Meals Result
                    if (generatedMeals.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("الجدول الغذائي المقترح اليومي", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Button(
                                    onClick = { viewModel.saveCurrentMealPlan() },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Bookmark, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(strings.savePlan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        items(generatedMeals) { meal ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Surface(
                                            color = AthleticOrange.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Text(
                                                text = meal.mealType,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = AthleticOrange,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                        Text(text = "${meal.calories} ${strings.calories}", fontWeight = FontWeight.Bold, color = AthleticOrange)
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = meal.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(text = "${strings.ingredients}: ${meal.ingredients}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        Text("${meal.protein}g ${strings.protein}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AthleticOrange)
                                        Text("${meal.carbs}g ${strings.carbs}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent)
                                        Text("${meal.fats}g ${strings.fats}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Saved Plans List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (savedPlans.isEmpty()) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                                Text("لا توجد خطط محفوظة حتى الآن.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                            }
                        }
                    } else {
                        items(savedPlans, key = { it.id }) { plan ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(plan.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                        Text("هدف: ${plan.goalType} - ${plan.targetCalories} سعرة", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                    }
                                    IconButton(onClick = { viewModel.deletePlan(plan.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GoalChip(title: String, isSelected: Boolean, onClick: () -> Unit, color: Color, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) color else DarkCardBorder),
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
