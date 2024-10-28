package dev.ybrmst.dicodingevents.presentation.ui.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import dev.ybrmst.dicodingevents.data.local.ThemeMode
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import kotlinx.coroutines.flow.Flow

@Composable
fun <T> rememberFlowWithLifecycle(
  flow: Flow<T>,
  lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
  minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
): Flow<T> {
  return remember(flow, lifecycle) {
    flow.flowWithLifecycle(
      lifecycle = lifecycle,
      minActiveState = minActiveState,
    )
  }
}

@Composable
fun isDarkThemeEnabled(themeMode: ThemeMode): Boolean {
  return when (themeMode) {
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
  }
}

@Composable
fun rememberUpdatedFavoriteStatus(
  events: List<EventPreview>,
  favorites: List<EventPreview>,
): List<EventPreview> {
  return remember(events, favorites) {
    events.map { event ->
      event.copy(isFavorite = favorites.any { it.id == event.id })
    }
  }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.scopedViewModel(
  navController: NavController,
): T {
  val navGraphRoute = destination.parent?.route ?: return hiltViewModel()

  val parentEntry = remember(this) {
    navController.getBackStackEntry(navGraphRoute)
  }

  return hiltViewModel(parentEntry)
}