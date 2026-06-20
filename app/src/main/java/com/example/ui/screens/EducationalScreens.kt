package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.DaftariEntry
import com.example.ui.DaftariViewModel

@Composable
fun PlanetEducationScreen() {
    var selectedPlanet by remember { mutableStateOf("الأرض") }

    val planetData = mapOf(
        "عطارد" to "أقرب كوكب إلى الشمس وأصغر كواكب المجموعة الشمسية. لا يملك أي أقمار وجوه رقيق جداً يعاني من حرارة شديدة نهاراً وبرودة قارسة ليلاً.",
        "الزهرة" to "ثاني كواكب المجموعة الشمسية وهو كوكب مشع وساخن جداً، محاط بغلاف غازي كثيف يحبس الحرارة (ظاهرة الاحتباس الحراري القصوى).",
        "الأرض" to "كوكب الحياة المعتدل والمتميز بوجود المحيطات الواسعة والغلاف الجوي الغني بالأكسجين وهي المكان الوحيد المأهول المعروف في الكون الواسع.",
        "المريخ" to "الكوكب الأحمر المثير للاهتمام بوجود غبار ثاني أكسيد الحديد بكثرة على سطحه ومحتوياً على دراسات وبحوث متقدمة للبحث عن مياه أو حياة ماضية.",
        "المشتري" to "عملاق الغاز وأكبر كواكب منظومتنا الشمسية على الإطلاق يفوق حجم الأرض بأكثر من 1300 مرة ويشتهر بوجود البقعة الحمراء العاصفة العملاقة."
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.img_educational_planets),
                        contentDescription = "الكواكب والمجموعة الشمسية",
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "كواكب المجموعة الشمسية الفاتنة",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        item {
            Text("اختر كوكباً لاستكشاف معلوماته الفلكية المذهلة:", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                planetData.keys.forEach { planetName ->
                    val active = planetName == selectedPlanet
                    Button(
                        onClick = { selectedPlanet = planetName },
                        colors = ButtonDefaults.buttonColors(containerColor = if (active) MaterialTheme.colorScheme.primary else Color.Gray),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(2.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(planetName, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "حقائق علمية: كوكب $selectedPlanet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = planetData[selectedPlanet] ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}

@Composable
fun FlashcardsScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var frontSide by remember { mutableStateOf("") }
    var backSide by remember { mutableStateOf("") }
    val flashcards = entries.filter { it.category == "FLASHCARD" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("صناعة بطاقة تعليمية (فلاش كارد)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = frontSide,
                        onValueChange = { frontSide = it },
                        label = { Text("السؤال / الواجهة الأمامية") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = backSide,
                        onValueChange = { backSide = it },
                        label = { Text("الإجابة السريعة / الجهة الخلفية") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (frontSide.isNotEmpty() && backSide.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "FLASHCARD",
                                        title = frontSide,
                                        content = backSide
                                    )
                                )
                                frontSide = ""
                                backSide = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_flashcard_button")
                    ) {
                        Text("حفظ البطاقة")
                    }
                }
            }
        }

        item {
            Text("البطاقات التعليمية (اضغط على البطاقة لقلبها ومعاينة الإجابة)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (flashcards.isEmpty()) {
            item {
                Text("لا توجد بطاقات مدونة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(flashcards) { item ->
                var isFlipped by remember { mutableStateOf(false) }
                val rotationY by animateFloatAsState(
                    targetValue = if (isFlipped) 180f else 0f,
                    animationSpec = tween(durationMillis = 400)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(110.dp)
                            .graphicsLayer {
                                this.rotationY = rotationY
                                cameraDistance = 12f * density
                            }
                            .clickable { isFlipped = !isFlipped }
                            .testTag("flashcard_${item.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isFlipped) MaterialTheme.colorScheme.primaryContainer 
                                             else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotationY > 90f) {
                                Text(
                                    text = item.content,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.graphicsLayer { this.rotationY = 180f },
                                    textAlign = TextAlign.Center
                                )
                            } else {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("اضغط لعرض الإجابة التفصيلية", fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }
                    IconButton(onClick = { viewModel.deleteById(item.id) }, modifier = Modifier.testTag("delete_flashcard_${item.id}")) {
                        Icon(Icons.Filled.Delete, contentDescription = "حذف بطاقة فلاش", tint = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionBankScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    val questions = entries.filter { it.category == "EXAM_QUESTIONS" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("تدشين بنك أسئلة مخصص للمراجعة والامتحان", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("المادة أو المجال") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = question,
                        onValueChange = { question = it },
                        label = { Text("السؤال العلمي") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        label = { Text("الإجابة النموذجية المعتمدة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (question.isNotEmpty() && answer.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "EXAM_QUESTIONS",
                                        title = question,
                                        content = answer,
                                        extraInfo = subject
                                    )
                                )
                                question = ""
                                answer = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_qbank_button")
                    ) {
                        Text("حفظ بالبنك")
                    }
                }
            }
        }

        item {
            Text("بنك الأسئلة والمواد المسجلة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (questions.isEmpty()) {
            item {
                Text("البنك فارغ. أضف أسئلة للتجهيز للمراجعة والامتحان!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(questions) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SuggestionChip(onClick = {}, label = { Text(item.extraInfo ?: "مادة عامة") })
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("الإجابة: ${item.content}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun StudyScheduleScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var subject by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val studyPlans = entries.filter { it.category == "STUDY_SCHEDULE" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("جدول وخطة المذاكرة اليومية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("المقرر الدراسي / الموضوع") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("خطة المذاكرة تفصيلاً وساعات الجهد المستهدفة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (subject.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "STUDY_SCHEDULE",
                                        title = subject,
                                        content = notes,
                                        isCompleted = false
                                    )
                                )
                                subject = ""
                                notes = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_study_schedule_button")
                    ) {
                        Text("حفظ بالجدول الدراسي")
                    }
                }
            }
        }

        item {
            Text("حصص وجلسات المذاكرة المخطط لها", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (studyPlans.isEmpty()) {
            item {
                Text("الجدول فارغ اليوم. اكتب لتبسيط خطوات علمك ومراجعتك!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(studyPlans) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Checkbox(checked = item.isCompleted, onCheckedChange = { ch ->
                                viewModel.update(item.copy(isCompleted = ch))
                            })
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                                )
                                if (item.content.isNotEmpty()) {
                                    Text(item.content, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                        IconButton(onClick = { viewModel.deleteById(item.id) }) {
                            Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonSummariesScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var title by remember { mutableStateOf("") }
    var summaryText by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    
    val summaries = entries.filter { it.category == "LESSON_SUMMARIES" }

    // Dynamic list of subjects
    val subjectsList = listOf("الكل") + summaries.map { it.extraInfo ?: "عام" }.distinct().filter { it.isNotEmpty() }
    var selectedSubjectTab by remember { mutableStateOf("الكل") }

    val filteredSummaries = summaries.filter { item ->
        val matchesSubject = selectedSubjectTab == "الكل" || item.extraInfo == selectedSubjectTab
        val matchesSearch = searchQuery.isEmpty() || 
                item.title.contains(searchQuery, ignoreCase = true) || 
                item.content.contains(searchQuery, ignoreCase = true) || 
                (item.extraInfo ?: "").contains(searchQuery, ignoreCase = true)
        matchesSubject && matchesSearch
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("تدوين ملخص مقتضب للدروس العلمية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("المقرر الدراسي (مثال: فيزياء الكواكب)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("عنوان التلخيص") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = summaryText,
                        onValueChange = { summaryText = it },
                        label = { Text("النقاط الأساسية والقوانين والتعاريف المهمة...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && summaryText.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "LESSON_SUMMARIES",
                                        title = title,
                                        content = summaryText,
                                        extraInfo = subject.ifEmpty { "عام" }
                                    )
                                )
                                title = ""
                                summaryText = ""
                                subject = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_summary_button")
                    ) {
                        Text("حفظ التلخيص")
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("البحث السريع والتصفية", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("ابحث عن فكرة، ملخص، أو درس...") },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                        colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.Transparent)
                    )

                    if (subjectsList.size > 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("حسب المادة الدراسية:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            subjectsList.forEach { sub ->
                                FilterChip(
                                    selected = selectedSubjectTab == sub,
                                    onClick = { selectedSubjectTab = sub },
                                    label = { Text(sub) }
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Text("مجلد التلاخيص الأكاديمية المدونة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (filteredSummaries.isEmpty()) {
            item {
                Text("لا توجد ملخصات تطابق البحث المختار.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(filteredSummaries) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SuggestionChip(onClick = {}, label = { Text(item.extraInfo ?: "عام") })
                                IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Menu, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "عدد كلمات الملخص: ${item.content.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size} كلمة", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressTrackerScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var targetName by remember { mutableStateOf("") }
    var progressSlider by remember { mutableFloatStateOf(50f) }
    val progressList = entries.filter { it.category == "PROGRESS" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("متابعة تقدم دراسي أو فني جديد", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = targetName,
                        onValueChange = { targetName = it },
                        label = { Text("المجال / المقرر المستهدف") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("مدى الإنجاز الحالي: ${progressSlider.toInt()}%", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Slider(
                        value = progressSlider,
                        onValueChange = { progressSlider = it },
                        valueRange = 0f..100f
                    )
                    Button(
                        onClick = {
                            if (targetName.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "PROGRESS",
                                        title = targetName,
                                        content = "مؤشر لتمكين ودراسة المهارة",
                                        rating = progressSlider
                                    )
                                )
                                targetName = ""
                                progressSlider = 50f
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_progress_button")
                    ) {
                        Text("حفظ نسبة التقدم")
                    }
                }
            }
        }

        item {
            Text("مؤشرات ونسب إنجاز تطلعاتك التعليمية والمهارية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (progressList.isEmpty()) {
            item {
                Text("لا توجد مؤشرات تقدم مسجلة.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(progressList) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            LinearProgressIndicator(
                                progress = { item.rating / 100f },
                                modifier = Modifier.weight(1f).height(10.dp),
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                            Text("${item.rating.toInt()}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun McqQuizScreen() {
    val quizQuestions = listOf(
        "ما هو الكوكب المسمى بالكوكب الأحمر؟" to listOf("الأرض", "عطارد", "المريخ", "المشتري") to 2,
        "ما هي المعمارية البرمجية التي تستخدم ViewModel لإدارة الحالات في كومبوز؟" to listOf("MVC", "MVVM", "MVP", "Monolithic") to 1,
        "ما هو أقرب كوكب إلى الشمس؟" to listOf("المشتري", "الأرض", "الزهرة", "عطارد") to 3
    )

    var currentQQuest by remember { mutableStateOf(0) }
    var selectedAnsOpt by remember { mutableStateOf<Int?>(null) }
    var userQuizScore by remember { mutableStateOf(0) }
    var showResultsAlert by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("اختبار قياسي تجريبي - متعدد الاختيارات", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        if (showResultsAlert) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("لقد أتممت الاختبار بنجاح!", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "حاصل نقاطك الإجمالي: $userQuizScore من أصل ${quizQuestions.size}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                currentQQuest = 0
                                selectedAnsOpt = null
                                userQuizScore = 0
                                showResultsAlert = false
                            },
                            modifier = Modifier.testTag("reset_quiz_button")
                        ) {
                            Text("إعادة المحاولة مجدداً")
                        }
                    }
                }
            }
        } else {
            val qData = quizQuestions[currentQQuest]
            val questText = qData.first.first
            val ansOptions = qData.first.second
            val correctIndex = qData.second

            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "السؤال ${currentQQuest + 1} من ${quizQuestions.size}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(questText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            items(ansOptions.size) { index ->
                val optText = ansOptions[index]
                val selected = selectedAnsOpt == index
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (selected) MaterialTheme.colorScheme.primaryContainer 
                                         else MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedAnsOpt = index }
                        .testTag("quiz_option_$index"),
                    border = if (selected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = selected, onClick = { selectedAnsOpt = index })
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(optText)
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        if (selectedAnsOpt != null) {
                            if (selectedAnsOpt == correctIndex) {
                                userQuizScore++
                            }
                            if (currentQQuest + 1 < quizQuestions.size) {
                                currentQQuest++
                                selectedAnsOpt = null
                            } else {
                                showResultsAlert = true
                            }
                        }
                    },
                    enabled = selectedAnsOpt != null,
                    modifier = Modifier.fillMaxWidth().testTag("quiz_next_button")
                ) {
                    Text(if (currentQQuest + 1 < quizQuestions.size) "التحقق والانتقال للتالي" else "إنهاء واستلام النتيجة")
                }
            }
        }
    }
}

@Composable
fun GlossaryScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var term by remember { mutableStateOf("") }
    var def by remember { mutableStateOf("") }
    val glossary = entries.filter { it.category == "GLOSSARY" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("أضف مصطلحاً علمياً أو برمجياً وتفسيره", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = term, onValueChange = { term = it }, label = { Text("المصطلح (مثال: API)") })
                    OutlinedTextField(value = def, onValueChange = { def = it }, label = { Text("تعريف وشرح المصطلح") })
                    Button(
                        onClick = {
                            if (term.isNotEmpty() && def.isNotEmpty()) {
                                viewModel.insert(DaftariEntry(category = "GLOSSARY", title = term, content = def))
                                term = ""
                                def = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_glossary_button")
                    ) {
                        Text("حفظ الكلمة")
                    }
                }
            }
        }

        item {
            Text("معجم المصطلحات والمفاهيم المدونة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (glossary.isEmpty()) {
            item {
                Text("المعجم فارغ.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(glossary) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun CourseTrackerScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var cName by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("") }
    var ratingPercent by remember { mutableFloatStateOf(0f) }
    val courses = entries.filter { it.category == "COURSES" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("تتبع دوراتك وتطورك المهني", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = cName, onValueChange = { cName = it }, label = { Text("اسم الكورس / الدورة") })
                    OutlinedTextField(value = platform, onValueChange = { platform = it }, label = { Text("المنصة الموفرة (مثال: يوديمي)") })
                    Text("مستواك الحالي في الدورة: ${ratingPercent.toInt()}%", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                    Slider(value = ratingPercent, onValueChange = { ratingPercent = it }, valueRange = 0f..100f)
                    Button(
                        onClick = {
                            if (cName.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "COURSES",
                                        title = cName,
                                        content = platform,
                                        rating = ratingPercent,
                                        isCompleted = ratingPercent == 100f,
                                        extraInfo = if (ratingPercent == 100f) "مكتملة" else "قيد الانجاز"
                                    )
                                )
                                cName = ""
                                platform = ""
                                ratingPercent = 0f
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_course_button")
                    ) {
                        Text("حفظ الكورس")
                    }
                }
            }
        }

        item {
            Text("قائمة الدورات والبرامج المسجلة لديك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (courses.isEmpty()) {
            item {
                Text("لا توجد دورات مسجلة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(courses) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("المنصة الموفرة: ${item.content}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            LinearProgressIndicator(
                                progress = { item.rating / 100f },
                                modifier = Modifier.weight(1f).height(6.dp),
                                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                            )
                            Text("${item.rating.toInt()}%", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageLearnScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var word by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val langs = entries.filter { it.category == "LANGUAGE" }

    // Dynamic languages list
    val languagesList = listOf("الكل") + langs.map { it.extraInfo ?: "عام" }.distinct().filter { it.isNotEmpty() }
    var selectedLanguageGroup by remember { mutableStateOf("الكل") }

    val filteredLangs = if (selectedLanguageGroup == "الكل") {
        langs
    } else {
        langs.filter { (it.extraInfo ?: "عام") == selectedLanguageGroup }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("إحصائيات التعلم السريعة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("إجمالي المفردات المحفوظة: ${langs.size} مفردة لغوية وقاعدة", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("تسجيل مفردة لغوية وقواعد جديدة بكفاءة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = word, 
                        onValueChange = { word = it }, 
                        label = { Text("الكلمة / العبارة الأجنبية") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = meaning, 
                        onValueChange = { meaning = it }, 
                        label = { Text("معناها الشامل واستعمالها") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = category, 
                        onValueChange = { category = it }, 
                        label = { Text("المجال / اللغة (مثال: إنجليزية، ألمانية)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (word.isNotEmpty() && meaning.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "LANGUAGE",
                                        title = word,
                                        content = meaning,
                                        extraInfo = category.ifEmpty { "عام" }
                                    )
                                )
                                word = ""
                                meaning = ""
                                category = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_lang_button")
                    ) {
                        Text("حفظ الكلمة")
                    }
                }
            }
        }

        // Language quick filter bar
        if (languagesList.size > 1) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("تصفية حسب اللغة:", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        languagesList.forEach { lang ->
                            FilterChip(
                                selected = selectedLanguageGroup == lang,
                                onClick = { selectedLanguageGroup = lang },
                                label = { Text(lang) }
                            )
                        }
                    }
                }
            }
        }

        // Interactive mini quiz card for self testing
        if (filteredLangs.isNotEmpty()) {
            item {
                var currentCardIndex by remember(filteredLangs.size, selectedLanguageGroup) { mutableStateOf(0) }
                if (currentCardIndex >= filteredLangs.size) {
                    currentCardIndex = 0
                }
                
                val currentCard = filteredLangs[currentCardIndex]
                var showAnswer by remember(currentCard) { mutableStateOf(false) }

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("لوحة المراجعة النشطة السريعة (تسهيل الحفظ)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = if (showAnswer) currentCard.content else currentCard.title, 
                            style = MaterialTheme.typography.headlineSmall, 
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                        Text(
                            text = if (showAnswer) "المعنى والاستخدام" else "خمن المعنى ثم اضغط على زر المعاينة أدناه",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Button(onClick = { showAnswer = !showAnswer }) {
                                Text(if (showAnswer) "إخفاء المعنى" else "إظهار المعنى")
                            }
                            if (filteredLangs.size > 1) {
                                Button(
                                    onClick = { 
                                        currentCardIndex = (currentCardIndex + 1) % filteredLangs.size
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                                ) {
                                    Text("الكلمة التالية >")
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Text("حصيلتك للمفردات والقواعد اللغوية المسجلة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (filteredLangs.isEmpty()) {
            item {
                Text("لا توجد مفرات مسجلة في هذا القسم بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(filteredLangs) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SuggestionChip(onClick = {}, label = { Text(item.extraInfo ?: "لغات") })
                                IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "حذف مفردة", tint = Color.Red)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun SkillLearnScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val skills = entries.filter { it.category == "SKILLS" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("وثق خطوات وملاحظات تعلم مهارة جديدة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("المهارة المستهدفة (مثال: الخط العربي)") })
                    OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("الملاحظات الاستراتيجية والخطوات العلمية للتنفيذ") }, minLines = 2)
                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                viewModel.insert(DaftariEntry(category = "SKILLS", title = title, content = notes))
                                title = ""
                                notes = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_skill_learn_button")
                    ) {
                        Text("حفظ المهارة")
                    }
                }
            }
        }

        item {
            Text("قائمتك للمهارات الإبداعية والفنية التي تتدرب عليها", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (skills.isEmpty()) {
            item {
                Text("لا توجد مهارات مسجلة.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(skills) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun CodeLearnScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var title by remember { mutableStateOf("") }
    var codeSnipp by remember { mutableStateOf("") }
    var programmingLang by remember { mutableStateOf("") }
    val codes = entries.filter { it.category == "PROGRAMMING" }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("مخزن الأكواد والمشاريع البرمجية الصغيرة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = programmingLang, onValueChange = { programmingLang = it }, label = { Text("لغة البرمجة (مثال: Kotlin)") })
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("اسم المشروع / الكود") })
                    OutlinedTextField(value = codeSnipp, onValueChange = { codeSnipp = it }, label = { Text("مقتطف الكود البرمجي") }, minLines = 4)
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && codeSnipp.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "PROGRAMMING",
                                        title = title,
                                        content = codeSnipp,
                                        extraInfo = programmingLang
                                    )
                                )
                                title = ""
                                codeSnipp = ""
                                programmingLang = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_code_button")
                    ) {
                        Text("حفظ بمخزن الأكواد")
                    }
                }
            }
        }

        item {
            Text("مستودع الأكواد والحلول التقنية المبتكرة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (codes.isEmpty()) {
            item {
                Text("المخزن البرمجي فارغ.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(codes) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SuggestionChip(onClick = {}, label = { Text(item.extraInfo ?: "كود") })
                                IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E2835),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.content,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                color = Color(0xFF81E6D9),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
