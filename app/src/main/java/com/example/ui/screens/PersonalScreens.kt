package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.R
import com.example.data.DaftariEntry
import com.example.ui.DaftariViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ProfileScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    val profileEntry = entries.firstOrNull { it.category == "PROFILE" }
    var isEditing by remember { mutableStateOf(false) }

    var name by remember(profileEntry) { mutableStateOf(profileEntry?.title ?: "أحمد محمد") }
    var bio by remember(profileEntry) { mutableStateOf(profileEntry?.content ?: "طالب جامعي شغوف بالتعلم والمثابرة.") }
    var hobbies by remember(profileEntry) { mutableStateOf(profileEntry?.extraInfo ?: "القراءة, البرمجة, الرياضة") }
    var savedAvatarUri by remember(profileEntry) { mutableStateOf(profileEntry?.imageUrl ?: "img_profile_avatar") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            savedAvatarUri = uri.toString()
            val updated = DaftariEntry(
                id = profileEntry?.id ?: 0,
                category = "PROFILE",
                title = name,
                content = bio,
                extraInfo = hobbies,
                imageUrl = uri.toString()
            )
            viewModel.insert(updated)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") }
                ) {
                    if (savedAvatarUri == "img_profile_avatar" || savedAvatarUri.isEmpty()) {
                        Image(
                            painter = painterResource(id = R.drawable.img_profile_avatar),
                            contentDescription = "الصورة الشخصية",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = rememberAsyncImagePainter(model = savedAvatarUri),
                            contentDescription = "الصورة الشخصية مخصصة",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Icon(Icons.Filled.Edit, contentDescription = "تغيير")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("رفع صورة من الجهاز", fontWeight = FontWeight.Bold)
                }
            }
        }

        item {
            if (isEditing) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("الاسم الكامل") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("نبذة عنك") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_bio_input"),
                    minLines = 3
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = hobbies,
                    onValueChange = { hobbies = it },
                    label = { Text("الهوايات (افصل بفاصلة)") },
                    modifier = Modifier.fillMaxWidth().testTag("profile_hobbies_input")
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val updated = DaftariEntry(
                            id = profileEntry?.id ?: 0,
                            category = "PROFILE",
                            title = name,
                            content = bio,
                            extraInfo = hobbies,
                            imageUrl = savedAvatarUri
                        )
                        viewModel.insert(updated)
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth().testTag("profile_save_button")
                ) {
                    Icon(Icons.Filled.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("حفظ التغييرات")
                }
            } else {
                Text(
                    text = name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PaddingValues(16.dp)
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "نبذة شخصية",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = bio,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Justify,
                            lineHeight = 22.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "اهتماماتي وهواياتي",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    hobbies.split(",").forEach { hobby ->
                        val trimmed = hobby.trim()
                        if (trimmed.isNotEmpty()) {
                            SuggestionChip(
                                onClick = {},
                                label = { Text(trimmed) }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth().testTag("profile_edit_button")
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تعديل البيانات الشخصية")
                }
            }
        }
    }
}

@Composable
fun DiaryScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val diaryEntries = entries.filter { it.category == "DIARY" }

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
                    Text("سجل حدثاً أو فكرة اليوم", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("العنوان") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("ماذا حدث معك اليوم؟ ...") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && content.isNotEmpty()) {
                                viewModel.insert(DaftariEntry(category = "DIARY", title = title, content = content))
                                title = ""
                                content = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_diary_button")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Text("إضافة لليوميات")
                    }
                }
            }
        }

        item {
            Text("يومياتك المسجلة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (diaryEntries.isEmpty()) {
            item {
                Text("لا توجد يوميات مسجلة بعد. ابدأ بكتابة يومياتك!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(diaryEntries) { item ->
                val dateStr = SimpleDateFormat("dd MMMM yyyy - hh:mm a", Locale("ar")).format(Date(item.timestamp))
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
                            IconButton(onClick = { viewModel.deleteById(item.id) }, modifier = Modifier.testTag("delete_diary_${item.id}")) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Text(dateStr, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                    }
                }
            }
        }
    }
}

