package net.aucutt.lewinesnob

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import net.aucutt.lewinesnob.data.WineViewModel
import net.aucutt.lewinesnob.ui.AddWineScreen
import net.aucutt.lewinesnob.ui.HomeScreen
import net.aucutt.lewinesnob.ui.theme.LeWineSnobTheme

@Serializable
private data object HomeRoute

@Serializable
private data object AddWineRoute

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
                onListWines = { }
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
    }
}
