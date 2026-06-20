package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DaftariEntry
import com.example.ui.DaftariViewModel

@Composable
fun BookReviewsScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var titleBook by remember { mutableStateOf("") }
    var reviewText by remember { mutableStateOf("") }
    var bookRating by remember { mutableFloatStateOf(4f) }
    val books = entries.filter { it.category == "BOOKS" }

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
                    Text("أكتب مراجعة وتقييم لرواية أو كتاب قرأته", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = titleBook, onValueChange = { titleBook = it }, label = { Text("عنوان الرواية / الكتاب") })
                    OutlinedTextField(value = reviewText, onValueChange = { reviewText = it }, label = { Text("ملخصك، انطباعك، والفوائد العلمية...") }, minLines = 3)
                    
                    Text("تقييمك لمحتوى الكتاب:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..5) {
                            val active = i <= bookRating
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = null,
                                tint = if (active) Color(0xFFD69E2E) else Color.Gray,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clickable { bookRating = i.toFloat() }
                            )
                        }
                    }
                    Button(
                        onClick = {
                            if (titleBook.isNotEmpty() && reviewText.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "BOOKS",
                                        title = titleBook,
                                        content = reviewText,
                                        rating = bookRating
                                    )
                                )
                                titleBook = ""
                                reviewText = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_book_review_button")
                    ) {
                        Text("حفظ المراجعة")
                    }
                }
            }
        }

        item {
            Text("مجلد مراجعات كتبك وتأملاتك الفكرية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (books.isEmpty()) {
            item {
                Text("لا توجد مراجعات مسجلة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(books) { item ->
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(5) { stepIndex ->
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (stepIndex + 1 <= item.rating) Color(0xFFD69E2E) else Color.LightGray,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                    }
                }
            }
        }
    }
}

@Composable
fun MediaTrackerScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var mediaName by remember { mutableStateOf("") }
    var review by remember { mutableStateOf("") }
    var isHistory by remember { mutableStateOf(false) }
    var personalRating by remember { mutableFloatStateOf(4f) }
    val mediaEntries = entries.filter { it.category == "MEDIA_TRACKER" }

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
                    Text("مسلسلات / أفلام تود مشاهدتها أو مراجعتها", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = mediaName, onValueChange = { mediaName = it }, label = { Text("اسم العمل الفني / المسلسل") })
                    OutlinedTextField(value = review, onValueChange = { review = it }, label = { Text("تعليقاتك أو انطباعاتك") })
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isHistory, onCheckedChange = { isHistory = it })
                        Text("لقد تم إنهاء مشاهدته كاملاً وجاهز للتقييم")
                    }

                    if (isHistory) {
                        Text("تقييمك للعمل:", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            for (i in 1..5) {
                                val active = i <= personalRating
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = if (active) Color(0xFFD69E2E) else Color.Gray,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clickable { personalRating = i.toFloat() }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (mediaName.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "MEDIA_TRACKER",
                                        title = mediaName,
                                        content = review,
                                        isCompleted = isHistory,
                                        rating = if (isHistory) personalRating else 0f,
                                        extraInfo = if (isHistory) "شاهدته" else "أرغب بمشاهدته"
                                    )
                                )
                                mediaName = ""
                                review = ""
                                isHistory = false
                                personalRating = 4f
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_media_button")
                    ) {
                        Text("حفظ في القائمة")
                    }
                }
            }
        }

        item {
            Text("قائمة الأعمال المضافة ومتابعة تقدم المشاهدة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (mediaEntries.isEmpty()) {
            item {
                Text("لا توجد أعمال في قائمتك بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(mediaEntries) { item ->
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
                                Text("الحالة: ${item.extraInfo ?: "غير محدد"}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        if (item.isCompleted) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { i ->
                                    Icon(
                                        imageVector = Icons.Filled.Star,
                                        contentDescription = null,
                                        tint = if (i + 1 <= item.rating) Color(0xFFD69E2E) else Color.LightGray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        if (item.content.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(item.content, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HealthLogScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var itemTitle by remember { mutableStateOf("") }
    var calorieCount by remember { mutableStateOf("") }
    val healthLogs = entries.filter { it.category == "HEALTH_LOG" }

    // Hydration counter - local simple state saved as a specific record or kept dynamically
    var waterGlassCount by remember { mutableIntStateOf(4) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Elegant water counter card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("متابع ري الهيدرات وشرب الماء اليومي", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "$waterGlassCount أكواب",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    Text("الهدف اليومي المستهدف: 8 أكواب (تم إنجاز ${(waterGlassCount * 100 / 8)}%)", style = MaterialTheme.typography.bodySmall)
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = { if (waterGlassCount > 0) waterGlassCount-- },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text("تقليل")
                        }
                        Button(
                            onClick = { waterGlassCount++ },
                            modifier = Modifier.testTag("add_water_button")
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("أضف كوب ماء")
                        }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("سجل عاداتك الغذائية والوجبات", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(value = itemTitle, onValueChange = { itemTitle = it }, label = { Text("الوجبة أو النشاط (مثال: فطور صحي)") })
                    OutlinedTextField(value = calorieCount, onValueChange = { calorieCount = it }, label = { Text("تقدير السعرات الحرارية أو التفاصيل") })
                    Button(
                        onClick = {
                            if (itemTitle.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "HEALTH_LOG",
                                        title = itemTitle,
                                        content = calorieCount
                                    )
                                )
                                itemTitle = ""
                                calorieCount = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_health_log_button")
                    ) {
                        Text("حفظ السجل الصحي")
                    }
                }
            }
        }

        item {
            Text("السجل الصحي للأيام الأخيرة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (healthLogs.isEmpty()) {
            item {
                Text("لا توجد وجبات أو عادات مسجلة اليوم.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(healthLogs) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            if (item.content.isNotEmpty()) {
                                Text("تفاصيل السعرات: ${item.content}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
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
