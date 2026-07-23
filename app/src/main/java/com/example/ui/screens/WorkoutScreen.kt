package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.ui.viewmodels.WorkoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel) {
    val strings = LocalTranslation.current
    val boxingRounds by viewModel.boxingRounds
    val gymExercises by viewModel.gymExercises
    val isGenerating by viewModel.isGenerating

    // Timer States
    val isTimerRunning by viewModel.isTimerRunning
    val currentRoundIndex by viewModel.currentRoundIndex
    val isRestPeriod by viewModel.isRestPeriod
    val secondsRemaining by viewModel.secondsRemaining

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.workoutTitle, fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                actions = {
                    IconButton(onClick = { LanguageState.toggleLanguage() }) {
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
            // Hero Boxing Banner
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(modifier = Modifier.height(140.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.hero_boxing_1784767395334),
                            contentDescription = "Boxing Hero",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(androidx.compose.ui.graphics.Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))))
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(14.dp)
                        ) {
                            Text("تمارين الملاكمة واللياقة البدنية الشاملة", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("مؤقت جولات محترف مع كومبوهات الملاكمة وتدريبات الكيس الثقيل", color = Color.LightGray, fontSize = 12.sp)
                        }
                    }
                }
            }

            // Mode Switcher (Boxing vs Gym)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { viewModel.setWorkoutMode("BOXING") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_boxing_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.selectedMode.value == "BOXING") AthleticOrange else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.SportsMma, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.boxingTab, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.setWorkoutMode("GYM") },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("mode_gym_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (viewModel.selectedMode.value == "GYM") CyanAccent else MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = if (viewModel.selectedMode.value == "GYM") Color.Black else Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.weightliftingTab, fontWeight = FontWeight.Bold, color = if (viewModel.selectedMode.value == "GYM") Color.Black else Color.White)
                    }
                }
            }

            if (viewModel.selectedMode.value == "BOXING") {
                // Boxing Interactive Round Timer Component
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, if (isRestPeriod) CyanAccent else AthleticOrange)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isRestPeriod) CyanAccent.copy(alpha = 0.2f) else AthleticOrange.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = if (isRestPeriod) "راحة بين الجولات (REST)" else "الجولة ${currentRoundIndex + 1} / ${if (boxingRounds.isEmpty()) 5 else boxingRounds.size} (FIGHT)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isRestPeriod) CyanAccent else AthleticOrange,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Timer Display
                            val minutes = secondsRemaining / 60
                            val secs = secondsRemaining % 60
                            val timeStr = String.format("%02d:%02d", minutes, secs)

                            Text(
                                text = timeStr,
                                fontSize = 52.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isRestPeriod) CyanAccent else AthleticOrange
                            )

                            val activeRound = boxingRounds.getOrNull(currentRoundIndex)
                            if (activeRound != null && !isRestPeriod) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.background
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "كومبو الجولة: ${activeRound.comboCue}", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = EmeraldGreen)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = activeRound.instructions, fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Timer Control Buttons
                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Button(
                                    onClick = {
                                        if (isTimerRunning) viewModel.pauseBoxingTimer() else viewModel.startBoxingTimer()
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (isTimerRunning) MaterialTheme.colorScheme.error else EmeraldGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("boxing_timer_start_pause")
                                ) {
                                    Icon(if (isTimerRunning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isTimerRunning) strings.pauseRoundBtn else strings.startRoundBtn, fontWeight = FontWeight.Bold)
                                }

                                OutlinedButton(
                                    onClick = { viewModel.resetBoxingTimer() },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("boxing_timer_reset")
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("إعادة ضبط")
                                }
                            }
                        }
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("جدول جولات الملاكمة والكومبوهات", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Button(
                            onClick = { viewModel.generateWorkoutWithAi(if (LanguageState.isArabic) "Arabic" else "English") },
                            colors = ButtonDefaults.buttonColors(containerColor = AthleticOrange, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            if (isGenerating) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.Black)
                            } else {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("توليد جولات بالذكاء الإصطناعي", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                items(boxingRounds) { round ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("جولة ${round.roundNumber}: ${round.title}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = AthleticOrange.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "🥊 Combo: ${round.comboCue}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AthleticOrange,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = round.instructions, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                        }
                    }
                }
            } else {
                // Gym Weightlifting Exercises
                item {
                    Button(
                        onClick = { viewModel.generateWorkoutWithAi(if (LanguageState.isArabic) "Arabic" else "English") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.Black)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.Black)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(strings.generateWorkoutBtn, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }

                items(gymExercises) { ex ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = ex.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Surface(shape = RoundedCornerShape(6.dp), color = CyanAccent.copy(alpha = 0.2f)) {
                                    Text(ex.setsReps, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CyanAccent, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Surface(shape = RoundedCornerShape(6.dp), color = EmeraldGreen.copy(alpha = 0.2f)) {
                                    Text("راحة ${ex.restSec} ثانية", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = "💡 نصيحة التكنيك: ${ex.tips}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
