package net.aucutt.lewinesnob.ui

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.WineBar
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.aucutt.lewinesnob.R
import net.aucutt.lewinesnob.data.Wine
import net.aucutt.lewinesnob.data.WineImageStore
import net.aucutt.lewinesnob.ui.theme.LeWineSnobTheme

@Composable
fun HomeScreen(
    onAddWine: () -> Unit,
    onListWines: () -> Unit,
    modifier: Modifier = Modifier,
    featuredWine: Wine? = null,
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (featuredWine != null) {
                FeaturedWineImage(
                    imageUri = featuredWine.imageUri,
                    modifier = Modifier
                        .widthIn(max = 320.dp)
                        .fillMaxWidth()
                        .height(220.dp)
                )
                Column(
                    modifier = Modifier.widthIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = featuredWine.brand,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(
                            R.string.rating_labeled,
                            if (featuredWine.rating > 0) {
                                featuredWine.rating.toString()
                            } else {
                                stringResource(R.string.unrated)
                            }
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Button(
                onClick = onAddWine,
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.add_wine))
            }
            OutlinedButton(
                onClick = onListWines,
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.list_wines))
            }
        }
    }
}

@Composable
private fun FeaturedWineImage(
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
            .clip(RoundedCornerShape(12.dp))
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
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.height(64.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    LeWineSnobTheme {
        HomeScreen(
            onAddWine = {},
            onListWines = {},
            featuredWine = Wine(
                id = "1",
                brand = "Château Example",
                type = "Red",
                varietal = "Cabernet Sauvignon",
                region = "Napa Valley",
                year = 2018,
                rating = 4,
            ),
        )
    }
}
