package dev.ybrmst.dicoding_events.ui.composables.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.ybrmst.dicoding_events.ui.composables.screens.EventDetailScreen
import dev.ybrmst.dicoding_events.ui.composables.screens.MainScreen

@Composable
fun AppNavHost() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = MainRoute
  ) {
    composable<MainRoute> { MainScreen(navController) }

    composable<EventDetailRoute> {
      val eventId = it.toRoute<EventDetailRoute>().eventId
      EventDetailScreen(
        eventId = eventId,
        navController = navController
      )
    }
  }
}