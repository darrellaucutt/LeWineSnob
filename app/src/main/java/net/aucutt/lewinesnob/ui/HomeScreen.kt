package net.aucutt.lewinesnob.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.aucutt.lewinesnob.R
import net.aucutt.lewinesnob.ui.theme.LeWineSnobTheme

@Composable
fun HomeScreen(
    onAddWine: () -> Unit,
    onListWines: () -> Unit,
    modifier: Modifier = Modifier,
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
            Button(
                onClick = onAddWine,
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.add_wine))
            }
            Button(
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

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    LeWineSnobTheme {
        HomeScreen(onAddWine = {}, onListWines = {})
    }
}
