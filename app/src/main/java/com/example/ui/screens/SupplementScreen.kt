package com.example.ui.screens

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.localization.LanguageState
import com.example.ui.localization.LocalTranslation
import com.example.ui.theme.*
import com.example.ui.viewmodels.SupplementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplementScreen(viewModel: SupplementViewModel) {
    val strings = LocalTranslation.current
    var userPrompt by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) } // 0: AI Chat, 1: Protocols Timeline, 2: Safety Guide

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.suppTitle, fontWeight = FontWeight.Bold, fontSize = 17.sp) },
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
            // Tabs Header
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = AthleticOrange
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("المحادثة الذكية", fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("جدول المكملات", fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("توعية السلامة ⚠️", fontWeight = FontWeight.Bold) })
            }

            when (selectedTab) {
                0 -> {
                    // Chat Tab
                    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                        // Quick Query Chips
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SuggestionChip(
                                onClick = { userPrompt = "ما هي جرعة الكرياتين وأفضل وقت لتناوله؟" },
                                label = { Text("الكرياتين", fontSize = 11.sp) }
                            )
                            SuggestionChip(
                                onClick = { userPrompt = "ما فرق الواي بروتين أيزوليت عن الكونسينتريت؟" },
                                label = { Text("الواي بروتين", fontSize = 11.sp) }
                            )
                            SuggestionChip(
                                onClick = { userPrompt = "ما هي إرشادات السلامة والفحوصات الطبية الدورية لصحية الكبد والقلب؟" },
                                label = { Text("السلامة والفحوصات", fontSize = 11.sp) }
                            )
                        }

                        // Chat Messages List
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(viewModel.chatMessages) { msg ->
                                val isUser = msg.sender == "USER"
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                                ) {
                                    Surface(
                                        color = if (isUser) AthleticOrange else MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(16.dp),
                                        border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline) else null,
                                        modifier = Modifier.widthIn(max = 300.dp)
                                    ) {
                                        Text(
                                            text = msg.text,
                                            fontSize = 13.sp,
                                            color = if (isUser) Color.Black else MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(12.dp)
                                        )
                                    }
                                }
                            }

                            if (viewModel.isAiThinking.value) {
                                item {
                                    Row(horizontalArrangement = Arrangement.Start) {
                                        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp)) {
                                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = AthleticOrange)
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text("جاري تحضير الإجابة العلمية...", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Input Box
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = userPrompt,
                                onValueChange = { userPrompt = it },
                                placeholder = { Text(strings.AskAiPromptHint, fontSize = 12.sp) },
                                modifier = Modifier.weight(1f).testTag("supp_chat_input"),
                                shape = RoundedCornerShape(12.dp),
                                maxLines = 2
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    if (userPrompt.isNotBlank()) {
                                        viewModel.sendMessage(userPrompt, if (LanguageState.isArabic) "Arabic" else "English")
                                        userPrompt = ""
                                    }
                                },
                                modifier = Modifier.testTag("supp_send_btn")
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = AthleticOrange, modifier = Modifier.size(28.dp))
                            }
                        }
                    }
                }

                1 -> {
                    // Protocol Timeline Tab
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("إنشاء بروتوكول مكملات مخصص", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Button(
                                            onClick = { viewModel.generateCustomProtocol("BULKING", if (LanguageState.isArabic) "Arabic" else "English") },
                                            colors = ButtonDefaults.buttonColors(containerColor = AthleticOrange),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("بروتوكول التضخيم", fontSize = 12.sp, color = Color.Black)
                                        }
                                        Button(
                                            onClick = { viewModel.generateCustomProtocol("CUTTING", if (LanguageState.isArabic) "Arabic" else "English") },
                                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                                            shape = RoundedCornerShape(10.dp)
                                        ) {
                                            Text("بروتوكول التنشيف", fontSize = 12.sp, color = Color.Black)
                                        }
                                    }
                                }
                            }
                        }

                        items(viewModel.generatedProtocol.value) { item ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Surface(color = AthleticOrange.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                                        Text(item.timeOfDay, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = AthleticOrange, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(item.name, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text("الجرعة: ${item.dosage}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = EmeraldGreen)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("الهدف: ${item.purpose}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                                    Text("💡 ملاحظة: ${item.notes}", fontSize = 11.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Hormones Safety & Harm Reduction Guide Tab
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(16.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Warning, contentDescription = "Warning", tint = MaterialTheme.colorScheme.error)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("تثقيف سلامة الهرمونات والحد من المخاطر", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, fontSize = 15.sp)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        strings.hormoneWarningText,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
                                    )
                                }
                            }
                        }

                        item {
                            SafetyGuidelineCard(
                                title = "1. فحوصات الدم الطبية الدورية (Bloodwork Checklist)",
                                description = "قبل أو بعد استخدام أي مواد، يجب إجراء فحوصات شاملة تشمل:\n• وظائف الكبد (ALT/AST)\n• الكوليسترول والدهون النافعة/الضارة (HDL/LDL)\n• صورة الدم الشاملة (CBC / Hematocrit)\n• هرمونات الذكورة والغدة (Total/Free Testosterone, Estradiol, SHBG, LH/FSH)"
                            )
                        }

                        item {
                            SafetyGuidelineCard(
                                title = "2. حماية صحة القلب والأوعية الدموية",
                                description = "المركبات البنائية قد تؤدي لارتفاع ضغط الدم وزيادة سمك عضلة القلب. يوصى بمواظبة ممارسة الكارديو (Zone 2) وتناول الأوميغا 3 والمواظبة على قياس الضغط يومياً."
                            )
                        }

                        item {
                            SafetyGuidelineCard(
                                title = "3. دعم صحة الكبد والاستشفاء (Liver Support)",
                                description = "المركبات الفموية تسبب إجهاداً شديداً للكبد. يجب تناول مكملات حماية الكبد مثل TUDCA و NAC وتجنب استهلاكها كلياً دون إشراف طبي."
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SafetyGuidelineCard(title: String, description: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = AthleticOrange)
            Spacer(modifier = Modifier.height(6.dp))
            Text(description, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
        }
    }
}
