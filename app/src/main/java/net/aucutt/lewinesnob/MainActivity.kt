package net.aucutt.lewinesnob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import net.aucutt.lewinesnob.data.Wine
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
            val wines by wineViewModel.wines.collectAsState()
            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
            var featuredWine by remember { mutableStateOf<Wine?>(null) }
            LaunchedEffect(lifecycleOwner, wines) {
                lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    featuredWine = wines.randomOrNull()
                }
            }
            HomeScreen(
                onAddWine = { navController.navigate(AddWineRoute) },
                onListWines = { navController.navigate(ListWinesRoute) },
                featuredWine = featuredWine
            )
        }
        composable<AddWineRoute> {
            AddWineScreen(
                onBack = { navController.popBackStack() },
                onSave = { wine, notes ->
                    wineViewModel.addWine(wine, notes)
                    navController.popBackStack()
                },
                findCollision = { brand, type, varietal, year ->
                    wineViewModel.findCollision(brand, type, varietal, year)
                },
                onOpenExisting = { wine ->
                    navController.navigate(ListWinesRoute) {
                        popUpTo(AddWineRoute) { inclusive = true }
                    }
                    navController.navigate(ViewWineRoute(wine.id))
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