@Composable
fun MemoriesScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var memoryName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val memoryEntries = entries.filter { it.category == "MEMORIES" }

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
                    Text("وثّق ذكرى مميزة جديدة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = memoryName,
                        onValueChange = { memoryName = it },
                        label = { Text("اسم الحدث / المناسبة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("وصف الذكرى وشعورك تجاهها") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            if (memoryName.isNotEmpty() && description.isNotEmpty()) {
                                viewModel.insert(DaftariEntry(category = "MEMORIES", title = memoryName, content = description))
                                memoryName = ""
                                description = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_memory_button")
                    ) {
                        Text("حفظ الذاكرة")
                    }
                }
            }
        }

        item {
            Text("صندوق الذكريات العائلية والشخصية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (memoryEntries.isEmpty()) {
            item {
                Text("لم تسجل أي ذكرى بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(memoryEntries) { item ->
                val dateStr = SimpleDateFormat("dd/MM/yyyy", Locale("ar")).format(Date(item.timestamp))
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
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Filled.Favorite, contentDescription = null, tint = Color.Red)
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Text("تاريخ الحدث: $dateStr", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun DreamsScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var dreamTitle by remember { mutableStateOf("") }
    var dreamDetail by remember { mutableStateOf("") }
    val dreamEntries = entries.filter { it.category == "DREAMS" }

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
                    Text("تطلّع للمستقبل واكتب حلمك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = dreamTitle,
                        onValueChange = { dreamTitle = it },
                        label = { Text("ما هو حلمك الأكبر؟") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = dreamDetail,
                        onValueChange = { dreamDetail = it },
                        label = { Text("خطتك أو خطوات إنجازه") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            if (dreamTitle.isNotEmpty()) {
                                viewModel.insert(DaftariEntry(category = "DREAMS", title = dreamTitle, content = dreamDetail, isCompleted = false))
                                dreamTitle = ""
                                dreamDetail = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_dream_button")
                    ) {
                        Text("إضافة حلم")
                    }
                }
            }
        }

        item {
            Text("قائمتك للأحلام والطموحات المستقبلية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (dreamEntries.isEmpty()) {
            item {
                Text("لا توجد أحلام وطموحات مدونة بعد. تذكر أن الأحلام تبدأ بكلمة!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(dreamEntries) { item ->
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
                            Checkbox(
                                checked = item.isCompleted,
                                onCheckedChange = { isChecked ->
                                    viewModel.update(item.copy(isCompleted = isChecked))
                                },
                                colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                            )
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                                )
                                if (item.content.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(item.content, style = MaterialTheme.typography.bodyMedium, color = if (item.isCompleted) Color.Gray else Color.Unspecified)
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
fun SelfReviewScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var periodTitle by remember { mutableStateOf("") }
    var reviewText by remember { mutableStateOf("") }
    var ratingValue by remember { mutableFloatStateOf(4f) }
    val reviewEntries = entries.filter { it.category == "SELF_REVIEW" }

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
                    Text("تقييم ومراجعة شخصية دورية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = periodTitle,
                        onValueChange = { periodTitle = it },
                        label = { Text("الفترة (مثال: مراجعة شهر يونيو)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = reviewText,
                        onValueChange = { reviewText = it },
                        label = { Text("ملاحظاتك الإيجابية ونقاط الضعف التي تحتاج لتطوير") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Text("تقييمك العام لنفسك بالفترة:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            val active = i <= ratingValue
                            Icon(
                                imageVector = if (active) Icons.Filled.Star else Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (active) GoldAccent else Color.Gray,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { ratingValue = i.toFloat() }
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${ratingValue.toInt()} / 5 نجوم", fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = {
                            if (periodTitle.isNotEmpty() && reviewText.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "SELF_REVIEW",
                                        title = periodTitle,
                                        content = reviewText,
                                        rating = ratingValue
                                    )
                                )
                                periodTitle = ""
                                reviewText = ""
                                ratingValue = 4f
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_review_button")
                    ) {
                        Text("حفظ التقييم الذاتي")
                    }
                }
            }
        }

        item {
            Text("سجل تطورك ومراجعاتك السابقة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (reviewEntries.isEmpty()) {
            item {
                Text("لا توجد مراجعات سابقة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(reviewEntries) { item ->
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(5) { ind ->
                                val starRate = ind + 1
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (starRate <= item.rating) GoldAccent else Color.LightGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${item.rating.toInt()}/5", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                    }
                }
            }
        }
    }
}
val GoldAccent = Color(0xFFD69E2E)
