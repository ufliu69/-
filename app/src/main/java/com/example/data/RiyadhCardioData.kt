package com.example.data

data class RiyadhCardioSpot(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val areaAr: String,
    val areaEn: String,
    val trackLength: String,
    val surfaceTypeAr: String,
    val surfaceTypeEn: String,
    val descriptionAr: String,
    val descriptionEn: String,
    val featuresAr: List<String>,
    val featuresEn: List<String>,
    val googleMapsQuery: String,
    val isNightLit: Boolean = true,
    val rating: Float = 4.8f
)

object RiyadhCardioRepository {
    val spots = listOf(
        RiyadhCardioSpot(
            id = "wadi_hanifah",
            nameAr = "ممشى ووادي حنيفة التاريخي",
            nameEn = "Wadi Hanifah Cardio & Nature Trail",
            areaAr = "غرب الرياض (سد العلب - البجيري)",
            areaEn = "West Riyadh (Al Ilb Dam - Diriyah)",
            trackLength = "8.5 km",
            surfaceTypeAr = "مسار مطاطي مخصص ومسارات صخرية طبيعية",
            surfaceTypeEn = "Rubberized Running Track & Natural Trails",
            descriptionAr = "من أرقى وأجمل الوجهات الطبيعية للكارديو في الرياض، تمتاز بمناظر الجداول المائية والأشجار وإمكانية الجري الطويل المفتوح.",
            descriptionEn = "One of Riyadh's top scenic nature trails featuring water streams, palm groves, and long uninterrupted running tracks.",
            featuresAr = listOf("مضمار مطاطي مريح للركب", "إنارة ليلية كاملة", "أماكن استراحة ومسارات دراجات"),
            featuresEn = listOf("Joint-friendly Rubber Track", "Full Night Lighting", "Resting Benches & Bike Paths"),
            googleMapsQuery = "Wadi Hanifah Walkway Riyadh"
        ),
        RiyadhCardioSpot(
            id = "sports_boulevard",
            nameAr = "المسار الرياضي - طريق الأمير محمد بن سلمان",
            nameEn = "Sports Boulevard Track",
            areaAr = "شمال الرياض (يمتد شرقاً وغرباً)",
            areaEn = "North Riyadh (East-West Boulevard)",
            trackLength = "12.0 km",
            surfaceTypeAr = "مضمار مشي احترافي ثلاثي المسارات",
            surfaceTypeEn = "Multi-lane Professional Athletic Track",
            descriptionAr = "مشروع عالمي مصمم بأعلى معايير الحركة الرياضية، يحتوي على مسارات منفصلة للمشي، الجري السريع، والدراجات الهوائية.",
            descriptionEn = "World-class mega sports corridor featuring dedicated segregated tracks for walking, sprinting, and cycling.",
            featuresAr = listOf("مسارات جغرافية آمنة 100%", "محطات ترطيب وتبريد", "تقنيات قياس المسافات"),
            featuresEn = listOf("100% Safe Segregated Lanes", "Hydration Stations", "Distance Markers"),
            googleMapsQuery = "Sports Boulevard Prince Mohammed Bin Salman Riyadh"
        ),
        RiyadhCardioSpot(
            id = "king_abdullah_park",
            nameAr = "منتزه الملك عبد الله بالملز",
            nameEn = "King Abdullah Park Walkway",
            areaAr = "حي الملز، وسط الرياض",
            areaEn = "Al Malaz, Central Riyadh",
            trackLength = "3.2 km",
            surfaceTypeAr = "مضمار مطاطي محيطي عريض",
            surfaceTypeEn = "Wide Perimeter Rubber Track",
            descriptionAr = "مضمار محيطي واسع يحيط بمنتزه الملك عبد الله، مجهز بالكامل للمشي العائلي والجري المسائي وسط أجواء حيوية.",
            descriptionEn = "Vibrant wide perimeter rubberized track surrounding the park, fully lit and ideal for family cardio sessions.",
            featuresAr = listOf("مضمار مطاطي عالي المرونة", "مناسب للعوائل والأطفال", "أجهزة لياقة بالهواء الطلق"),
            featuresEn = listOf("High-elasticity Rubber Track", "Family Friendly", "Outdoor Gym Stations"),
            googleMapsQuery = "King Abdullah Park Al Malaz Riyadh"
        ),
        RiyadhCardioSpot(
            id = "bujairi_heritage",
            nameAr = "مسار ممشى البجيري والدرعية",
            nameEn = "Al Bujairi Heritage Trail",
            areaAr = "الدرعية التاريخية، شمال غرب الرياض",
            areaEn = "Historic Diriyah, NW Riyadh",
            trackLength = "4.5 km",
            surfaceTypeAr = "مسار ممشى حجري وممهد",
            surfaceTypeEn = "Paved Heritage Promenade",
            descriptionAr = "تجربة كارديو ساحرة بين العمارة النجدية والتراث التاريخي لحي الطريف والبحيرات المجاورة.",
            descriptionEn = "Enchanting cardio experience surrounded by heritage Najdi architecture and serene water features.",
            featuresAr = listOf("منظر تراثي خلاب", "أمان وعناية فائقة", "كافيهات ومطاعم فاخرة"),
            featuresEn = listOf("Heritage Views", "High Security & Maintenance", "Fine Cafes"),
            googleMapsQuery = "Al Bujairi Heritage Trail Diriyah Riyadh"
        ),
        RiyadhCardioSpot(
            id = "al_nahda_park",
            nameAr = "ممشى شارع النهضة",
            nameEn = "Al Nahda Street Park Walkway",
            areaAr = "حي الروضة / الربوة، شرق الرياض",
            areaEn = "Al Rawdah / Al Rabwah, East Riyadh",
            trackLength = "5.0 km",
            surfaceTypeAr = "مضمار مشي ممهد ومحيط خضري",
            surfaceTypeEn = "Paved Greenery Strip Track",
            descriptionAr = "من أطول وأشهر المماشيل المستقيمة بشرق الرياض، مجهز بألعاب أطفال وأجهزة تمارين لياقية متفرقة.",
            descriptionEn = "One of the longest straight cardio walking strips in East Riyadh with embedded fitness equipment stations.",
            featuresAr = listOf("ممشى مستقيم طويل", "أجهزة تمارين بالهواء الطلق", "إنارة ومظلات متقطعة"),
            featuresEn = listOf("Long Straight Track", "Open Air Gym Equipment", "Shaded Rest Spots"),
            googleMapsQuery = "Al Nahda Walkway Park Riyadh"
        )
    )
}
