package net.aucutt.lewinesnob.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import net.aucutt.lewinesnob.R
import net.aucutt.lewinesnob.data.TastingNote
import net.aucutt.lewinesnob.data.Wine
import net.aucutt.lewinesnob.data.WineImageStore
import net.aucutt.lewinesnob.data.WineOptions
import net.aucutt.lewinesnob.ui.theme.LeWineSnobTheme
import java.io.File
import java.text.DateFormat
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWineScreen(
    onBack: () -> Unit,
    onSave: (Wine, List<TastingNote>) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val canTakePhoto = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)

    var brand by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf("") }
    var varietal by rememberSaveable { mutableStateOf("") }
    var region by rememberSaveable { mutableStateOf("") }
    var year by rememberSaveable { mutableStateOf("") }
    var rating by rememberSaveable { mutableIntStateOf(0) }
    var imageUri by rememberSaveable { mutableStateOf<String?>(null) }
    var pendingCameraUri by rememberSaveable { mutableStateOf<String?>(null) }
    var showPhotoSourcePicker by rememberSaveable { mutableStateOf(false) }
    var noteDraft by rememberSaveable { mutableStateOf("") }
    val addedNotes = remember { mutableStateListOf<NoteDraft>() }

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        imageUri = uri?.toString()
    }
    val takePicture = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = pendingCameraUri
        }
    }

    fun launchGallery() {
        photoPicker.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    fun launchCamera() {
        val uri = createCameraImageUri(context)
        pendingCameraUri = uri.toString()
        takePicture.launch(uri)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.add_wine)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BottlePhoto(
                imageUri = imageUri,
                onClick = {
                    if (canTakePhoto) {
                        showPhotoSourcePicker = true
                    } else {
                        launchGallery()
                    }
                }
            )
            OutlinedTextField(
                value = brand,
                onValueChange = { brand = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.brand)) },
                singleLine = true
            )
            DropdownField(
                label = stringResource(R.string.type),
                options = WineOptions.types,
                selected = type,
                onSelected = { type = it }
            )
            DropdownField(
                label = stringResource(R.string.varietal),
                options = WineOptions.varietals,
                selected = varietal,
                onSelected = { varietal = it }
            )
            OutlinedTextField(
                value = region,
                onValueChange = { region = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.region)) },
                singleLine = true
            )
            OutlinedTextField(
                value = year,
                onValueChange = { input ->
                    year = input.filter { it.isDigit() }.take(4)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = stringResource(R.string.year)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(
                text = stringResource(R.string.rating),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                )
            ) {
                (1..5).forEach { value ->
                    FilterChip(
                        selected = rating == value,
                        onClick = { rating = value },
                        label = { Text(text = value.toString()) }
                    )
                }
            }
            TastingNotesSection(
                notes = addedNotes,
                draft = noteDraft,
                onDraftChange = { noteDraft = it },
                onAddNote = {
                    val text = noteDraft.trim()
                    if (text.isNotEmpty()) {
                        addedNotes.add(
                            NoteDraft(
                                id = UUID.randomUUID().toString(),
                                date = System.currentTimeMillis(),
                                text = text,
                            )
                        )
                        noteDraft = ""
                    }
                },
                onDeleteNote = { addedNotes.remove(it) }
            )
            Button(
                onClick = {
                    val wineId = UUID.randomUUID().toString()
                    val notesToSave = buildList {
                        addAll(addedNotes)
                        val leftover = noteDraft.trim()
                        if (leftover.isNotEmpty()) {
                            add(
                                NoteDraft(
                                    id = UUID.randomUUID().toString(),
                                    date = System.currentTimeMillis(),
                                    text = leftover,
                                )
                            )
                        }
                    }
                    onSave(
                        Wine(
                            id = wineId,
                            brand = brand.trim(),
                            type = type,
                            varietal = varietal,
                            region = region.trim(),
                            year = year.toIntOrNull(),
                            rating = rating,
                            imageUri = imageUri
                        ),
                        notesToSave.map { draft ->
                            TastingNote(
                                id = draft.id,
                                wineId = wineId,
                                date = draft.date,
                                notes = draft.text,
                            )
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = brand.isNotBlank()
            ) {
                Text(text = stringResource(R.string.save_wine))
            }
        }
    }

    if (showPhotoSourcePicker) {
        PhotoSourceDialog(
            onTakePhoto = {
                showPhotoSourcePicker = false
                launchCamera()
            },
            onPickGallery = {
                showPhotoSourcePicker = false
                launchGallery()
            },
            onDismiss = { showPhotoSourcePicker = false }
        )
    }
}

private data class NoteDraft(
    val id: String,
    val date: Long,
    val text: String,
)

@Composable
private fun TastingNotesSection(
    notes: List<NoteDraft>,
    draft: String,
    onDraftChange: (String) -> Unit,
    onAddNote: () -> Unit,
    onDeleteNote: (NoteDraft) -> Unit,
) {
    val dateFormat = remember { DateFormat.getDateInstance(DateFormat.MEDIUM) }

    Text(
        text = stringResource(R.string.tasting_notes),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.titleSmall
    )
    notes.forEach { note ->
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 4.dp, bottom = 12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = dateFormat.format(Date(note.date)),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = note.text,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                IconButton(onClick = { onDeleteNote(note) }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(R.string.delete_note)
                    )
                }
            }
        }
    }
    OutlinedTextField(
        value = draft,
        onValueChange = onDraftChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = stringResource(R.string.notes)) },
        minLines = 3
    )
    OutlinedButton(
        onClick = onAddNote,
        modifier = Modifier.fillMaxWidth(),
        enabled = draft.isNotBlank()
    ) {
        Text(text = stringResource(R.string.add_note))
    }
}

@Composable
private fun PhotoSourceDialog(
    onTakePhoto: () -> Unit,
    onPickGallery: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.bottle_photo)) },
        text = {
            Column {
                TextButton(
                    onClick = onTakePhoto,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.take_photo))
                }
                TextButton(
                    onClick = onPickGallery,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.choose_from_gallery))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun BottlePhoto(
    imageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val imageBitmap by produceState<ImageBitmap?>(initialValue = null, imageUri) {
        value = imageUri?.let { uriString ->
            WineImageStore(context).open(uriString)?.use { stream ->
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            }
        }
    }

    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (imageBitmap != null) {
                Image(
                    bitmap = imageBitmap!!,
                    contentDescription = stringResource(R.string.bottle_photo),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(text = stringResource(R.string.bottle_photo_hint))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DropdownField(
    label: String,
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun createCameraImageUri(context: Context): Uri {
    val photoFile = File(context.cacheDir, "camera_${UUID.randomUUID()}.jpg")
    photoFile.createNewFile()
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        photoFile,
    )
}

@Preview(showBackground = true)
@Composable
private fun AddWineScreenPreview() {
    LeWineSnobTheme {
        AddWineScreen(onBack = {}, onSave = { _, _ -> })
    }
}
