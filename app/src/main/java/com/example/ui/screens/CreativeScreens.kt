package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

@Composable
fun QuotesScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>, isPersonalOnly: Boolean) {
    var quoteText by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    
    // Filter quotes: regular or personal
    val quotes = entries.filter { 
        it.category == "QUOTES" && (if (isPersonalOnly) it.extraInfo == "PERSONAL" else it.extraInfo == "GENERAL")
    }

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
                    Text(
                        text = if (isPersonalOnly) "دوّن مقولة ذات صلة بروحك وتحفيزك" else "سجل حكمة ومأثورة علمية وأدبية أعجبتك",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(
                        value = quoteText,
                        onValueChange = { quoteText = it },
                        label = { Text("نص الحكمة أو الاقتباس") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("قائل المقولة / المصدر") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (quoteText.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "QUOTES",
                                        title = quoteText,
                                        content = author,
                                        extraInfo = if (isPersonalOnly) "PERSONAL" else "GENERAL"
                                    )
                                )
                                quoteText = ""
                                author = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_quote_button")
                    ) {
                        Text("إضافة")
                    }
                }
            }
        }

        item {
            Text(
                text = if (isPersonalOnly) "اقتباساتك المفعمة بالإلهام الشخصي" else "روائع الحكم والاقتباسات العلمية والأدبية",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (quotes.isEmpty()) {
            item {
                Text("القائمة فارغة. أضف بعض الكلمات التي تمنحك الأمل والانطلاق!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(quotes) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "“${item.title}”",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                            }
                        }
                        if (item.content.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "— ${item.content}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LinksScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var linkTitle by remember { mutableStateOf("") }
    var urlLink by remember { mutableStateOf("") }
    var domainType by remember { mutableStateOf("") }
    
    val links = entries.filter { it.category == "LINKS" }

    // Dynamic categories list
    val categoriesList = listOf("الكل") + links.map { it.extraInfo ?: "روابط عامة" }.distinct().filter { it.isNotEmpty() }
    var selectedCategoryTab by remember { mutableStateOf("الكل") }

    val filteredLinks = if (selectedCategoryTab == "الكل") {
        links
    } else {
        links.filter { (it.extraInfo ?: "روابط عامة") == selectedCategoryTab }
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
                    Text("أضف رابطا هاما ترجع إليه يوميا", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = linkTitle, 
                        onValueChange = { linkTitle = it }, 
                        label = { Text("اسم الموقع / الرابط (مثال: منصة حسوب)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = urlLink, 
                        onValueChange = { urlLink = it }, 
                        label = { Text("عنوان الرابط المباشر (URL)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = domainType, 
                        onValueChange = { domainType = it }, 
                        label = { Text("تصنيف الرابط (مثال: دراسة، برمجة، ترفيه)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (linkTitle.isNotEmpty() && urlLink.isNotEmpty()) {
                                val cleanUrl = if (!urlLink.startsWith("http://") && !urlLink.startsWith("https://")) {
                                    "https://$urlLink"
                                } else {
                                    urlLink
                                }
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "LINKS",
                                        title = linkTitle,
                                        content = cleanUrl,
                                        extraInfo = domainType.ifEmpty { "روابط عامة" }
                                    )
                                )
                                linkTitle = ""
                                urlLink = ""
                                domainType = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_link_button")
                    ) {
                        Text("حفظ الرابط الإلكتروني")
                    }
                }
            }
        }

        // Category filter chips
        if (categoriesList.size > 1) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    categoriesList.forEach { cat ->
                        FilterChip(
                            selected = selectedCategoryTab == cat,
                            onClick = { selectedCategoryTab = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }
        }

        item {
            Text("قائمتك المنظمة للروابط الرقمية المفضلة (انقر للفتح)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (filteredLinks.isEmpty()) {
            item {
                Text("لا توجد روابط مسجلة في هذا التصنيف بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(filteredLinks) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW, Uri.parse(item.content))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(item.content, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SuggestionChip(onClick = {}, label = { Text(item.extraInfo ?: "روابط عامة") })
                                IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "حذف الرابط", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExperiencesScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>, isTechOnly: Boolean) {
    var title by remember { mutableStateOf("") }
    var notesText by remember { mutableStateOf("") }
    val exps = entries.filter { 
        it.category == "EXPERIENCES" && (if (isTechOnly) it.extraInfo == "TECH" else it.extraInfo == "PERSONAL")
    }

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
                    Text(
                        text = if (isTechOnly) "سجل تجربة برمجية أو عقبة تقنية تمكنت منها" else "سجل خطوة وتجربة حياتية شخصية هامة",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("عنوان كلي مختصر للموضوع") })
                    OutlinedTextField(value = notesText, onValueChange = { notesText = it }, label = { Text("تفاصيل وتأملات التجربة الهامة وما تعلمته...") }, minLines = 3)
                    Button(
                        onClick = {
                            if (title.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "EXPERIENCES",
                                        title = title,
                                        content = notesText,
                                        extraInfo = if (isTechOnly) "TECH" else "PERSONAL"
                                    )
                                )
                                title = ""
                                notesText = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_exp_button")
                    ) {
                        Text("حفظ بالسجل")
                    }
                }
            }
        }

        item {
            Text(
                text = if (isTechOnly) "سجل التجارب والحلول البرمجية الفنية" else "سجل التجارب والقناعات الحياتية الشخصية",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (exps.isEmpty()) {
            item {
                Text("السجل فارغ. ابدأ بتسجيل أول تجربة لتستفيد وتراجع خطواتك اللاحقة بذكاء!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(exps) { item ->
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
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoDiaryScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var caption by remember { mutableStateOf("") }
    var titleText by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    
    val diaryPhotos = entries.filter { it.category == "PHOTO_DIARY" }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedUri = uri
        }
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
                    Text("يوميات مصورة مع تفاصيل (شخصية ويومية)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    OutlinedTextField(
                        value = titleText, 
                        onValueChange = { titleText = it }, 
                        label = { Text("موضوع اليومية المصورة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = caption, 
                        onValueChange = { caption = it }, 
                        label = { Text("تعليقك ووصفك ليومك في هذه الصورة...") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { imagePickerLauncher.launch("image/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (selectedUri == null) "التقط/اختر صورة" else "تم اختيار صورة ✓")
                        }
                        
                        Button(
                            onClick = {
                                if (titleText.isNotEmpty() && selectedUri != null) {
                                    viewModel.insert(
                                        DaftariEntry(
                                            category = "PHOTO_DIARY",
                                            title = titleText,
                                            content = caption,
                                            imageUrl = selectedUri.toString()
                                        )
                                    )
                                    titleText = ""
                                    caption = ""
                                    selectedUri = null
                                }
                            },
                            enabled = selectedUri != null && titleText.isNotEmpty(),
                            modifier = Modifier.testTag("add_photo_diary_button")
                        ) {
                            Text("حفظ يوميات مصورة")
                        }
                    }
                }
            }
        }

        item {
            Text("دفتر تتبع اليوميات المصورة اليومية الخاصة بك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (diaryPhotos.isEmpty()) {
            item {
                Text("لا توجد يوميات مصورة مسجلة بعد. أضف لقطاتك المميزة يومياً!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(diaryPhotos) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        if (item.imageUrl.isNullOrEmpty() || item.imageUrl == "img_companion_cover" || item.imageUrl == "img_profile_avatar") {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .background(MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Gray)
                                    Text("صورة تذكارية غير محملة", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(model = item.imageUrl),
                                contentDescription = item.title,
                                modifier = Modifier.fillMaxWidth().height(180.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
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
                            Text(item.content, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RecipesScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>, recipeSubtype: String) {
    // Subtypes: "GENERAL" (وصفات), "PERSONAL" (وصفات شخصية), "HEALTHY" (وصفات صحية)
    var recipeName by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    val recipes = entries.filter { 
        it.category == "RECIPES" && (
            if (recipeSubtype == "HEALTHY") it.extraInfo == "صحية"
            else if (recipeSubtype == "PERSONAL") it.extraInfo == "شخصية"
            else it.extraInfo != "صحية" && it.extraInfo != "شخصية"
        )
    }

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
                    Text(
                        text = if (recipeSubtype == "HEALTHY") "أضف وصفة صحية وجديدة للريجيم"
                               else if (recipeSubtype == "PERSONAL") "أكتب سر طبختك ووصفاتك الخاصة والمجربة"
                               else "سجل وصفة أكل شهية مع الخطوات المقترحة",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    OutlinedTextField(value = recipeName, onValueChange = { recipeName = it }, label = { Text("اسم الوجبات / الأكلة") })
                    OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("المكونات وسر التحضير خطوة بخطوة...") }, minLines = 3)
                    Button(
                        onClick = {
                            if (recipeName.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "RECIPES",
                                        title = recipeName,
                                        content = steps,
                                        extraInfo = if (recipeSubtype == "HEALTHY") "صحية"
                                                    else if (recipeSubtype == "PERSONAL") "شخصية"
                                                    else "عامة"
                                    )
                                )
                                recipeName = ""
                                steps = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_recipe_button")
                    ) {
                        Text("حفظ كارت الوصفة")
                    }
                }
            }
        }

        item {
            Text(
                text = if (recipeSubtype == "HEALTHY") "أرشيف الوصفات الصحية والرجيم"
                       else if (recipeSubtype == "PERSONAL") "دفتر أسرار طبخاتك الشخصية الخاصة"
                       else "كتالوج وصفات الغذاء والحلويات",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        if (recipes.isEmpty()) {
            item {
                Text("لا توجد وصفات مدونة في هذا الأرشيف بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(recipes) { item ->
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
                        Text(item.content, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Justify)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AssistChip(onClick = {}, label = { Text(item.extraInfo ?: "وصفة") })
                        }
                    }
                }
            }
        }
    }
}
