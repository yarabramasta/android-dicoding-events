package dev.ybrmst.dicodingevents

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.ybrmst.dicodingevents.presentation.ui.composables.screens.EventDetailScreen
import dev.ybrmst.dicodingevents.presentation.ui.composables.screens.MainScreen
import dev.ybrmst.dicodingevents.presentation.viewmodel.HomeViewModel
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = AppRoute.MainPage
  ) {
    composable<AppRoute.MainPage> { backstackEntry ->
      val parentEntry = remember(backstackEntry) {
        navController.getBackStackEntry(AppRoute.MainPage)
      }
      val homeVm = hiltViewModel<HomeViewModel>(parentEntry)
      MainScreen(navController, homeVm = homeVm)
    }

    composable<AppRoute.EventDetailPage> {
      val eventId = it.toRoute<AppRoute.EventDetailPage>().eventId
      EventDetailScreen(
        eventId = eventId,
        navController = navController,
      )
    }
  }
}

sealed class AppRoute {
  @Serializable
  data object MainPage : AppRoute()

  @Serializable
  data class EventDetailPage(val eventId: Int) : AppRoute()
}