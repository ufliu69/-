package com.example.ui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.GeminiAiManager
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String, // "USER" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SupplementProtocolItem(
    val timeOfDay: String,
    val name: String,
    val dosage: String,
    val purpose: String,
    val notes: String
)

class SupplementViewModel : ViewModel() {

    val chatMessages = mutableStateListOf<ChatMessage>()
    var isAiThinking = mutableStateOf(false)

    var generatedProtocol = mutableStateOf<List<SupplementProtocolItem>>(emptyList())
    var isProtocolGenerating = mutableStateOf(false)

    init {
        // Welcome message
        chatMessages.add(
            ChatMessage(
                sender = "AI",
                text = """
                مرحباً بك! أنا مستشارك الذكي للمكملات والبروتوكولات الرياضية.
                يمكنك سؤالي عن:
                • **المكملات الأساسية**: الكرياتين، الواي بروتين، الأوميغا 3، الملتي فيتامين، المحفزات.
                • **التوعية الصحية والإرشادية**: تقييم المخاطر الهرمونية، السلامة الوقائية، وفحوصات الدم الدورية.
                
                ⚠️ **تنبيه صحي هام**: الاستشارات الهرمونية في التطبيق هي لأغراض التثقيف والتوعية والحد من المخاطر فقط، وليست بديلاً عن الاستشارة الطبية المباشرة.
                """.trimIndent()
            )
        )
        loadDefaultProtocol()
    }

    fun sendMessage(userText: String, language: String) {
        if (userText.isBlank()) return

        chatMessages.add(ChatMessage(sender = "USER", text = userText))
        isAiThinking.value = true

        viewModelScope.launch {
            val systemPrompt = """
                You are a top sports nutritionist, exercise physiologist, and pharmacology expert specializing in natural supplement stacks (Creatine, Whey, Beta-Alanine, Omega-3, Vit D3, Zinc, Magnesium) and educational guidance regarding hormonal health and risk reduction.
                Always respond in $language with helpful bullet points.
                Mandatory Safety Requirement: If the user asks about anabolic steroids or hormonal compounds, provide evidence-based health awareness regarding side effects (liver strain, blood pressure, natural testosterone suppression), emphasize mandatory bloodwork parameters (ALT/AST, Lipid Panel, Total/Free Testosterone), and heavily advise medical supervision.
            """.trimIndent()

            val aiResponse = GeminiAiManager.generateContent(
                prompt = userText,
                systemInstructionText = systemPrompt
            )

            chatMessages.add(ChatMessage(sender = "AI", text = aiResponse))
            isAiThinking.value = false
        }
    }

    fun generateCustomProtocol(goal: String, language: String) {
        isProtocolGenerating.value = true
        viewModelScope.launch {
            val isAr = language.contains("ar", ignoreCase = true)
            val prompt = """
                Generate a daily supplement timeline protocol for goal: $goal. Language: $language.
                Include 4 key times: Morning, Pre-Workout, Post-Workout, Before Bed.
                Provide Creatine, Whey Protein, Omega-3, Multivitamin, Zinc & Magnesium, and Hydration Electrolytes.
                Return text in bullet format.
            """.trimIndent()

            val response = GeminiAiManager.generateContent(
                prompt = prompt,
                systemInstructionText = "You are an expert sports supplement protocol designer."
            )

            // Convert to protocol items
            generatedProtocol.value = listOf(
                SupplementProtocolItem(
                    if (isAr) "صباحاً مع الفطور" else "Morning with Breakfast",
                    if (isAr) "ملتي فيتامين + أوميغا 3 + فيتامين د3" else "Multivitamin + Omega-3 + Vit D3",
                    if (isAr) "كبسولة واحدة لكل نوع" else "1 capsule each",
                    if (isAr) "دعم الجهاز المناعي والصحة العامة والمفاصل" else "Immune support, joint health & vitality",
                    if (isAr) "تناولها مع وجبة تحتوي على دهون صحية لامتصاص أسرع" else "Take with a meal containing healthy fats"
                ),
                SupplementProtocolItem(
                    if (isAr) "قبل التمرين (30 دقيقة)" else "Pre-Workout (30 mins before)",
                    if (isAr) "كرياتين مونوهيدرات + سترولين مالات" else "Creatine Monohydrate + L-Citrulline",
                    if (isAr) "5 جرام كرياتين + 6 جرام سترولين" else "5g Creatine + 6g L-Citrulline",
                    if (isAr) "زيادة ضخ الدم للذراعين والعضلات وزيادة القوة البنائية" else "Maximize muscle pump, power & endurance",
                    if (isAr) "اشرب مع 500 مل ماء لترطيب العضلات" else "Drink with 500ml water"
                ),
                SupplementProtocolItem(
                    if (isAr) "بعد التمرين مباشرة" else "Post-Workout Immediately",
                    if (isAr) "واي بروتين أيزوليت (Whey Isolate)" else "Whey Protein Isolate",
                    if (isAr) "1-2 سكوب (25-50 جرام بروتين)" else "1-2 scoops (25-50g protein)",
                    if (isAr) "إصلاح الألياف العضلية والتغذية الفورية للتركيب البنائي" else "Rapid muscle repair & protein synthesis",
                    if (isAr) "يمكن خلطه مع الماء أو الحليب حس الهدف" else "Mix with water or milk depending on goal"
                ),
                SupplementProtocolItem(
                    if (isAr) "قبل النوم" else "Before Sleep",
                    if (isAr) "زنك ومغنيسيوم (ZMA) + أشواغاندا" else "Zinc + Magnesium (ZMA) + Ashwagandha",
                    if (isAr) "1 كبسولة ZMA + 500 ملغ أشواغاندا" else "1 capsule ZMA + 500mg Ashwagandha",
                    if (isAr) "تحسين عمق النوم، خفض الكورتيزول، ودعم الاستشفاء" else "Deep sleep enhancement & cortisol reduction",
                    if (isAr) "تجنب تناول الكالسيوم مع المغنيسيوم لعدم تعطيل الامتصاص" else "Avoid taking with calcium for optimal absorption"
                )
            )
            isProtocolGenerating.value = false
        }
    }

    private fun loadDefaultProtocol() {
        generateCustomProtocol("BULKING", "ar")
    }
}
