package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.ybrmst.dicodingevents.AppRoute
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.EmptyItemsFallback
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.ErrorFallback
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.EventPreviewListItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.ShimmerItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberFlowWithLifecycle
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  vm: FavoritesViewModel = hiltViewModel()
) {
  val state by vm.state.collectAsStateWithLifecycle()
  val effect = rememberFlowWithLifecycle(vm.effect)
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect { action ->
      when (action) {
        is FavoritesContract.Effect.NavigateToDetail -> {
          navController.navigate(AppRoute.EventDetailPage(action.eventId))
        }
        is FavoritesContract.Effect.ShowToast -> {
          Toast.makeText(
            context,
            action.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.add(FavoritesContract.Event.OnRefresh) },
    modifier = Modifier.fillMaxSize()
  ) {
    FavoritesScreenContent(
      modifier = modifier.fillMaxSize(),
      events = state.favorites,
      isLoading = state.isFetching,
      hasError = state.error != null,
      error = state.error,
      onRetry = { vm.add(FavoritesContract.Event.OnRefresh) },
      onEventClick = { vm.add(FavoritesContract.Event.OnEventClicked(it.id)) },
      onFavoriteClick = {
        vm.add(FavoritesContract.Event.OnEventRemoved(it))
      }
    )
  }
}

@Composable
fun FavoritesScreenContent(
  modifier: Modifier = Modifier,
  events: List<EventPreview>,
  isLoading: Boolean,
  hasError: Boolean,
  error: String?,
  onRetry: () -> Unit,
  onEventClick: (EventPreview) -> Unit,
  onFavoriteClick: (EventPreview) -> Unit,
) {
  Box(modifier = modifier.fillMaxSize()) {
    if (!isLoading && !hasError) {
      if (events.isEmpty()) {
        EmptyItemsFallback(
          onRefresh = onRetry,
          message = "You have no favorite events."
        )
      } else {
        LazyColumn {
          buildHeadline()
          items(events) { event ->
            EventPreviewListItem(
              event = event,
              isFavorite = event.isFavorite,
              onClick = onEventClick,
              onFavoriteClick = onFavoriteClick
            )
          }
        }
      }
    }

    if (isLoading) {
      LazyColumn {
        buildHeadline()
        items(events) { ShimmerItem() }
      }
    }

    if (hasError) {
      ErrorFallback(message = error, onRetry = onRetry)
    }
  }
}

private fun LazyListScope.buildHeadline() {
  item {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      "Favorite Events",
      style = MaterialTheme.typography.headlineMedium,
      color = MaterialTheme.colorScheme.primary,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      "Here are your favorite events.",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoritesScreenPreview() {
  val events = listOf(
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
  ).distinctBy { it.id }

  AppTheme {
    Scaffold { innerPadding ->
      FavoritesScreenContent(
        modifier = Modifier.padding(innerPadding),
        events = events,
        isLoading = false,
        hasError = false,
        error = null,
        onRetry = {},
        onEventClick = {},
        onFavoriteClick = {}
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun FavoritesScreenLoadingPreview() {
  val events = listOf(
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
  ).distinctBy { it.id }

  AppTheme {
    Scaffold { innerPadding ->
      FavoritesScreenContent(
        modifier = Modifier.padding(innerPadding),
        events = events,
        isLoading = true,
        hasError = false,
        error = null,
        onRetry = {},
        onEventClick = {},
        onFavoriteClick = {}
      )
    }
  }
}