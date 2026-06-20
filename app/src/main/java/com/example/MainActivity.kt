package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DaftariEntry
import com.example.ui.DaftariViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: DaftariViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                // Force RTL LayoutDirection for cohesive Arabic view orientation naturally
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        DaftariAppContent(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

data class DaftariSubpage(val id: String, val title: String, val description: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

data class DaftariCategory(val title: String, val description: String, val subpages: List<DaftariSubpage>, val accentColor: Color)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DaftariAppContent(viewModel: DaftariViewModel, modifier: Modifier = Modifier) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val activeCategoryName by viewModel.activeCategory.collectAsState()
    val activeSubpageId by viewModel.activeSubpageId.collectAsState()
    val entries by viewModel.allEntries.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    var showPermissionPrompt by remember {
        mutableStateOf(
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_MEDIA_IMAGES) != android.content.pm.PackageManager.PERMISSION_GRANTED
            } else {
                androidx.core.content.ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED
            }
        )
    }

    val permissionLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        showPermissionPrompt = false
    }

    if (showPermissionPrompt) {
        AlertDialog(
            onDismissRequest = { showPermissionPrompt = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text("منح الصلاحيات اللازمة", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Text(
                    "مرحباً بك في دفتري المميز! 📝 لتتمكن من تخصيص صورتك الشخصية وتصفح الصور المفضلة وسماع الموسيقى من جهازك مباشرة، يرجى منح الوصول الصوتي والمرئي.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        val list = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                            arrayOf(
                                android.Manifest.permission.READ_MEDIA_IMAGES,
                                android.Manifest.permission.READ_MEDIA_AUDIO
                            )
                        } else {
                            arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        }
                        permissionLauncher.launch(list)
                    }
                ) {
                    Text("منح الآن")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermissionPrompt = false }) {
                    Text("لاحقاً")
                }
            }
        )
    }

    // 6 major categories with child subpages mapped seamlessly
    val categories = remember {
        listOf(
            DaftariCategory(
                title = "صفحات شخصية",
                description = "ملفك التعريفي، ومقالات يومياتك، وذكرياتك العائلية، وأحلام حياتك الاستراتيجية.",
                accentColor = Color(0xFF0F9B73),
                subpages = listOf(
                    DaftariSubpage("profile", "صفحة شخصية", "بياناتك وهواياتك ونبذة عنك", Icons.Filled.Person),
                    DaftariSubpage("diary", "صفحة يوميات", "دوّن أحداث وسجلات يومك ببساطة", Icons.Filled.Edit),
                    DaftariSubpage("memories", "صفحة ذكريات", "صندوق لتوثيق صور وأحداث عائلية هامة", Icons.Filled.Favorite),
                    DaftariSubpage("dreams", "أحلام مستقبلية", "أهدافك وطموحاتك الاستراتيجية الكبرى", Icons.Filled.Star),
                    DaftariSubpage("self_review", "مراجعة شخصية", "تقييم أسبوعي وربع سنوي لتطور عقلك", Icons.Filled.Info)
                )
            ),
            DaftariCategory(
                title = "صفحات تنظيم الحياة",
                description = "قائمة مهام، وجداول يومية، وتتبع لتمويلك وميزانيتك، وجودة نومك وصحتك البدنية.",
                accentColor = Color(0xFFD69E2E),
                subpages = listOf(
                    DaftariSubpage("todo", "قائمة مهام", "قائمة تتبع وإنجاز المسؤوليات السريعة", Icons.Filled.Check),
                    DaftariSubpage("planner", "مفكرة يومية", "تسجيل وجدولة نشاطات ومواعيد اليوم", Icons.Filled.DateRange),
                    DaftariSubpage("budget", "صفحة ميزانية", "تتبع إيراداتك ومصروفاتك وحساب الوفورات", Icons.Filled.Add),
                    DaftariSubpage("sleep", "متابعة نوم", "رصد ساعات راحتك ومؤشر جودة نومك", Icons.Filled.Star),
                    DaftariSubpage("workout", "جدول رياضي", "خطة التمارين الرياضية وجلسات اللياقة", Icons.Filled.List),
                    DaftariSubpage("future_plans", "خطط مستقبلية", "مخططات استراتيجية للمشاريع طويلة الأجل", Icons.Filled.DateRange)
                )
            ),
            DaftariCategory(
                title = "هوايات واهتمامات",
                description = "معرض لصورك، مكتبتك الأدبية، مقطوعات السمع الموسيقية، ودفاتر رحلات سفرك وألعابك.",
                accentColor = Color(0xFF1F7A8C),
                subpages = listOf(
                    DaftariSubpage("gallery", "معرض صور", "أرشيف لصورك المفضلة ملهمة للتأمل", Icons.Filled.Home),
                    DaftariSubpage("library", "مكتبة شخصية", "كتب ومجلات دراسية وأدبية ترغب بقراءتها", Icons.Filled.List),
                    DaftariSubpage("music", "صفحة موسيقى", "أصوات طبيعية وألحان هادئة للدراسة", Icons.Filled.Favorite),
                    DaftariSubpage("travel", "صفحة سفر", "خطط لزيارتك وأسفارك القادمة بكل أريحية", Icons.Filled.LocationOn),
                    DaftariSubpage("game_tracker", "متابعة ألعاب", "سجل إنجازات وتحديات ألعابك الإلكترونية", Icons.Filled.List),
                    DaftariSubpage("fav_images", "صور مفضلة", "أجمل اللقطات والرموز الموحية بالأمل", Icons.Filled.Home),
                    DaftariSubpage("hobby_log", "سجل هوايات", "تتبع ممارستك للفنون والرياضات المفضلة", Icons.Filled.Edit)
                )
            ),
            DaftariCategory(
                title = "صفحات تعليمية ودراسة",
                description = "علم الفلك الكواكب، وفلاش كاردز بطاقات، وبنك أسئلة، ملخصات ومؤشرات تقدم كويزات مجدولة.",
                accentColor = Color(0xFF9F7AEA),
                subpages = listOf(
                    DaftariSubpage("planet_edu", "صفحة تعليمية", "موسوعة واستكشاف فلكي لكواكب الفضاء", Icons.Filled.Settings),
                    DaftariSubpage("flashcards", "فلاش كاردز", "بطاقات ذكية للمراجعة عبر ميكانيكية القلب", Icons.Filled.Menu),
                    DaftariSubpage("q_bank", "بنك أسئلة", "بنك لحل واختبار المسائل الامتحانية الصعبة", Icons.Filled.Search),
                    DaftariSubpage("study_schedule", "جدول مذاكرة", "تنظيم وإعادة هيكلة ساعات المراجعة اليومية", Icons.Filled.DateRange),
                    DaftariSubpage("summaries", "صفحة ملخصات", "أرشيف ملخص لدروسك وجزئيات المواد أهمها", Icons.Filled.List),
                    DaftariSubpage("progress_tracker", "متابعة تقدم", "خطوط بيانية لمستوى تمكينك لكل علم", Icons.Filled.Info),
                    DaftariSubpage("mcq_tests", "اختبارات تجريبية", "تحديات خيارات متعددة متبوعة بتصحيح فوري", Icons.Filled.Check),
                    DaftariSubpage("glossary", "صفحة مصطلحات", "معجم لتفسير مدلولات المصطلحات العلمية", Icons.Filled.List),
                    DaftariSubpage("course_tracker", "متابعة كورسات", "مؤشر لدرجة إنجازك لكل دورة أونلاين", Icons.Filled.Info),
                    DaftariSubpage("lang_learn", "تعلم لغة", "حفظ الكلمات والقواعد المبتكرة للغات", Icons.Filled.Edit),
                    DaftariSubpage("skill_learn", "تعلم مهارة", "خريطة طريق لاكتساب المهارات الاحترافية", Icons.Filled.Star),
                    DaftariSubpage("code_learn", "تعلم برمجة", "مستودع لتكامل الأكواد والمشاريع الصغيرة", Icons.Filled.Settings)
                )
            ),
            DaftariCategory(
                title = "صفحات إبداعية وشخصية",
                description = "حكم واقتباسات ملهمة، روابطك الرقمية، سجل لتجاربك ونظرياتك وتجارب المطبخ.",
                accentColor = Color(0xFFED64A6),
                subpages = listOf(
                    DaftariSubpage("quotes", "صفحة اقتباسات", "موسوعة الحكم والمأثورات الإنسانية الراقية", Icons.Filled.Favorite),
                    DaftariSubpage("personal_quotes", "اقتباسات شخصية", "مقولات كتبتها أو تفضلها برياح رحلتك", Icons.Filled.Star),
                    DaftariSubpage("links", "روابط شخصية", "روابطك ومواقعك الإلكترونية المعتادة يومياً", Icons.Filled.Share),
                    DaftariSubpage("experiences", "تجارب شخصية", "تحديات ومخططات تدوّن تأملاتك لكل تجربة", Icons.Filled.List),
                    DaftariSubpage("tech_experiences", "تجارب تقنية", "دروسك المستفادة ومراجعاتك للأجهزة والبرامج", Icons.Filled.Settings),
                    DaftariSubpage("photo_diary", "يوميات مصورة", "بطاقات لذكريات يومك تسطع بالوصف الملائم", Icons.Filled.Home),
                    DaftariSubpage("recipes", "صفحة وصفات", "كتالوج إعداد وجبات الغذاء اللذيذة", Icons.Filled.Menu),
                    DaftariSubpage("personal_recipes", "وصفات شخصية", "أسرارك وإضافاتك الرائعة لخلطات الطعام", Icons.Filled.Add),
                    DaftariSubpage("healthy_recipes", "وصفات صحية", "أكلات منخفضة السعرات تدعم نظامك الرياضي", Icons.Filled.Check)
                )
            ),
            DaftariCategory(
                title = "صفحات متابعة اهتمامات",
                description = "تقييم الكتب، رصد مسلسلات وقوائم أفلام تحبها، ومتابعة سجلات تروية وترطيب عاداتك الصحية.",
                accentColor = Color(0xFF4299E1),
                subpages = listOf(
                    DaftariSubpage("book_reviews", "مراجعة كتب", "ملخصات وتقييمات للكتب التي قرأتها مؤخراً", Icons.Filled.List),
                    DaftariSubpage("media_tracker", "مسلسلات/أفلام", "سلسلة أعمال تود مشاهدتها وتقييم حبكتها", Icons.Filled.List),
                    DaftariSubpage("health_log", "صفحة صحية", "تتبع لترطيب وشرب الماء والوجبات الصحية", Icons.Filled.Check)
                )
            )
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("الكل") }

    val filteredSubpages = remember(searchQuery) {
        if (searchQuery.isEmpty()) emptyList()
        else {
            categories.flatMap { cat ->
                cat.subpages.map { sub -> sub to cat.title }
            }.filter { it.first.title.contains(searchQuery) || it.first.description.contains(searchQuery) }
        }
    }

    val displayCategories = remember(selectedFilter, categories) {
        if (selectedFilter == "الكل") categories
        else {
            categories.filter { cat ->
                when (selectedFilter) {
                    "تنظيم" -> cat.title.contains("تنظيم")
                    "هوايات" -> cat.title.contains("هوايات")
                    "تعليم" -> cat.title.contains("تعليم") || cat.title.contains("دراسة")
                    "شخصية" -> cat.title.contains("شخصية")
                    "متابعة" -> cat.title.contains("متابعة") || cat.title.contains("اهتمامات")
                    else -> true
                }
            }
        }
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            fadeIn(animationSpec = tween(220)) with fadeOut(animationSpec = tween(220))
        },
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) { screenState ->
        when {
            screenState == "dashboard" -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        // Header Layout from the design HTML
                        val profileEntry = entries.firstOrNull { it.category == "PROFILE" }
                        val userNameInitial = (profileEntry?.title ?: "أ").first().toString()
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "مرحباً بعودتك،",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF64748B), // Slate-500
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "مساحتي الخاصة",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSystemInDarkTheme()) Color.White else Color(0xFF1D1B20)
                                )
                            }
                            
                            // Beautiful customized avatar or default fallback
                            val avatarUrl = profileEntry?.imageUrl
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .clickable { viewModel.navigateTo("subpage_profile", "صفحات شخصية", "profile") }
                                    .background(Color(0xFFD0BCFF))
                                    .border(2.dp, Color.White, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (avatarUrl.isNullOrEmpty() || avatarUrl == "img_profile_avatar") {
                                    Text(
                                        text = userNameInitial,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = Color(0xFF21005D)
                                    )
                                } else {
                                    Image(
                                        painter = coil.compose.rememberAsyncImagePainter(model = avatarUrl),
                                        contentDescription = "الصورة الشخصية",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }

                    item {
                        // Category Scrollable Filter Bar from HTML instructions
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val filterOptions = listOf("الكل", "تنظيم", "هوايات", "تعليم", "شخصية", "متابعة")
                            filterOptions.forEach { option ->
                                val isSelected = selectedFilter == option
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(
                                            if (isSelected) Color(0xFFEADDFF) else if (isSystemInDarkTheme()) Color(0xFF2C2A30) else Color.White
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) Color(0xFFD0BCFF) else if (isSystemInDarkTheme()) Color(0xFF3B3940) else Color(0xFFE2E8F0),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable { selectedFilter = option }
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF21005D) else if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.8f) else Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }

                    item {
                        // Profile Cover Card (Grid col-span-2) with dynamic tags and database syncing
                        val profileEntry = entries.firstOrNull { it.category == "PROFILE" }
                        val userName = profileEntry?.title ?: "أحمد محمد"
                        val userBio = profileEntry?.content ?: "طالب جامعي شغوف بالتعلم والمثابرة."
                        val userHobbies = profileEntry?.extraInfo ?: "الرسم, التقنية"
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(32.dp))
                                .background(
                                    androidx.compose.ui.graphics.Brush.linearGradient(
                                        colors = listOf(Color(0xFF6750A4), Color(0xFF9278D1))
                                    )
                                )
                                .padding(20.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "صفحة الشخصية",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.8f)
                                        )
                                        Text(
                                            text = userName,
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            lineHeight = 24.sp
                                        )
                                        Text(
                                            text = userBio,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.9f),
                                            lineHeight = 16.sp,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                    
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Color.White.copy(alpha = 0.2f))
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    userHobbies.split(",").take(3).forEach { tag ->
                                        val trimmed = tag.trim()
                                        if (trimmed.isNotEmpty()) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color.White.copy(alpha = 0.3f))
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = "#$trimmed",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // Global search field to find any of the 35 pages immediately
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("ابحث عن أي صفحة (مثال: يوميات، فلاش كاردز)...") },
                            modifier = Modifier.fillMaxWidth().testTag("global_search_input"),
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            trailingIcon = if (searchQuery.isNotEmpty()) {
                                {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Filled.Delete, contentDescription = "مسح")
                                    }
                                }
                            } else null
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        item {
                            Text("نتائج البحث المباشرة:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        if (filteredSubpages.isEmpty()) {
                            item {
                                Text("لا توجد صفحات مطابقة لبحثك.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                        } else {
                            items(filteredSubpages) { (subpage, catTitle) ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.navigateTo("subpage_${subpage.id}", catTitle, subpage.id)
                                            searchQuery = ""
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Icon(subpage.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                                        Column {
                                            Text(subpage.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                            Text("قسم: $catTitle", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = "أقسام وأدوات تنظيم الحياة:",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Displaying 6 main category workspace card folders styled in "Vibrant Palette" specs
                        items(displayCategories) { cat ->
                            val isDark = isSystemInDarkTheme()
                            val (cardBg, borderStroke, textColorOpen, iconBg, iconColor) = when (cat.title) {
                                "صفحات شخصية" -> {
                                    listOf(
                                        if (isDark) Color(0xFF382F4E) else Color(0xFFE8DEF8),
                                        Color(0xFFD0BCFF),
                                        if (isDark) Color(0xFFEADDFF) else Color(0xFF21005D),
                                        Color(0xFF6750A4),
                                        Color.White
                                    )
                                }
                                "صفحات تنظيم الحياة" -> {
                                    listOf(
                                        if (isDark) Color(0xFF552E2A) else Color(0xFFF2B8B5),
                                        Color(0xFFE46962),
                                        if (isDark) Color(0xFFFFD8D6) else Color(0xFF31111D),
                                        Color(0xFFB3261E),
                                        Color.White
                                    )
                                }
                                "هوايات واهتمامات" -> {
                                    listOf(
                                        if (isDark) Color(0xFF2E242B) else Color.White,
                                        if (isDark) Color(0xFFFFD8E4).copy(alpha = 0.3f) else Color(0xFFE2E8F0),
                                        if (isDark) Color.White else Color(0xFF1D1B20),
                                        Color(0xFFFFD8E4),
                                        Color(0xFF31111D)
                                    )
                                }
                                "صفحات تعليمية ودراسة" -> {
                                    listOf(
                                        if (isDark) Color(0xFF202A38) else Color.White,
                                        if (isDark) Color(0xFFC2E7FF).copy(alpha = 0.3f) else Color(0xFFE2E8F0),
                                        if (isDark) Color.White else Color(0xFF1D1B20),
                                        Color(0xFFC2E7FF),
                                        Color(0xFF001D35)
                                    )
                                }
                                "صفحات إبداعية وشخصية" -> {
                                    listOf(
                                        if (isDark) Color(0xFF282A3A) else Color.White,
                                        if (isDark) Color(0xFFEADDFF).copy(alpha = 0.3f) else Color(0xFFE2E8F0),
                                        if (isDark) Color.White else Color(0xFF1D1B20),
                                        Color(0xFFEADDFF),
                                        Color(0xFF21005D)
                                    )
                                }
                                else -> { // صفحات متابعة اهتمامات
                                    listOf(
                                        if (isDark) Color(0xFF182E2B) else Color.White,
                                        if (isDark) Color(0xFFC1F2E8).copy(alpha = 0.3f) else Color(0xFFE2E8F0),
                                        if (isDark) Color.White else Color(0xFF1D1B20),
                                        Color(0xFFC1F2E8),
                                        Color(0xFF003B30)
                                    )
                                }
                            }
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(28.dp))
                                    .background(cardBg as Color)
                                    .border(
                                        width = 1.dp,
                                        color = borderStroke as Color,
                                        shape = RoundedCornerShape(28.dp)
                                    )
                                    .clickable {
                                        viewModel.navigateTo("category_${cat.title}", cat.title)
                                    }
                                    .testTag("category_card_${cat.title}")
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(44.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(iconBg as Color),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = when (cat.title) {
                                                    "صفحات شخصية" -> Icons.Filled.Person
                                                    "صفحات تنظيم الحياة" -> Icons.Filled.Check
                                                    "هوايات واهتمامات" -> Icons.Filled.Favorite
                                                    "صفحات تعليمية ودراسة" -> Icons.Filled.Search
                                                    "صفحات إبداعية وشخصية" -> Icons.Filled.Star
                                                    else -> Icons.Filled.Info
                                                },
                                                contentDescription = null,
                                                tint = iconColor as Color,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                        
                                        Column {
                                            Text(
                                                text = cat.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = textColorOpen as Color
                                            )
                                        }
                                    }
                                    
                                    Icon(
                                        imageVector = Icons.Filled.ArrowBack,
                                        contentDescription = null,
                                        tint = if (isDark) Color.White.copy(alpha = 0.5f) else Color(0xFF64748B),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            screenState.startsWith("category_") -> {
                val cat = categories.firstOrNull { it.title == activeCategoryName }
                if (cat != null) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            IconButton(onClick = { viewModel.navigateBack() }, modifier = Modifier.testTag("back_button")) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "رجوع")
                            }
                            Text(cat.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            items(cat.subpages) { sub ->
                                val isDark = isSystemInDarkTheme()
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(140.dp)
                                        .clip(RoundedCornerShape(28.dp))
                                        .background(
                                            if (isDark) Color(0xFF2C2A30) else Color.White
                                        )
                                        .border(
                                            width = 1.dp,
                                            color = cat.accentColor.copy(alpha = 0.35f),
                                            shape = RoundedCornerShape(28.dp)
                                        )
                                        .clickable {
                                            viewModel.navigateTo("subpage_${sub.id}", cat.title, sub.id)
                                        }
                                        .testTag("subpage_card_${sub.id}")
                                        .padding(14.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(cat.accentColor.copy(alpha = 0.15f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = sub.icon,
                                                contentDescription = null,
                                                tint = cat.accentColor,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = sub.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDark) Color.White else Color(0xFF1D1B20),
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            screenState.startsWith("subpage_") -> {
                val flatSubpages = categories.flatMap { cat -> cat.subpages.map { sub -> sub to cat } }
                val pair = flatSubpages.firstOrNull { it.first.id == activeSubpageId }
                if (pair != null) {
                    val sub = pair.first
                    val cat = pair.second

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Custom ToolBar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                IconButton(onClick = { viewModel.navigateBack() }, modifier = Modifier.testTag("sub_back_button")) {
                                    Icon(Icons.Filled.ArrowBack, contentDescription = "رجوع")
                                }
                                Text(sub.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            }
                            Text(cat.title, style = MaterialTheme.typography.bodySmall, color = cat.accentColor, fontWeight = FontWeight.ExtraBold)
                        }

                        // Render the perfect matching subpage layout dynamically
                        Box(modifier = Modifier.weight(1f)) {
                            when (sub.id) {
                                "profile" -> ProfileScreen(viewModel, entries)
                                "diary" -> DiaryScreen(viewModel, entries)
                                "memories" -> MemoriesScreen(viewModel, entries)
                                "dreams" -> DreamsScreen(viewModel, entries)
                                "self_review" -> SelfReviewScreen(viewModel, entries)
                                
                                "todo" -> TodoScreen(viewModel, entries)
                                "planner" -> PlannerScreen(viewModel, entries)
                                "budget" -> BudgetScreen(viewModel, entries)
                                "sleep" -> SleepScreen(viewModel, entries)
                                "workout" -> WorkoutScreen(viewModel, entries)
                                "future_plans" -> FuturePlansScreen(viewModel, entries)
                                
                                "gallery" -> GalleryScreen(viewModel, entries)
                                "library" -> LibraryScreen(viewModel, entries)
                                "music" -> MusicScreen(viewModel, entries)
                                "travel" -> TravelScreen(viewModel, entries)
                                "game_tracker" -> GameTrackerScreen(viewModel, entries)
                                "fav_images" -> FavImagesScreen(viewModel, entries)
                                "hobby_log" -> HobbyLogScreen(viewModel, entries)
                                
                                "planet_edu" -> PlanetEducationScreen()
                                "flashcards" -> FlashcardsScreen(viewModel, entries)
                                "q_bank" -> QuestionBankScreen(viewModel, entries)
                                "study_schedule" -> StudyScheduleScreen(viewModel, entries)
                                "summaries" -> LessonSummariesScreen(viewModel, entries)
                                "progress_tracker" -> ProgressTrackerScreen(viewModel, entries)
                                "mcq_tests" -> McqQuizScreen()
                                "glossary" -> GlossaryScreen(viewModel, entries)
                                "course_tracker" -> CourseTrackerScreen(viewModel, entries)
                                "lang_learn" -> LanguageLearnScreen(viewModel, entries)
                                "skill_learn" -> SkillLearnScreen(viewModel, entries)
                                "code_learn" -> CodeLearnScreen(viewModel, entries)
                                
                                "quotes" -> QuotesScreen(viewModel, entries, isPersonalOnly = false)
                                "personal_quotes" -> QuotesScreen(viewModel, entries, isPersonalOnly = true)
                                "links" -> LinksScreen(viewModel, entries)
                                "experiences" -> ExperiencesScreen(viewModel, entries, isTechOnly = false)
                                "tech_experiences" -> ExperiencesScreen(viewModel, entries, isTechOnly = true)
                                "photo_diary" -> PhotoDiaryScreen(viewModel, entries)
                                "recipes" -> RecipesScreen(viewModel, entries, recipeSubtype = "GENERAL")
                                "personal_recipes" -> RecipesScreen(viewModel, entries, recipeSubtype = "PERSONAL")
                                "healthy_recipes" -> RecipesScreen(viewModel, entries, recipeSubtype = "HEALTHY")
                                
                                "book_reviews" -> BookReviewsScreen(viewModel, entries)
                                "media_tracker" -> MediaTrackerScreen(viewModel, entries)
                                "health_log" -> HealthLogScreen(viewModel, entries)
                            }
                        }
                    }
                }
            }
        }
    }
}
