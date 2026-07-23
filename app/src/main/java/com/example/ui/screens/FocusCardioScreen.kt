package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.localization.LanguageState
import com.example.ui.localization.LocalTranslation
import com.example.ui.theme.*
import com.example.ui.viewmodels.FocusCardioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusCardioScreen(viewModel: FocusCardioViewModel) {
    val context = LocalContext.current
    val strings = LocalTranslation.current
    val spotsList = viewModel.spotsList

    // Focus State
    val isFocusActive by viewModel.isFocusActive
    val focusType by viewModel.focusType
    val secondsLeft by viewModel.secondsLeft
    val blockedAppsCount by viewModel.blockedAppsCount

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Riyadh Cardio Spots, 1: Focus Blocker Mode

    if (isFocusActive) {
        // Fullscreen Active Focus Mode Overlay Screen
        FocusBlockerActiveOverlay(viewModel = viewModel)
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(if (selectedTab == 0) strings.riyadhCardioTitle else strings.focusBlockerTitle, fontWeight = FontWeight.Bold, fontSize = 17.sp) },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Main Switcher Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = AthleticOrange
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("أماكن الكارديو بالرياض 🏃", fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("حظر المشتتات والتركيز 🔒", fontWeight = FontWeight.Bold) }
                    )
                }

                if (selectedTab == 0) {
                    // Riyadh Cardio Spots Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Riyadh Hero Image
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Box(modifier = Modifier.height(130.dp)) {
                                    Image(
                                        painter = painterResource(id = R.drawable.hero_riyadh_1784767420395),
                                        contentDescription = "Riyadh Cardio",
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
                                        Text("أفضل مسارات المشي والجري في الرياض", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text("مضامير احترافية، إنارة ليلية، وخدمات متكاملة", color = Color.LightGray, fontSize = 12.sp)
                                    }
                                }
                            }
                        }

                        // Search Bar
                        item {
                            OutlinedTextField(
                                value = viewModel.searchQuery.value,
                                onValueChange = { viewModel.searchQuery.value = it },
                                placeholder = { Text(strings.searchSpotsHint, fontSize = 12.sp) },
                                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = AthleticOrange) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("riyadh_search_input"),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Spots List
                        items(spotsList) { spot ->
                            val name = if (LanguageState.isArabic) spot.nameAr else spot.nameEn
                            val area = if (LanguageState.isArabic) spot.areaAr else spot.areaEn
                            val desc = if (LanguageState.isArabic) spot.descriptionAr else spot.descriptionEn
                            val surface = if (LanguageState.isArabic) spot.surfaceTypeAr else spot.surfaceTypeEn
                            val features = if (LanguageState.isArabic) spot.featuresAr else spot.featuresEn

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                            Text(area, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                                        }
                                        Surface(
                                            color = EmeraldGreen.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(Icons.Default.DirectionsRun, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(14.dp))
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(spot.trackLength, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(desc, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("المضمار: $surface", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = CyanAccent)

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = { viewModel.launchGoogleMapsDirections(context, spot) },
                                            colors = ButtonDefaults.buttonColors(containerColor = AthleticOrange, contentColor = Color.Black),
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.testTag("launch_maps_directions")
                                        ) {
                                            Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(strings.openMapDirections, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Star, contentDescription = null, tint = AthleticOrange, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text("${spot.rating}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Focus Blocker Setup Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AthleticOrange)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Lock, contentDescription = null, tint = AthleticOrange, modifier = Modifier.size(28.dp))
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text("وضع التركيز وإغلاق المشتتات", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        "يقوم النظام بحظر التطبيقات المشتتة (وسائل التواصل، الألعاب، الإشعارات) تلقائياً أثناء وقت وجبتك أو تمرينك أو جولة الكارديو لضمان أقصى أداء وتركيز ذهني.",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("اختر النشاط الحالي للتركيز:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        FocusTypeChip("MEAL", strings.focusMealTime, viewModel.focusType.value == "MEAL", AthleticOrange) { viewModel.focusType.value = "MEAL" }
                                        FocusTypeChip("WORKOUT", strings.focusWorkoutTime, viewModel.focusType.value == "WORKOUT", CyanAccent) { viewModel.focusType.value = "WORKOUT" }
                                        FocusTypeChip("CARDIO", strings.focusCardioTime, viewModel.focusType.value == "CARDIO", EmeraldGreen) { viewModel.focusType.value = "CARDIO" }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text("حدد مدة الجلسة (بالدقائق):", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        listOf(15, 30, 45, 60).forEach { mins ->
                                            FilterChip(
                                                selected = viewModel.focusDurationMinutes.value == mins,
                                                onClick = { viewModel.focusDurationMinutes.value = mins },
                                                label = { Text("$mins دقيقة") }
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(20.dp))

                                    Button(
                                        onClick = { viewModel.startFocusSession(viewModel.focusType.value, viewModel.focusDurationMinutes.value) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .testTag("start_focus_session_btn"),
                                        colors = ButtonDefaults.buttonColors(containerColor = AthleticOrange, contentColor = Color.Black),
                                        shape = RoundedCornerShape(14.dp)
                                    ) {
                                        Icon(Icons.Default.Security, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(strings.startFocusMode, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    }
                                }
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("قائمة التطبيقات المغلقة تلقائياً عند بدء التركيز:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        BlockedAppBadge("TikTok")
                                        BlockedAppBadge("Instagram")
                                        BlockedAppBadge("Snapchat")
                                        BlockedAppBadge("YouTube")
                                        BlockedAppBadge("Games")
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
fun FocusBlockerActiveOverlay(viewModel: FocusCardioViewModel) {
    val strings = LocalTranslation.current
    val secondsLeft by viewModel.secondsLeft

    val minutes = secondsLeft / 60
    val secs = secondsLeft % 60
    val timeStr = String.format("%02d:%02d", minutes, secs)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = CircleShape,
                color = AthleticOrange.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(2.dp, AthleticOrange),
                modifier = Modifier.size(100.dp)
            ) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = AthleticOrange,
                    modifier = Modifier.padding(24.dp).fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = strings.focusTimerRunning,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = timeStr,
                fontSize = 64.sp,
                fontWeight = FontWeight.ExtraBold,
                color = AthleticOrange
            )

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = EmeraldGreen.copy(alpha = 0.15f)
            ) {
                Text(
                    text = strings.appsBlockedNotice,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { viewModel.stopFocusSession() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(48.dp)
                    .testTag("exit_focus_mode_btn")
            ) {
                Icon(Icons.Default.PowerSettingsNew, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(strings.exitFocusMode, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun FocusTypeChip(type: String, label: String, isSelected: Boolean, color: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) color else MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) color else DarkCardBorder)
    ) {
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun BlockedAppBadge(appName: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.background,
        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder)
    ) {
        Text(appName, fontSize = 11.sp, color = Color.Gray, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
    }
}
