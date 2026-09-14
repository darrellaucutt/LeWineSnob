package net.aucutt.lewinesnob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import net.aucutt.lewinesnob.data.WineViewModel
import net.aucutt.lewinesnob.ui.AddWineScreen
import net.aucutt.lewinesnob.ui.HomeScreen
import net.aucutt.lewinesnob.ui.ListWinesScreen
import net.aucutt.lewinesnob.ui.theme.LeWineSnobTheme

@Serializable
private data object HomeRoute

@Serializable
private data object AddWineRoute

@Serializable
private data object ListWinesRoute

@Serializable
private data class ViewWineRoute(val wineId: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LeWineSnobTheme {
                LeWineSnobApp()
            }
        }
    }
}

@Composable
private fun LeWineSnobApp(wineViewModel: WineViewModel = viewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = HomeRoute
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onAddWine = { navController.navigate(AddWineRoute) },
                onListWines = { navController.navigate(ListWinesRoute) }
            )
        }
        composable<AddWineRoute> {
            AddWineScreen(
                onBack = { navController.popBackStack() },
                onSave = { wine, notes ->
                    wineViewModel.addWine(wine, notes)
                    navController.popBackStack()
                }
            )
        }
        composable<ListWinesRoute> {
            val wines by wineViewModel.wines.collectAsState()
            ListWinesScreen(
                wines = wines,
                onBack = { navController.popBackStack() },
                onWineClick = { wine -> navController.navigate(ViewWineRoute(wine.id)) },
                onDeleteWine = { wine -> wineViewModel.deleteWine(wine.id) }
            )
        }
        composable<ViewWineRoute> { backStackEntry ->
            val wineId = backStackEntry.toRoute<ViewWineRoute>().wineId
            val wines by wineViewModel.wines.collectAsState()
            val wine = wines.find { it.id == wineId }
            val notes by wineViewModel.notesForWine(wineId).collectAsState(initial = emptyList())
            if (wine == null) {
                LaunchedEffect(wineId) {
                    navController.popBackStack()
                }
            } else {
                AddWineScreen(
                    existingWine = wine,
                    existingNotes = notes,
                    onBack = { navController.popBackStack() },
                    onSave = { _, _ -> },
                    onRatingChange = { rating -> wineViewModel.updateRating(wine.id, rating) },
                    onNoteAdded = { text -> wineViewModel.addNote(wine.id, text) },
                )
            }
        }
    }
}
