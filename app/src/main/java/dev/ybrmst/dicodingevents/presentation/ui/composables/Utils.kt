package dev.ybrmst.dicodingevents.presentation.ui.composables

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager
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

@OptIn(ExperimentalLayoutApi::class)
@Stable
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
  var isFocused by remember { mutableStateOf(false) }
  var keyboardAppearedSinceLastFocus by remember { mutableStateOf(false) }

  if (isFocused) {
    val imeIsVisible = WindowInsets.isImeVisible
    val focusManager = LocalFocusManager.current

    LaunchedEffect(imeIsVisible) {
      if (imeIsVisible) {
        keyboardAppearedSinceLastFocus = true
      } else if (keyboardAppearedSinceLastFocus) {
        focusManager.clearFocus()
      }
    }
  }

  onFocusEvent {
    if (isFocused != it.isFocused) {
      isFocused = it.isFocused
      if (isFocused) keyboardAppearedSinceLastFocus = false
    }
  }
}