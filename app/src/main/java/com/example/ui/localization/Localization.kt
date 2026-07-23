package com.example.ui.localization

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

enum class AppLanguage(val code: String, val label: String) {
    ARABIC("ar", "العربية"),
    ENGLISH("en", "English")
}

object LanguageState {
    var currentLanguage by mutableStateOf(AppLanguage.ARABIC)

    fun toggleLanguage() {
        currentLanguage = if (currentLanguage == AppLanguage.ARABIC) AppLanguage.ENGLISH else AppLanguage.ARABIC
    }

    val isArabic: Boolean
        get() = currentLanguage == AppLanguage.ARABIC
}

data class TranslationStrings(
    val appTitle: String,
    val homeTab: String,
    val mealsTab: String,
    val workoutsTab: String,
    val supplementsTab: String,
    val cardioTab: String,
    
    // Calorie & Dashboard
    val dailyOverview: String,
    val calories: String,
    val caloriesRemaining: String,
    val caloriesEaten: String,
    val protein: String,
    val carbs: String,
    val fats: String,
    val stepProgress: String,
    val steps: String,
    val distance: String,
    val quickAddMeal: String,
    val quickAddMealHint: String,
    val aiCalorieEstimate: String,
    val todayMeals: String,
    val addWater: String,
    val waterGoal: String,
    
    // Meals & Bulking/Cutting
    val mealPlannerTitle: String,
    val bulking: String,
    val cutting: String,
    val maintenance: String,
    val generateMealPlanBtn: String,
    val targetCaloriesLabel: String,
    val mealBreakfast: String,
    val mealLunch: String,
    val mealDinner: String,
    val mealSnack: String,
    val ingredients: String,
    val savePlan: String,
    
    // Workouts & Boxing
    val workoutTitle: String,
    val weightliftingTab: String,
    val boxingTab: String,
    val generateWorkoutBtn: String,
    val boxingRounds: String,
    val roundTimer: String,
    val startRoundBtn: String,
    val pauseRoundBtn: String,
    val boxingCombos: String,
    val heavyBagDrills: String,
    val shadowboxing: String,
    val footworkMitts: String,
    
    // Supplements & Hormones
    val suppTitle: String,
    val suppChatTitle: String,
    val AskAiPromptHint: String,
    val hormoneSafetyGuide: String,
    val hormoneWarningText: String,
    val buildSuppProtocol: String,
    val creatineGuide: String,
    val wheyProteinGuide: String,
    val preworkoutGuide: String,
    
    // Riyadh Cardio & Focus Blocker
    val riyadhCardioTitle: String,
    val searchSpotsHint: String,
    val focusBlockerTitle: String,
    val startFocusMode: String,
    val focusMealTime: String,
    val focusWorkoutTime: String,
    val focusCardioTime: String,
    val appsBlockedNotice: String,
    val focusTimerRunning: String,
    val exitFocusMode: String,
    val openMapDirections: String,
    val facilitiesLabel: String
)

val ArabicStrings = TranslationStrings(
    appTitle = "FitAI | التغذية والملاكمة",
    homeTab = "السعرات",
    mealsTab = "التغذية",
    workoutsTab = "التمارين",
    supplementsTab = "المكملات والهرمونات",
    cardioTab = "الكارديو والتركيز",
    
    dailyOverview = "الملخص اليومي للسعرات",
    calories = "سعرة حرارية",
    caloriesRemaining = "المتبقي",
    caloriesEaten = "المستهلك",
    protein = "بروتين",
    carbs = "كارب",
    fats = "دهون",
    stepProgress = "تقدم الخطوات اليومي",
    steps = "خطوة",
    distance = "كم",
    quickAddMeal = "إضافة وجبة بالذكاء الاصطناعي",
    quickAddMealHint = "اكتب ما أكلته (مثال: صحن كبسة دجاج 300 جرام وسلطة)...",
    aiCalorieEstimate = "تحليل السعرات بالذكاء الاصطناعي",
    todayMeals = "وجبات اليوم المسجلة",
    addWater = "إضافة ماء",
    waterGoal = "هدف الماء",
    
    mealPlannerTitle = "جدول التغذية للتضخيم والتنشيف",
    bulking = "تضخيم",
    cutting = "تنشيف",
    maintenance = "محافظة",
    generateMealPlanBtn = "توليد جدول غذائي بالذكاء الاصطناعي",
    targetCaloriesLabel = "السعرات المستهدفة",
    mealBreakfast = "فطور صحي",
    mealLunch = "غداء متوازن",
    mealDinner = "عشاء خفيف",
    mealSnack = "سناك مغذي",
    ingredients = "المكونات والقيمة الغذائية",
    savePlan = "حفظ الخطة الغذائية",
    
    workoutTitle = "تمارين الحديد والملاكمة",
    weightliftingTab = "تمارين المقاومة",
    boxingTab = "تمارين الملاكمة",
    generateWorkoutBtn = "إنشاء خطة تمرين بالذكاء الاصطناعي",
    boxingRounds = "جولات الملاكمة",
    roundTimer = "مؤقت الجولات (Round Timer)",
    startRoundBtn = "ابدأ الجولة",
    pauseRoundBtn = "إيقاف مؤقت",
    boxingCombos = "كومبوهات ومجموعات لكمات",
    heavyBagDrills = "تمارين الكيس الثقيل",
    shadowboxing = "الملاكمة الوهمية (Shadowboxing)",
    footworkMitts = "حركات القدمين والميتس",
    
    suppTitle = "مستشار المكملات والبروتوكولات الهرمونية",
    suppChatTitle = "استشر الذكاء الاصطناعي عن المكملات والهرمونات",
    AskAiPromptHint = "اسأل عن الكرياتين، البروتين، أو التثقيف الصحي للهرمونات والسلامة...",
    hormoneSafetyGuide = "إرشادات السلامة والحد من المخاطر الهرمونية",
    hormoneWarningText = "⚠️ إخلاء مسؤولية صحي: المركبات الهرمونية تحمل مخاطر صحية عالية على الكبد والدورة الدموية. التطبيق يقدم معلومات تثقيفية وحماية صحية فقط.",
    buildSuppProtocol = "إنشاء خطة مكملات مخصصة",
    creatineGuide = "دليل الكرياتين مونوهيدرات",
    wheyProteinGuide = "دليل الواي بروتين والمستخلص",
    preworkoutGuide = "دليل محفزات الطاقة قبل التمرين",
    
    riyadhCardioTitle = "أفضل مواقع الكارديو في الرياض",
    searchSpotsHint = "ابحث عن حدائق أو مسارات مشي في الرياض...",
    focusBlockerTitle = "وضع التركيز وحظر المشتتات",
    startFocusMode = "تفعيل وضع التركيز وحظر التطبيقات",
    focusMealTime = "وقت الوجبة الغذائية",
    focusWorkoutTime = "وقت تمرين الحديد/الملاكمة",
    focusCardioTime = "وقت الكارديو والمشي",
    appsBlockedNotice = "🔒 تم إغلاق التطبيقات المشتتة مؤقتاً لضمان تركيزك التام",
    focusTimerRunning = "وضع التركيز نشط الآن...",
    exitFocusMode = "إنهاء جلسة التركيز",
    openMapDirections = "فتح الموقع في خرائط Google",
    facilitiesLabel = "المميزات والخدمات"
)

