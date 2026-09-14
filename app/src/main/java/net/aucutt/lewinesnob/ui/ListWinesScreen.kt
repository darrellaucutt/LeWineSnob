package net.aucutt.lewinesnob.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.aucutt.lewinesnob.R
import net.aucutt.lewinesnob.data.Wine
import net.aucutt.lewinesnob.data.WineImageStore
import net.aucutt.lewinesnob.ui.theme.LeWineSnobTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListWinesScreen(
    wines: List<Wine>,
    onBack: () -> Unit,
    onWineClick: (Wine) -> Unit,
    onDeleteWine: (Wine) -> Unit,
    modifier: Modifier = Modifier,
) {
    var query by rememberSaveable { mutableStateOf("") }
    var wineToDelete by remember { mutableStateOf<Wine?>(null) }
    val filteredWines = wines.filter { it.matches(query) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.list_wines)) },
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
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text(text = stringResource(R.string.search_wines)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = null
                    )
                },
                singleLine = true
            )
            if (filteredWines.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(
                            if (wines.isEmpty()) R.string.no_wines else R.string.no_matching_wines
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredWines, key = { it.id }) { wine ->
                        WineListItem(
                            wine = wine,
                            onClick = { onWineClick(wine) },
                            onDelete = { wineToDelete = wine }
                        )
                    }
                }
            }
        }
    }

    wineToDelete?.let { wine ->
        AlertDialog(
            onDismissRequest = { wineToDelete = null },
            title = { Text(text = stringResource(R.string.delete_wine)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.delete_wine_confirm,
                        wine.brand.ifBlank { stringResource(R.string.list_wines) }
                    )
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteWine(wine)
                        wineToDelete = null
                    }
                ) {
                    Text(text = stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { wineToDelete = null }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
private fun WineListItem(
    wine: Wine,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val details = listOfNotNull(
        wine.type.takeIf { it.isNotBlank() },
        wine.varietal.takeIf { it.isNotBlank() },
        wine.region.takeIf { it.isNotBlank() },
        wine.year?.toString(),
    ).joinToString(" · ")

    OutlinedCard(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            WineThumbnail(imageUri = wine.imageUri)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = wine.brand,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (details.isNotEmpty()) {
                    Text(
                        text = details,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = if (wine.rating > 0) {
                        stringResource(R.string.rating_value, wine.rating)
                    } else {
                        stringResource(R.string.unrated)
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.delete_wine)
                )
            }
        }
    }
}

@Composable
private fun WineThumbnail(
    imageUri: String?,
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

    Box(
        modifier = modifier
            .size(72.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer),
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
            Icon(
                imageVector = Icons.Outlined.WineBar,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

private fun Wine.matches(query: String): Boolean {
    if (query.isBlank()) return true
    val haystack = listOf(
        brand,
        type,
        varietal,
        region,
        year?.toString().orEmpty(),
    )
    return haystack.any { it.contains(query, ignoreCase = true) }
}

@Preview(showBackground = true)
@Composable
private fun ListWinesScreenPreview() {
    LeWineSnobTheme {
        ListWinesScreen(
            wines = listOf(
                Wine(
                    id = "1",
                    brand = "Château Example",
                    type = "Red",
                    varietal = "Cabernet Sauvignon",
                    region = "Napa Valley",
                    year = 2018,
                    rating = 4,
                ),
                Wine(
                    id = "2",
                    brand = "Domaine Preview",
                    type = "White",
                    varietal = "Chardonnay",
                    region = "Burgundy",
                    year = 2020,
                    rating = 5,
                ),
            ),
            onBack = {},
            onWineClick = {},
            onDeleteWine = {},
        )
    }
}
