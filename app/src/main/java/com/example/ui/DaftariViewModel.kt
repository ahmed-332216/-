package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DaftariDatabase
import com.example.data.DaftariEntry
import com.example.data.DaftariRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DaftariViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DaftariRepository
    val allEntries: StateFlow<List<DaftariEntry>>

    private val _currentScreen = MutableStateFlow<String>("dashboard") // "dashboard", "category_X", "subpage_Y"
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    private val _activeCategory = MutableStateFlow<String>("") // Section title
    val activeCategory: StateFlow<String> = _activeCategory.asStateFlow()

    private val _activeSubpageId = MutableStateFlow<String>("") // Page ID (e.g. "diary", "budget")
    val activeSubpageId: StateFlow<String> = _activeSubpageId.asStateFlow()

    private val _navigationStack = MutableStateFlow<List<String>>(listOf("dashboard"))
    val navigationStack: StateFlow<List<String>> = _navigationStack.asStateFlow()

    init {
        val database = DaftariDatabase.getDatabase(application)
        repository = DaftariRepository(database.dao)
        allEntries = repository.getAllEntries().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Prepopulate on startup if db is empty
        viewModelScope.launch {
            allEntries.collect { list ->
                if (list.isEmpty()) {
                    prepopulateData()
                }
            }
        }
    }

    fun navigateTo(screen: String, activeCat: String = "", subpageId: String = "") {
        _currentScreen.value = screen
        if (activeCat.isNotEmpty()) _activeCategory.value = activeCat
        if (subpageId.isNotEmpty()) _activeSubpageId.value = subpageId
        
        val currentStack = _navigationStack.value.toMutableList()
        currentStack.add(screen)
        _navigationStack.value = currentStack
    }

    fun navigateBack() {
        val currentStack = _navigationStack.value.toMutableList()
        if (currentStack.size > 1) {
            currentStack.removeAt(currentStack.size - 1)
            val previousScreen = currentStack.last()
            _navigationStack.value = currentStack
            _currentScreen.value = previousScreen
            
            // Adjust states
            if (previousScreen == "dashboard") {
                _activeCategory.value = ""
                _activeSubpageId.value = ""
            } else if (previousScreen.startsWith("category_")) {
                _activeSubpageId.value = ""
            }
        } else {
            // Reached dashboard root
            _currentScreen.value = "dashboard"
            _activeCategory.value = ""
            _activeSubpageId.value = ""
        }
    }

    fun insert(entry: DaftariEntry) {
        viewModelScope.launch {
            repository.insert(entry)
        }
    }

    fun update(entry: DaftariEntry) {
        viewModelScope.launch {
            repository.update(entry)
        }
    }

    fun deleteById(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    private suspend fun prepopulateData() {
        // Pre-populating default entities
        val defaultProfile = DaftariEntry(
            category = "PROFILE",
            title = "أحمد محمد الموقر",
            content = "طالب جامعي مطور برمجيات، شغوف بتنظيم الوقت والتعلم المستمر وتجربة التقنيات الحديثة وإثراء المحتوى العربي.",
            extraInfo = "القراءة, البرمجة والتكويد, السفر والاستكشاف, الرياضة الصباحية",
            imageUrl = "img_profile_avatar"
        )
        repository.insert(defaultProfile)

        // Diary seeds
        val diarySeeds = listOf(
            DaftariEntry(category = "DIARY", title = "غروب دافئ ومثمر", content = "اليوم أنهيت جزء كبير من مشروعي البرمجي الجديد، ثم قضيت ساعة كاملة أقرأ فصولاً رائعة من كتاب العادات الذرية في الشرفة. شعور الإنجاز مذهل بعد يوم طويل من الجهد والتركيز."),
            DaftariEntry(category = "DIARY", title = "التفكير بالخطط القادمة", content = "استيقظت باكراً ومارست تمرين الجري الصباحي. الجو كان منعشاً للغاية. جلست في المقهى ودونت الخطط المهنية للشهور الثلاثة القادمة. يجب أن أستمر على هذا الانضباط.")
        )
        diarySeeds.forEach { repository.insert(it) }

        // Memories seeds
        val memoriesSeeds = listOf(
            DaftariEntry(category = "MEMORIES", title = "حفل التخرج والحصول على الشهادة", content = "اللحظة التي لطالما تطلعت إليها بصبر وتعب مع عائلتي وأصدقائي المقربين. الفرحة في عيون والدي كانت تسوى الدنيا وما فيها.", timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 30),
            DaftariEntry(category = "MEMORIES", title = "رحلة جبال اللوز الثلجية", content = "كشتة وطلعة وتخييم لا ينسى مع الشباب وسط الثلوج البيضاء والشواء على الجمر في ليلة شتوية شديدة البرودة.", timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 90)
        )
        memoriesSeeds.forEach { repository.insert(it) }

        // Future dreams seeds
        val dreamsSeeds = listOf(
            DaftariEntry(category = "DREAMS", title = "إكمال دراساتي العليا بذكاء اصطناعي", content = "الحصول على منحة لدراسة الماجستير والدكتوراه والعمل على أبحاث ونماذج لغوية كبيرة تخدم الأمة واللغة العربية."),
            DaftariEntry(category = "DREAMS", title = "تأسيس شركة برمجيات ناشئة", content = "بناء شركة تقدم حلولاً ذكية ومميزة لتسهيل حياة الناس وتحقيق التحول الرقمي الفعال.", isCompleted = false)
        )
        dreamsSeeds.forEach { repository.insert(it) }

        // Self Review seeds
        val selfReviewSeeds = listOf(
            DaftariEntry(category = "SELF_REVIEW", title = "مراجعة الذات - منتصف العام", content = "الالتزام بالرياضة: ممتاز 90%\nالاستيقاظ الباكر: جيد جداً 80%\nالتركيز على المذاكرة والبرمجة: يحتاج تحسين 70% لإكمال المشاريع وعدم التشتت بالمشتتات الرقمية.", rating = 4.5f),
            DaftariEntry(category = "SELF_REVIEW", title = "مراجعة الربع الأول", content = "تم إنجاز قراءة 3 كتب كاملة وتعلّم لغة برمجة جديدة. أحتاج تنظيم ساعات النوم وجدولتها جيداً والحد من استهلاك الكافيين.", rating = 4.0f)
        )
        selfReviewSeeds.forEach { repository.insert(it) }

        // Tasks / To Do List
        val todoSeeds = listOf(
            DaftariEntry(category = "TODO", title = "شراء المستلزمات الطبية والغذائية للبيت", content = "شراء خضروات، حليب، مكملات غذائية ومسكنات", isCompleted = false),
            DaftariEntry(category = "TODO", title = "مراجعة الكود وإصلاح الأخطاء البرمجية", content = "حل مشاكل التوافقية والتصميم في تطبيق الأندرويد", isCompleted = true),
            DaftariEntry(category = "TODO", title = "تحضير تلخيص الكواكب لعرض الغد", content = "قراءة ومراجعة الكواكب وفلاش كاردز الدرس", isCompleted = false)
        )
        todoSeeds.forEach { repository.insert(it) }

        // Planner seeds
        val plannerSeeds = listOf(
            DaftariEntry(category = "PLANNER", title = "جدول المهام اليومية المعتاد", content = "09:00 ص - مذاكرة والبرمجة\n12:00 م - رياضة وغداء صحي\n04:00 م - قراءة حرة ومراجعة فلاش كاردز\n08:00 م - مراجعة المطبخ والوجبات والراحة"),
            DaftariEntry(category = "PLANNER", title = "جلسة عصف ذهني للمشروع الجديد", content = "كتابة خطة العمل، واجهات المستخدم البدئية، وتحديد المهام الأكثر أهمية للبدء بها مباشرة")
        )
        plannerSeeds.forEach { repository.insert(it) }

        // Budget seeds
        val budgetSeeds = listOf(
            DaftariEntry(category = "BUDGET", title = "إيراد: العمل الحر البرمجي", content = "تطوير واجهات وبناء قواعد بيانات لعميل", rating = 1500f, extraInfo = "إيراد"),
            DaftariEntry(category = "BUDGET", title = "مصروف: كتب ومراجع برمجية وأقلام", content = "شراء كتب متخصصة لتطوير المهارات", rating = -120f, extraInfo = "مصروف"),
            DaftariEntry(category = "BUDGET", title = "مصروف: اشتراك النادي الرياضي", content = "تجديد اشتراك مجمع اللياقة البدنية والسباحة", rating = -300f, extraInfo = "مصروف")
        )
        budgetSeeds.forEach { repository.insert(it) }

        // Sleep seeds
        val sleepSeeds = listOf(
            DaftariEntry(category = "SLEEP", title = "نوم ليلة الجمعة", content = "نوم عميق دون استيقاظ، الشعور بالنشاط عالي جداً صباحاً", rating = 8.5f, extraInfo = "ممتاز"),
            DaftariEntry(category = "SLEEP", title = "نوم ليلة الخميس", content = "نوم متقطع قليلاً بسبب شرب شاي متأخر، استيقاظ مرهق خفيف", rating = 6.0f, extraInfo = "مقبول")
        )
        sleepSeeds.forEach { repository.insert(it) }

        // Workout seeds
        val workoutSeeds = listOf(
            DaftariEntry(category = "WORKOUT", title = "تمرين تضخيم الصدر والأكتاف", content = "بنش برس 4 جولات × 12 تكرار\nتفتيح صدر دمبلز 3 جولات × 10\nضغط أكتاف بالبار 4 جولات × 12", isCompleted = true),
            DaftariEntry(category = "WORKOUT", title = "تمارين المقاومة والظهر", content = "سحب ظهر واسع 4 جولات\nطحن بطن 3 جولات كرانشز\nتجديف كيبل 4 جولات لأسفل", isCompleted = false)
        )
        workoutSeeds.forEach { repository.insert(it) }

        // Future plans seeds
        val futurePlanSeeds = listOf(
            DaftariEntry(category = "FUTURE_PLANS", title = "مشروع كتابة دليل المطور العربي الشامل", content = "تأليف مرجع مبسط يشرح بنية البيانات، قواعد برمجة الأندرويد، وتجربة المستخدم للمبتدئين مجاناً."),
            DaftariEntry(category = "FUTURE_PLANS", title = "تحسين مستوى اللغة الإنجليزية والفرنسية", content = "دخول دورات أكاديمية متقدمة وحفظ 500 مفردة جديدة ومحادثة المتحدثين الأصليين أسبوعياً.")
        )
        futurePlanSeeds.forEach { repository.insert(it) }

        // Hobbies Progress Log
        val hobbySeeds = listOf(
            DaftariEntry(category = "HOBBY_LOG", title = "ممارسة الفن التشكيلي والرسم", content = "إكمال لوحة الغروب الزيتية على القماش وتجربة أساليب المزج بالألوان الباردة."),
            DaftariEntry(category = "HOBBY_LOG", title = "ممارسة رياضة الجري الجبلي الوعر", content = "تحدي 10 كيلومترات في المسارات البرية لرفع قوة التحمل العضلية والتنفسية.")
        )
        hobbySeeds.forEach { repository.insert(it) }

        // Photo Gallery & Favorites
        val gallerySeeds = listOf(
            DaftariEntry(category = "GALLERY", title = "سماء الليل الصافية والنجوم", content = "تصوير فلكي في ليلة حالكة وسط رمال الصحراء الذهبية تبرز مجرة درب التبانة.", imageUrl = "img_companion_cover"),
            DaftariEntry(category = "GALLERY", title = "غابات الخريف الذهبية الساحرة", content = "تدفق الأوراق البرتقالية والصفراء في منظر طبيعي ملهم للأمل والهدوء النفسي.", imageUrl = "img_educational_planets")
        )
        gallerySeeds.forEach { repository.insert(it) }

        // Library Seeds
        val librarySeeds = listOf(
            DaftariEntry(category = "LIBRARY", title = "مقدمة في تاريخ الكواكب والكون", content = "كتاب علمي شيق وممتع يناقش ولادة النجوم والمجرات وتفسير الظواهر الكونية بأسلوب تبسيطي مذهل.", rating = 4.8f, extraInfo = "د. علي الخالدي"),
            DaftariEntry(category = "LIBRARY", title = "هندسة البرمجيات النظيفة وقواعدها", content = "شرح مفصل لكتابة الكود والتعرف على معايير الجودة والصلابة البرمجية للمشاريع الكبيرة.", rating = 5.0f, extraInfo = "أونكل بوب")
        )
        librarySeeds.forEach { repository.insert(it) }

        // Music Playlist
        val musicSeeds = listOf(
            DaftariEntry(category = "MUSIC", title = "أصوات الطبيعة - خرير الماء والعصافير", content = "صوت نقي يبعث على التركيز أثناء المذاكرة والعمل الهادئ.", extraInfo = "قائمة التركيز الذهبي"),
            DaftariEntry(category = "MUSIC", title = "ألحان بيانو كلاسيكية مريحة للأعصاب", content = "مقطوعات فريدة تهدئ العقل وتساعد على النوم والاسترخاء بعد تعب شديد.", extraInfo = "موسيقى الاسترخاء")
        )
        musicSeeds.forEach { repository.insert(it) }

        // Travel Page
        val travelSeeds = listOf(
            DaftariEntry(category = "TRAVEL", title = "رحلة إلى مكة المكرمة كأولوية قصوى", content = "أداء مناسك العمرة، الصلاة في الروضة الشريفة، واستغلال الوقت في العبادة.", isCompleted = false),
            DaftariEntry(category = "TRAVEL", title = "زيارة مدينة أبها البهية", content = "الاستمتاع بالطقس الضبابي الماطر والقرى التراثية والتلفريك في مرتفعات السودة المعلقة.", isCompleted = true)
        )
        travelSeeds.forEach { repository.insert(it) }

        // Game tracker
        val gameSeeds = listOf(
            DaftariEntry(category = "GAMES", title = "لعبة الألغاز الميكانيكية الشهيرة Portal 2", content = "لعبة ذكاء وفيزياء ممتعة جداً تعتمد على المنطق البصري وحل العقبات الصعبة.", rating = 5f, isCompleted = true, extraInfo = "مكتملة"),
            DaftariEntry(category = "GAMES", title = "لعبة تقمص الأدوار الساحرة Elden Ring", content = "شاسعة وممتعة وصعبة جداً، تستكشف فيها عوالم أسطورية مدهشة ومصممة بعناية فائقة.", rating = 4.7f, isCompleted = false, extraInfo = "قيد اللعب")
        )
        gameSeeds.forEach { repository.insert(it) }

        // Preloaded Flash Cards (Cleared as requested)

        // Question Bank
        val qBankSeeds = listOf(
            DaftariEntry(category = "EXAM_QUESTIONS", title = "سؤال: ما هي معمارية MVVM ومميزاتها الأساسية؟", content = "هي معمارية تقسم التطبيق لـ: Model (البيانات والداتا)، View (الواجهات)، ViewModel (إدارة حالات الواجهة والاتصال بالبيانات والمنطق الفني)، وتمنحنا مرونة برمجية عالية واختبارية كود سهلة.", extraInfo = "هندسة البرمجيات"),
            DaftariEntry(category = "EXAM_QUESTIONS", title = "سؤال: عرف قانون الديناميكا الحرارية الثاني؟", content = "ينص على أن الإنتروبيا (الفوضى/تشتت الطاقة) الكلية لأي نظام معزول تزداد دائماً بمرور الزمن وتدفق الحرارة يحدث طوعياً من الساخن للبارد.", extraInfo = "الفيزياء الكلاسيكية")
        )
        qBankSeeds.forEach { repository.insert(it) }

        // Study Schedule
        val studyScheduleSeeds = listOf(
            DaftariEntry(category = "STUDY_SCHEDULE", title = "جلسة مذاكرة الفيزياء الفلكية اليومية", content = "قراءة ومذاكرة الباب الثالث: النجوم النيوترونية والثقوب السوداء من الساعة 4 إلى 7 مساءً اليوم.", isCompleted = false),
            DaftariEntry(category = "STUDY_SCHEDULE", title = "حل تدريبات تطبيقات قواعد البيانات أندرويد", content = "مراجعة دوال الاستعلام المعقدة وبناء داو وقاعدة بيانات روم بنجاح وبسرعة.", isCompleted = true)
        )
        studyScheduleSeeds.forEach { repository.insert(it) }

        // Lesson Summaries
        val summarySeeds = listOf(
            DaftariEntry(category = "LESSON_SUMMARIES", title = "تلخيص شامل لقوانين نيوتن وحركة الكواكب", content = "1- القانون الأول: الجسم الساكن يبقى ساكناً والمتحرك مستمراً مالم تؤثر فيه قوة خارجية.\n2- القانون الثاني: القوة تساوي الكتلة في التسارع الكوني (F=ma).\n3- القانون الثالث: لكل فعل رد فعل مساوٍ له في المقدار ومضاد له في الاتجاه المعاكس.", extraInfo = "الفيزياء"),
            DaftariEntry(category = "LESSON_SUMMARIES", title = "ملخص الروابط الكيميائية وخصائصها الفنية", content = "تتميز الرابطة الأيونية بانتقال كامل للإلكترونات وتوليد جزيئات بقطبية عالية جداً، بينما الرابطة التساهمية تعتمد على المشاركة والتكافؤ.", extraInfo = "الكيمياء العامة")
        )
        summarySeeds.forEach { repository.insert(it) }

        // Course Tracker (Cleared default pre-seeded entries as requested)

        // Language learning
        val langSeeds = listOf(
            DaftariEntry(category = "LANGUAGE", title = "الكلمة اللغوية: Resilience", content = "المعنى بالعربية: المرونة اللغوية والنفسية والقدرة على التكيف والصمود بوجه التحديات واستعادة القوة والنهوض سريعاً.", extraInfo = "الإنجليزية المتقدمة"),
            DaftariEntry(category = "LANGUAGE", title = "الكلمة اللغوية: Perseverance", content = "المعنى بالعربية: المثابرة والإصرار والمواظبة لتحقيق هدف طويل الأمد على الرغم من وجود الصعاب الشديدة والمحن.", extraInfo = "الإنجليزية المتقدمة")
        )
        langSeeds.forEach { repository.insert(it) }

        // Skill learning
        val skillLearnSeeds = listOf(
            DaftariEntry(category = "SKILLS", title = "برمجة ألعاب الأندرويد والرسوم ثنائية السرعة", content = "دراسة جودوت إنجن وبناء المنطق البرمجي وحركات فيزيائية للشخصيات والوحوش والجاذبية.", rating = 40f),
            DaftariEntry(category = "SKILLS", title = "مهارة الخط العربي والكتابة الديوانية والرقعة", content = "التدرب يومياً على مسك البوص والحبر والتحكم بحركات ميل وجلسات الكتابة التنافسية والزوايا.", rating = 70f)
        )
        skillLearnSeeds.forEach { repository.insert(it) }

        // Programming Page
        val codeSeeds = listOf(
            DaftariEntry(category = "PROGRAMMING", title = "كود استعلام روم بسيط في كومبوز", content = "```kotlin\n@Dao\ninterface MyDao {\n    @Query(\"SELECT * FROM items\")\n    fun getItems(): Flow<List<Item>>\n}\n```\nهذا الكود يعيد قائمة تتدفق باستمرار وتتحدث الواجهات كومبوز تلقائياً.", extraInfo = "Kotlin"),
            DaftariEntry(category = "PROGRAMMING", title = "كود بايثون سريع لقراءة ملف نصي ومعالجته", content = "```python\nwith open('data.txt', 'r', encoding='utf-8') as f:\n    lines = f.readlines()\n    print(f\"عدد الأسطر: {len(lines)}\")\n```\nطريقة آمنة لقراءة الملف تغلق المورد تلقائياً.", extraInfo = "Python")
        )
        codeSeeds.forEach { repository.insert(it) }

        // Quotes seeds
        val quoteSeeds = listOf(
            DaftariEntry(category = "QUOTES", title = "قيمة المرء ما يحسنه، فاطلب العلم تظفر به حيا.", content = "الإمام علي بن أبي طالب k", extraInfo = "GENERAL"),
            DaftariEntry(category = "QUOTES", title = "كن مع الله تر الله معك، واترك الكل واقصد طمعك.", content = "اقتباس صوفي إسلامي ملهم", extraInfo = "PERSONAL"),
            DaftariEntry(category = "QUOTES", title = "الكمال ليس أن تفعل أشياء خارقة، بل أن تفعل الأشياء العادية بأمانة وإتقان فائق.", content = "حكمة ملهمة للعمل المتقن", extraInfo = "PERSONAL")
        )
        quoteSeeds.forEach { repository.insert(it) }

        // Links seeds
        val linkSeeds = listOf(
            DaftariEntry(category = "LINKS", title = "موقع جيت هاب - مستودعاتي البرمجية الخاصة", content = "https://github.com", extraInfo = "روابط برمجية"),
            DaftariEntry(category = "LINKS", title = "منصة جوجل للذكاء الاصطناعي والدراسة AI Studio", content = "https://ai.studio.google.com", extraInfo = "أدوات الذكاء الاصطناعي")
        )
        linkSeeds.forEach { repository.insert(it) }

        // Experiences
        val expSeeds = listOf(
            DaftariEntry(category = "EXPERIENCES", title = "تجربتي الفريدة مع الصيام المتقطع", content = "التزام بـ 16 ساعة صيام، ساعدتني كثيراً في الحصول على صفاء ذهني غير مسبوق، ونزول مستويات الإنسولين في الدم بطريقة آمنة ومتوازنة وفعالة للغاية.", extraInfo = "شخصية"),
            DaftariEntry(category = "EXPERIENCES", title = "تجربتي البرمجية مع Jetpack Compose", content = "البناء الإعلاني والمجدول للواجهات أسهل بكثير من XML التفريدي التقليدي السابق. يوفر أكثر من 50% من كتابة الكلاسات البرمجية الطويلة والمملة ويعمل بسلاسة هائلة.", extraInfo = "تقنية")
        )
        expSeeds.forEach { repository.insert(it) }

        // Photo Diary
        val photoDiarySeeds = listOf(
            DaftariEntry(category = "PHOTO_DIARY", title = "شروق ساحر على شاطئ البحر", content = "التقطت هذه الصورة الجميلة مع نسمات هواء الفجر الباردة والمنعشة.", imageUrl = "img_companion_cover"),
            DaftariEntry(category = "PHOTO_DIARY", title = "تجهيز قهوة الإسبريسو الصباحية المعتادة", content = "رائحة البن المطحون الطازج هي بداية يوم رائع ومركز.", imageUrl = "img_profile_avatar")
        )
        photoDiarySeeds.forEach { repository.insert(it) }

        // Recipes
        val recipeSeeds = listOf(
            DaftariEntry(category = "RECIPES", title = "كبسة الدجاج السعودية الملوكية الفاخرة", content = "الأرز البسمتي الفاخر مع الدجاج المتبل والمطبوخ ببهارات الكبسة الأصلية والهيل واللومي والزعفران والقرفة والمزين بالمكسرات المحمصة اللذيذة والزبيب.", extraInfo = "عالمية/تقليدية"),
            DaftariEntry(category = "RECIPES", title = "شرباء الخضار المشبعة والدافئة للريجيم والأكل الصحي", content = "مجموعة من الكوسا، الجزر، البازلاء، والبروكلي مطبوخة بمرقة الدجاج الطبيعية الخالية تماماً من الدهون مع الليمون والكمون والكزبرة الطازجة.", extraInfo = "صحية"),
            DaftariEntry(category = "RECIPES", title = "سلطة الكينوا الغنية بالبروتين والفيتامينات والحديد", content = "خلط الكينوا المسلوقة مع الطماطم الكرزية، الخيار، أوراق النعناع الطازجة، عصير الليمون الحامض وزيت الزيتون البكر الأصلي الفاخر والغني بمضادات الأكسدة.", extraInfo = "صحية")
        )
        recipeSeeds.forEach { repository.insert(it) }

        // Media tracker
        val mediaSeeds = listOf(
            DaftariEntry(category = "MEDIA_TRACKER", title = "وثائقي كوكبنا العظيم Our Planet ديفيد أتينبورو", content = "تغطية مذهلة للتنوع البيولوجي الطبيعي وبيئات الحيوانات المدهشة حول العالم.", rating = 5.0f, isCompleted = true, extraInfo = "منتهي"),
            DaftariEntry(category = "MEDIA_TRACKER", title = "فيلم الخيال العلمي والفيزياء Interstellar كريستوفر نولان", content = "رحلة فضائية عبر ثقب دودي للبحث عن كوكب بديل صالح للبشرية مفعم بالحب والأبعاد والجاذبية وقوانين غارقة بالعمق البصري.", rating = 4.9f, isCompleted = false, extraInfo = "أخطط لمشاهدته مجدداً")
        )
        mediaSeeds.forEach { repository.insert(it) }

        // Health log
        val healthSeeds = listOf(
            DaftariEntry(category = "HEALTH_LOG", title = "شرب كورس الماء اليومي الكامل", content = "شرب 3 لترات من الماء المصفى على مدار اليوم للحفاظ على مرونة الجلد ووظائف الكلى الحيوية.", rating = 100f),
            DaftariEntry(category = "HEALTH_LOG", title = "وجبة الإفطار الصحية الصباحية", content = "بيض مسلوق عدد 2 + شريحة خبز الشوفان الكامل + نصف حبة أفوكادو غنية بالدهون المفيدة مع خيار طماطم وجرجير.", rating = 95f)
        )
        healthSeeds.forEach { repository.insert(it) }
    }
}