val EnglishStrings = TranslationStrings(
    appTitle = "FitAI | Boxing & Calories",
    homeTab = "Calories",
    mealsTab = "Nutrition",
    workoutsTab = "Workouts",
    supplementsTab = "Supplements",
    cardioTab = "Cardio & Focus",
    
    dailyOverview = "Daily Calorie Overview",
    calories = "kcal",
    caloriesRemaining = "Remaining",
    caloriesEaten = "Consumed",
    protein = "Protein",
    carbs = "Carbs",
    fats = "Fats",
    stepProgress = "Daily Step Progress",
    steps = "steps",
    distance = "km",
    quickAddMeal = "AI Quick Meal Logging",
    quickAddMealHint = "Type what you ate (e.g. 300g grilled chicken, rice, salad)...",
    aiCalorieEstimate = "AI Calorie & Macro Estimator",
    todayMeals = "Today's Logged Meals",
    addWater = "Add Water",
    waterGoal = "Water Goal",
    
    mealPlannerTitle = "Bulking & Cutting Meal Plans",
    bulking = "Bulking",
    cutting = "Cutting",
    maintenance = "Maintenance",
    generateMealPlanBtn = "Generate AI Meal Plan",
    targetCaloriesLabel = "Target Calories",
    mealBreakfast = "Breakfast",
    mealLunch = "Lunch",
    mealDinner = "Dinner",
    mealSnack = "Nutritious Snack",
    ingredients = "Ingredients & Macros",
    savePlan = "Save Meal Plan",
    
    workoutTitle = "Gym & Boxing Workouts",
    weightliftingTab = "Weightlifting",
    boxingTab = "Boxing Drills",
    generateWorkoutBtn = "Generate AI Workout Routine",
    boxingRounds = "Boxing Rounds",
    roundTimer = "Round Timer",
    startRoundBtn = "Start Round",
    pauseRoundBtn = "Pause",
    boxingCombos = "Punch Combinations",
    heavyBagDrills = "Heavy Bag Drills",
    shadowboxing = "Shadowboxing Routine",
    footworkMitts = "Footwork & Mitt Work",
    
    suppTitle = "AI Supplement & Protocol Advisor",
    suppChatTitle = "Chat with AI about Supplements & Protocols",
    AskAiPromptHint = "Ask about Creatine, Whey, or Hormone safety & health awareness...",
    hormoneSafetyGuide = "Safety Guidelines & Harm Reduction",
    hormoneWarningText = "⚠️ Health Disclaimer: Anabolic substances carry severe health risks to cardiovascular and liver systems. Content is for educational and harm reduction guidance only.",
    buildSuppProtocol = "Generate Supplement Protocol",
    creatineGuide = "Creatine Monohydrate Deep Dive",
    wheyProteinGuide = "Whey Isolate Guide",
    preworkoutGuide = "Pre-Workout Energy Guide",
    
    riyadhCardioTitle = "Best Cardio Spots in Riyadh",
    searchSpotsHint = "Search Riyadh parks or running trails...",
    focusBlockerTitle = "Focus Mode & Distraction Blocker",
    startFocusMode = "Activate Focus & Block Apps",
    focusMealTime = "Meal Focus Time",
    focusWorkoutTime = "Workout/Boxing Time",
    focusCardioTime = "Cardio/Running Time",
    appsBlockedNotice = "🔒 Distracting apps temporarily blocked for maximum focus",
    focusTimerRunning = "Focus Mode Active...",
    exitFocusMode = "End Focus Session",
    openMapDirections = "Open Directions in Google Maps",
    facilitiesLabel = "Facilities & Lighting"
)

val LocalTranslation = staticCompositionLocalOf { ArabicStrings }

@Composable
fun ProvideTranslation(content: @Composable () -> Unit) {
    val strings = if (LanguageState.isArabic) ArabicStrings else EnglishStrings
    CompositionLocalProvider(LocalTranslation provides strings) {
        content()
    }
}
