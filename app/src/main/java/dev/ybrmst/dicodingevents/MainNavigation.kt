package dev.ybrmst.dicodingevents

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.ybrmst.dicodingevents.presentation.ui.composables.screens.EventDetailScreen
import dev.ybrmst.dicodingevents.presentation.ui.composables.screens.MainScreen
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = AppRoute.Main
  ) {
    composable<AppRoute.Main> { MainScreen(navController) }

    composable<AppRoute.EventDetail> {
      val eventId = it.toRoute<AppRoute.EventDetail>().eventId
      EventDetailScreen(
        eventId = eventId,
        navController = navController,
      )
    }
  }
}

sealed class AppRoute {
  @Serializable
  data object Main : AppRoute()

  @Serializable
  data class EventDetail(val eventId: Int) : AppRoute()
}