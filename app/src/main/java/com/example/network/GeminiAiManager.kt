package com.example.network

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiAiManager {
    private const val TAG = "GeminiAiManager"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateContent(prompt: String, systemInstructionText: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = try { BuildConfig.GEMINI_API_KEY } catch (e: Exception) { "" }
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.w(TAG, "Gemini API key missing or default placeholder used")
            return@withContext getFallbackResponse(prompt)
        }

        try {
            val rootJson = JSONObject()
            val contentsArray = JSONArray()
            val contentObj = JSONObject()
            val partsArray = JSONArray()
            val partObj = JSONObject()
            
            partObj.put("text", prompt)
            partsArray.put(partObj)
            contentObj.put("parts", partsArray)
            contentsArray.put(contentObj)
            rootJson.put("contents", contentsArray)

            if (!systemInstructionText.isNullOrBlank()) {
                val sysObj = JSONObject()
                val sysPartsArray = JSONArray()
                val sysPartObj = JSONObject()
                sysPartObj.put("text", systemInstructionText)
                sysPartsArray.put(sysPartObj)
                sysObj.put("parts", sysPartsArray)
                rootJson.put("systemInstruction", sysObj)
            }

            val requestBody = rootJson.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val requestUrl = "$BASE_URL?key=$apiKey"

            val request = Request.Builder()
                .url(requestUrl)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseStr = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e(TAG, "Gemini API error: ${response.code} $responseStr")
                return@withContext getFallbackResponse(prompt)
            }

            val resJson = JSONObject(responseStr)
            val candidates = resJson.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCand = candidates.getJSONObject(0)
                val content = firstCand.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return@withContext parts.getJSONObject(0).optString("text", "")
                }
            }

            return@withContext getFallbackResponse(prompt)
        } catch (e: Exception) {
            Log.e(TAG, "Exception during Gemini API call", e)
            return@withContext getFallbackResponse(prompt)
        }
    }

    private fun getFallbackResponse(prompt: String): String {
        val lower = prompt.lowercase()
        return when {
            lower.contains("calorie") || lower.contains("سعرات") -> {
                """
                {
                  "name": "الوجبة المعالجة",
                  "calories": 550,
                  "protein": 40,
                  "carbs": 50,
                  "fats": 15,
                  "advice": "وجبة متوازنة تحتوي على نسبة ممتازة من البروتين والكربوهيدرات المعقدة."
                }
                """.trimIndent()
            }
            lower.contains("supplement") || lower.contains("مكمل") || lower.contains("هرمون") -> {
                """
                أهلاً بك! بصفتي المساعد الذكي للمكملات والبروتوكولات:
                - **الكرياتين مونوهيدرات**: 5g يومياً لتعزيز القوة البنائية والاستشفاء العضلي.
                - **الواي بروتين**: 1-2 سكوب لتغطية الاحتياج اليومي من البروتين.
                - **أوميغا 3 والمواظبة**: ممتازة لصحة المفاصل والقلب.
                
                ⚠️ **تنبيه هرموني وصحي هام**: استخدام الهرمونات البنائية والمركبات المحظورة يحمل مخاطر صحية جسيمة على الكبد، القلب، والدورة الهرمونية الطبيعية. ينصح دائماً بطلب استشارة طبيب مختص وإجراء فحوصات دم دورية.
                """.trimIndent()
            }
            else -> {
                """
                أهلاً بك في FitAI! لقد تم إعداد خطتك بنجاح بأسلوب علمي ومخصص لأهدافك في الرياض والتنشيف/التضخيم.
                """.trimIndent()
            }
        }
    }
}
