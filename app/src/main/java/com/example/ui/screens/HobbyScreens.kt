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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun GalleryScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var imageName by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    
    val galleryEntries = entries.filter { it.category == "GALLERY" && !it.imageUrl.isNullOrEmpty() && it.imageUrl != "img_companion_cover" }

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
                    Text("أضف صورة لمعرضك من جهازك الخاص", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    OutlinedTextField(
                        value = imageName,
                        onValueChange = { imageName = it },
                        label = { Text("عنوان الصورة / الموضوع") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("تعليق أو وصف ملهم للفكرة والأسباب") },
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
                            Text(if (selectedUri == null) "اختر صورة من الجهاز" else "تم اختيار صورة ✓")
                        }
                        
                        Button(
                            onClick = {
                                if (imageName.isNotEmpty() && selectedUri != null) {
                                    viewModel.insert(
                                        DaftariEntry(
                                            category = "GALLERY",
                                            title = imageName,
                                            content = caption,
                                            imageUrl = selectedUri.toString()
                                        )
                                    )
                                    imageName = ""
                                    caption = ""
                                    selectedUri = null
                                }
                            },
                            enabled = selectedUri != null && imageName.isNotEmpty(),
                            modifier = Modifier.testTag("add_gallery_button")
                        ) {
                            Text("حفظ لمعرض الصور")
                        }
                    }
                }
            }
        }

        item {
            Text("معرض الصور المنظم الخاص بك (صور من جهازك)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (galleryEntries.isEmpty()) {
            item {
                Text("لا توجد صور مضافة من جهازك بعد. ارفع أول صورة لتظهر هنا!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(galleryEntries) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        Image(
                            painter = rememberAsyncImagePainter(model = item.imageUrl),
                            contentDescription = item.title,
                            modifier = Modifier.fillMaxWidth().height(180.dp),
                            contentScale = ContentScale.Crop
                        )
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
                            Text(item.content, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LibraryScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var bookTitle by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var isRead by remember { mutableStateOf(false) }
    val books = entries.filter { it.category == "LIBRARY" }

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
                    Text("أضف كتاباً لمكتبتك الرقمية الفاخرة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = bookTitle,
                        onValueChange = { bookTitle = it },
                        label = { Text("عنوان الكتاب") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = author,
                        onValueChange = { author = it },
                        label = { Text("المؤلف") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("ملخص مقتضب أو ملاحظات سريعة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isRead, onCheckedChange = { isRead = it })
                        Text("لقد تم إنهاء مراجعته وقراءته كاملاً")
                    }
                    Button(
                        onClick = {
                            if (bookTitle.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "LIBRARY",
                                        title = bookTitle,
                                        content = notes,
                                        extraInfo = author,
                                        isCompleted = isRead
                                    )
                                )
                                bookTitle = ""
                                author = ""
                                notes = ""
                                isRead = false
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_book_button")
                    ) {
                        Text("حفظ في المكتبة")
                    }
                }
            }
        }

        item {
            Text("مكتبتك الشخصية والمجلات المفضلة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (books.isEmpty()) {
            item {
                Text("المكتبة فارغة. أضف كتبك المفضلة لبناء رصيدك المعرفي!", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
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
                            Column {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("للمؤلف: ${item.extraInfo ?: "غير معروف"}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text(if (item.isCompleted) "تمت قراءته" else "يراد قراءته") }
                                )
                                IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                                }
                            }
                        }
                        if (item.content.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(item.content, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MusicScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var songName by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var selectedAudioUri by remember { mutableStateOf<Uri?>(null) }

    // Only display local music files picked from the device
    val musicEntries = entries.filter { it.category == "MUSIC" && !it.imageUrl.isNullOrEmpty() && it.imageUrl.startsWith("content:") }

    var mediaPlayer by remember { mutableStateOf<android.media.MediaPlayer?>(null) }
    var activePlayingId by remember { mutableStateOf<Int?>(null) }

    val audioPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedAudioUri = uri
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
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
                    Text("أضف ملف صوتي من جهازك الخاص", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    
                    OutlinedTextField(
                        value = songName,
                        onValueChange = { songName = it },
                        label = { Text("عنوان المقطع الصوتي") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = artist,
                        onValueChange = { artist = it },
                        label = { Text("اسم المؤدي / الفنان") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { audioPickerLauncher.launch("audio/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (selectedAudioUri == null) "اختر ملفاً صوتياً من الجهاز" else "تم اختيار الملف ✓")
                        }

                        Button(
                            onClick = {
                                if (songName.isNotEmpty() && selectedAudioUri != null) {
                                    viewModel.insert(
                                        DaftariEntry(
                                            category = "MUSIC",
                                            title = songName,
                                            content = "ملف صوتي محلي ملهم",
                                            extraInfo = artist,
                                            imageUrl = selectedAudioUri.toString()
                                        )
                                    )
                                    songName = ""
                                    artist = ""
                                    selectedAudioUri = null
                                }
                            },
                            enabled = selectedAudioUri != null && songName.isNotEmpty(),
                            modifier = Modifier.testTag("add_song_button")
                        ) {
                            Text("حفظ الملف")
                        }
                    }
                }
            }
        }

        item {
            Text("قائمة التشغيل والأصوات الحية الخاصة بك (من جهازك فقط)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (musicEntries.isEmpty()) {
            item {
                Text("لا توجد مقاطع صوتية مضافة من جهازك بعد. الرجاء إضافة ملفاتك الصوتية الحالية لتشغيلها.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(musicEntries) { item ->
                val uriString = item.imageUrl
                val isPlaying = activePlayingId == item.id

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPlaying) MaterialTheme.colorScheme.primaryContainer 
                                         else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconButton(onClick = {
                                try {
                                    if (isPlaying) {
                                        mediaPlayer?.stop()
                                        mediaPlayer?.release()
                                        mediaPlayer = null
                                        activePlayingId = null
                                    } else {
                                        mediaPlayer?.stop()
                                        mediaPlayer?.release()
                                        mediaPlayer = android.media.MediaPlayer.create(context, Uri.parse(uriString))
                                        mediaPlayer?.setOnCompletionListener {
                                            activePlayingId = null
                                        }
                                        mediaPlayer?.start()
                                        activePlayingId = item.id
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Filled.Star else Icons.Filled.PlayArrow,
                                    contentDescription = "تشغيل / إيقاف مؤقت",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("الفنان: ${item.extraInfo ?: "غير معروف"}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                        IconButton(onClick = {
                            if (isPlaying) {
                                mediaPlayer?.stop()
                                mediaPlayer?.release()
                                mediaPlayer = null
                                activePlayingId = null
                            }
                            viewModel.deleteById(item.id)
                        }) {
                            Icon(Icons.Filled.Delete, contentDescription = "حذف", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TravelScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var destination by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val travelEntries = entries.filter { it.category == "TRAVEL" }

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
                    Text("خطط لرحلتك القادمة بكل أريحية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = destination,
                        onValueChange = { destination = it },
                        label = { Text("الواجهة / المدينة التي ترغب بزيارتها") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("خطة الزيارة، الأماكن والمعالم الهامة") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            if (destination.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "TRAVEL",
                                        title = destination,
                                        content = notes,
                                        isCompleted = false
                                    )
                                )
                                destination = ""
                                notes = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_travel_button")
                    ) {
                        Text("حفظ لوجهة السفر")
                    }
                }
            }
        }

        item {
            Text("قائمتي لأماكن وخطط السفر الممتعة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (travelEntries.isEmpty()) {
            item {
                Text("لا توجد خطط سفر مدونة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(travelEntries) { item ->
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
fun GameTrackerScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var gameTitle by remember { mutableStateOf("") }
    var gameStatus by remember { mutableStateOf("قيد اللعب") }
    val games = entries.filter { it.category == "GAMES" }

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
                    Text("سجل ألعابك وتحدياتك الرقمية", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = gameTitle,
                        onValueChange = { gameTitle = it },
                        label = { Text("اسم اللعبة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("الحالة مراجعة التقدم الحالية:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("قيد اللعب", "مكتملة", "أرغب بالتجريب").forEach { status ->
                            val active = status == gameStatus
                            Button(
                                onClick = { gameStatus = status },
                                colors = ButtonDefaults.buttonColors(containerColor = if (active) MaterialTheme.colorScheme.primary else Color.Gray),
                                modifier = Modifier.weight(1f),
                                contentPadding = PaddingValues(2.dp)
                            ) {
                                Text(status, fontSize = 11.sp)
                            }
                        }
                    }
                    Button(
                        onClick = {
                            if (gameTitle.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "GAMES",
                                        title = gameTitle,
                                        content = "متابعة ألعاب إلكترونية",
                                        extraInfo = gameStatus,
                                        isCompleted = gameStatus == "مكتملة"
                                    )
                                )
                                gameTitle = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_game_button")
                    ) {
                        Text("حفظ بقائمة الألعاب")
                    }
                }
            }
        }

        item {
            Text("سجل ألعاب الفيديو والترفيه المفضلة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (games.isEmpty()) {
            item {
                Text("لا توجد ألعاب مضافة بعد.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(games) { item ->
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
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(item.extraInfo ?: "قيد اللعب", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
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
fun FavImagesScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var imageName by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    
    val favImages = entries.filter { it.category == "FAV_IMAGES" && !it.imageUrl.isNullOrEmpty() }

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
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("تعيين صورة مفضلة جديدة", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                    
                    OutlinedTextField(
                        value = imageName,
                        onValueChange = { imageName = it },
                        label = { Text("عنوان الصورة المفضلة") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = caption,
                        onValueChange = { caption = it },
                        label = { Text("لماذا هي مميزة بالنسبة لك؟") },
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
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                        ) {
                            Icon(Icons.Filled.Favorite, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (selectedUri == null) "اختر الصورة المفضلة" else "تم الاختيار ✓")
                        }
                        
                        Button(
                            onClick = {
                                if (imageName.isNotEmpty() && selectedUri != null) {
                                    viewModel.insert(
                                        DaftariEntry(
                                            category = "FAV_IMAGES",
                                            title = imageName,
                                            content = caption,
                                            imageUrl = selectedUri.toString()
                                        )
                                    )
                                    imageName = ""
                                    caption = ""
                                    selectedUri = null
                                }
                            },
                            enabled = selectedUri != null && imageName.isNotEmpty()
                        ) {
                            Text("إضافة للمفضلة")
                        }
                    }
                }
            }
        }

        item {
            Text("معرض الصور المفضلة (من جهازك)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (favImages.isEmpty()) {
            item {
                Text("المعرض فارغ حالياً! حدد واشحن صورك المفضلة من محتويات جهازك لتتألق هنا.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(favImages) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column {
                        Image(
                            painter = rememberAsyncImagePainter(model = item.imageUrl),
                            contentDescription = item.title,
                            modifier = Modifier.fillMaxWidth().height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                IconButton(onClick = { viewModel.deleteById(item.id) }) {
                                    Icon(Icons.Filled.Delete, contentDescription = "حذف من المفضلة", tint = Color.Red)
                                }
                            }
                            if (item.content.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(item.content, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HobbyLogScreen(viewModel: DaftariViewModel, entries: List<DaftariEntry>) {
    var hobbyTitle by remember { mutableStateOf("") }
    var detail by remember { mutableStateOf("") }
    val hobbies = entries.filter { it.category == "HOBBY_LOG" }

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
                    Text("سجل ومتابعة الهوايات والإبداع", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = hobbyTitle,
                        onValueChange = { hobbyTitle = it },
                        label = { Text("المجال / الهواية (مثال: الرسم والتشكيل)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = detail,
                        onValueChange = { detail = it },
                        label = { Text("الأنشطة التي مارستها ومجالات الابتكار") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                    Button(
                        onClick = {
                            if (hobbyTitle.isNotEmpty()) {
                                viewModel.insert(
                                    DaftariEntry(
                                        category = "HOBBY_LOG",
                                        title = hobbyTitle,
                                        content = detail
                                    )
                                )
                                hobbyTitle = ""
                                detail = ""
                            }
                        },
                        modifier = Modifier.align(Alignment.End).testTag("add_hobby_button")
                    ) {
                        Text("حفظ السجل")
                    }
                }
            }
        }

        item {
            Text("دفتر تتبع ممارسة أنشطتك وهواياتك", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (hobbies.isEmpty()) {
            item {
                Text("لا توجد سجلات مضافة للهوايات.", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
        } else {
            items(hobbies) { item ->
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
