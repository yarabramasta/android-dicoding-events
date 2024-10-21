package dev.ybrmst.dicoding_events

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.ybrmst.dicoding_events.ui.composables.screens.MainScreen
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = AppRoute.Main
  ) {
    composable<AppRoute.Main> { MainScreen(navController) }
  }
}

sealed class AppRoute {
  @Serializable
  data object Main : AppRoute()
}