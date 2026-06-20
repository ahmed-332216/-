package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DaftariEntry
import com.example.ui.DaftariViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TodoScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var taskTitle by remember { mutableStateOf("") }
    var taskDesc by remember { mutableStateOf("") }
    val todos = entries.filter { it.category == "TODO" }

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
                    Text("إضافة مهمة جديدة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = taskTitle,
                        onValueChange = { taskTitle = it },
                        label = { Text("اسم المهمة") },
                        modifier = Modifier.fillMaxWidth().testTag("todo_add_input")
                    )
                    OutlinedTextField(
                        value = taskDesc,
                        onValueChange = { taskDesc = it },
                        label = { Text("تفاصيل المهمة (اختياري)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (taskTitle.isNotEmpty()) {
                                viewModel.insert(DaftariEntry(category = "TODO", title = taskTitle, content = taskDesc, isCompleted = false))
                                taskTitle = ""
                                taskDesc = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("todo_add_button")
                    ) {
                        Text("إضافة")
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("قائمة المهام اليومية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                val completedCount = todos.count { it.isCompleted }
                Text("إنجاز: $completedCount / ${todos.size}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }

        if (todos.isEmpty()) {
            item {
                Text("لا توجد مهام مدونة اليوم. استمتع بيومك!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(todos) { item ->
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
                                onCheckedChange = { check ->
                                    viewModel.update(item.copy(isCompleted = check))
                                },
                                modifier = Modifier.testTag("todo_checkbox_${item.id}")
                            )
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                                )
                                if (item.content.isNotEmpty()) {
                                    Text(item.content, style = MaterialTheme.typography.bodySmall, color = if (item.isCompleted) Color.Gray else Color.Gray)
                                }
                            }
                        }
                        IconButton(onClick = { viewModel.deleteById(item.id) }, modifier = Modifier.testTag("todo_delete_${item.id}")) {
                            Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlannerScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var title by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val schedules = entries.filter { it.category == "PLANNER" }

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
                    Text("إضافة فقرة للجدول اليومي", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("الوقت الفقرة (مثال: 08:30 ص)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("ملاحظات / نشاطات الفقرة") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && notes.isNotEmpty()) {
                                viewModel.insert(DaftariEntry(category = "PLANNER", title = title, content = notes))
                                title = ""
                                notes = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("حفظ بالجدول")
                    }
                }
            }
        }

        item {
            Text("المذكرة وجدول المواعيد اليومية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (schedules.isEmpty()) {
            item {
                Text("المذكرة فارغة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(schedules) { item ->
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
                                Icon(Icons.Filled.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun BudgetScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var isExpense by remember { mutableStateOf(true) }
    val budgetEntries = entries.filter { it.category == "BUDGET" }

    // Computations
    val totalIncome = budgetEntries.filter { it.extraInfo == "إيراد" }.sumOf { it.rating.toDouble() }
    val totalExpense = budgetEntries.filter { it.extraInfo == "مصروف" }.sumOf { it.rating.toDouble().coerceAtLeast(0.0) }
    val currentBalance = totalIncome - totalExpense

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Balance report
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("الرصيد المتبقي الكلي", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text("$currentBalance جنيه مصري", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("الإيرادات", style = MaterialTheme.typography.bodySmall)
                            Text("+$totalIncome ج.م", color = Color(0xFF48BB78), fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("المصروفات", style = MaterialTheme.typography.bodySmall)
                            Text("-$totalExpense ج.م", color = Color(0xFFF56565), fontWeight = FontWeight.Bold)
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
                    Text("إضافة معاملة مالية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { isExpense = true },
                            colors = ButtonDefaults.buttonColors(containerColor = if (isExpense) Color(0xFFF56565) else Color.Gray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("مصروف")
                        }
                        Button(
                            onClick = { isExpense = false },
                            colors = ButtonDefaults.buttonColors(containerColor = if (!isExpense) Color(0xFF48BB78) else Color.Gray),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("إيراد")
                        }
                    }
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("المصدر / البند (مثال: راتب، بقالة)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("المبلغ (بالجنيه المصري)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            val amount = amountText.toFloatOrNull()
                            if (title.isNotEmpty() && amount != null && amount > 0) {
                                val typeStr = if (isExpense) "مصروف" else "إيراد"
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "BUDGET",
                                        title = title,
                                        content = "تسجيل بند مالي",
                                        rating = amount,
                                        extraInfo = typeStr
                                    )
                                )
                                title = ""
                                amountText = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_budget_button")
                    ) {
                        Text("إضافة")
                    }
                }
            }
        }

        item {
            Text("السجل المالي للمعاملات الأخيرة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (budgetEntries.isEmpty()) {
            item {
                Text("لا توجد أي سجلات مالية حتى الآن.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(budgetEntries) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                imageVector = if (item.extraInfo == "إيراد") Icons.Filled.Add else Icons.Filled.Add,
                                contentDescription = null,
                                tint = if (item.extraInfo == "إيراد") Color(0xFF48BB78) else Color(0xFFF56565)
                            )
                            Column {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text(item.extraInfo ?: "غير محدد", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "${if (item.extraInfo == "إيراد") "+" else "-"}${item.rating} ج.م",
                                fontWeight = FontWeight.Bold,
                                color = if (item.extraInfo == "إيراد") Color(0xFF48BB78) else Color(0xFFF56565),
                                fontSize = 16.sp
                            )
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SleepScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var sleepDate by remember { mutableStateOf("") }
    var hoursStr by remember { mutableStateOf("") }
    var qualityStr by remember { mutableStateOf("ممتاز") }
    val sleepEntries = entries.filter { it.category == "SLEEP" }

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
                    Text("سجل ساعات نومك وجودته", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = sleepDate,
                        onValueChange = { sleepDate = it },
                        label = { Text("اليوم والتاريخ (مثال: الجمعة)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = hoursStr,
                        onValueChange = { hoursStr = it },
                        label = { Text("مدة النوم (عدد الساعات)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("كيف كانت جودة نومك؟", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        listOf("ممتاز", "جيد جداً", "مقبول", "سيء").forEach { q ->
                            val selected = q == qualityStr
                            Button(
                                onClick = { qualityStr = q },
                                colors = ButtonDefaults.buttonColors(containerColor = if (selected) MaterialTheme.colorScheme.primary else Color.Gray),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(2.dp)
                            ) {
                                Text(q, fontSize = 11.sp, maxLines = 1)
                            }
                        }
                    }
                    Button(
                        onClick = {
                            val hours = hoursStr.toFloatOrNull()
                            if (sleepDate.isNotEmpty() && hours != null) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "SLEEP",
                                        title = sleepDate,
                                        content = "نوم يومي هادئ",
                                        rating = hours,
                                        extraInfo = qualityStr
                                    )
                                )
                                sleepDate = ""
                                hoursStr = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_sleep_button")
                    ) {
                        Text("حفظ السجل")
                    }
                }
            }
        }

        item {
            Text("سجل النوم والراحة المتراكم", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (sleepEntries.isEmpty()) {
            item {
                Text("لا توجد سجلات نوم مسجلة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(sleepEntries) { item ->
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
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SuggestionChip(onClick = {}, label = { Text(item.extraInfo ?: "ممتاز") })
                                IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("مدة النوم المريحة: ${item.rating} ساعة متواصلة", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var workoutName by remember { mutableStateOf("") }
    var workoutDetails by remember { mutableStateOf("") }
    val workoutEntries = entries.filter { it.category == "WORKOUT" }

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
                    Text("وثق التمرين والبرنامج اليومي وسجل تقدمك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = workoutName,
                        onValueChange = { workoutName = it },
                        label = { Text("اسم التمرين (مثال: كارديو)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = workoutDetails,
                        onValueChange = { workoutDetails = it },
                        label = { Text("المجموعات والتكرارات والتفاصيل") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            if (workoutName.isNotEmpty() && workoutDetails.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "WORKOUT",
                                        title = workoutName,
                                        content = workoutDetails,
                                        isCompleted = false
                                    )
                                )
                                workoutName = ""
                                workoutDetails = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_workout_button")
                    ) {
                        Text("إضافة تمرين للجدول")
                    }
                }
            }
        }

        item {
            Text("برنامج الرياضة والتمارين المعتمدة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (workoutEntries.isEmpty()) {
            item {
                Text("لا توجد تمارين رياضية مضافة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(workoutEntries) { item ->
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
                                onCheckedChange = { check ->
                                    viewModel.update(item.copy(isCompleted = check))
                                }
                            )
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (item.isCompleted) Color.Gray else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(item.content, style = MaterialTheme.typography.bodySmall)
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
fun FuturePlansScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var goalTitle by remember { mutableStateOf("") }
    var stepsText by remember { mutableStateOf("") }
    val planEntries = entries.filter { it.category == "FUTURE_PLANS" }

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
                    Text("خطط ومشاريع طويلة المدى", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = goalTitle,
                        onValueChange = { goalTitle = it },
                        label = { Text("المشروع / الهدف الاستراتيجي") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = stepsText,
                        onValueChange = { stepsText = it },
                        label = { Text("الخطوات والمراحل والعمل المطلق") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    Button(
                        onClick = {
                            if (goalTitle.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "FUTURE_PLANS",
                                        title = goalTitle,
                                        content = stepsText
                                    )
                                )
                                goalTitle = ""
                                stepsText = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_future_plan_button")
                    ) {
                        Text("حفظ وتدشين")
                    }
                }
            }
        }

        item {
            Text("مخططات أهدافك الاستراتيجية والمشاريع", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (planEntries.isEmpty()) {
            item {
                Text("لا توجد خطط استراتيجية مضافة.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(planEntries) { item ->
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
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                    }
                }
            }
        }
    }
}
