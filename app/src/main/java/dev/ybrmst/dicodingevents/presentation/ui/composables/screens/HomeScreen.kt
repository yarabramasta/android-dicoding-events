package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
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
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.EmptyItemsFallback
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.ErrorFallback
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.EventPreviewCard
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.EventPreviewListItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.ShimmerBox
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.ShimmerItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberFlowWithLifecycle
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberUpdatedFavoriteStatus
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesViewModel
import dev.ybrmst.dicodingevents.presentation.viewmodel.HomeContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  vm: HomeViewModel = hiltViewModel(),
  favsVm: FavoritesViewModel,
  onEventClick: (EventPreview) -> Unit = {},
) {
  val state by vm.state.collectAsStateWithLifecycle()
  val effect = rememberFlowWithLifecycle(vm.effect)
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect { action ->
      when (action) {
        is HomeContract.Effect.ShowToast -> {
          Toast.makeText(
            context,
            action.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  val favs by favsVm.state.collectAsStateWithLifecycle()
  val upcomingEvents = rememberUpdatedFavoriteStatus(
    state.upcomingEvents,
    favs.events
  )
  val finishedEvents = rememberUpdatedFavoriteStatus(
    state.finishedEvents,
    favs.events
  )

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.add(HomeContract.Event.OnRefresh) },
    modifier = Modifier.fillMaxSize()
  ) {
    HomeScreenContent(
      modifier = modifier.fillMaxSize(),
      upcomingEvents = upcomingEvents,
      finishedEvents = finishedEvents,
      isLoading = state.isFetching || state.isRefreshing,
      hasError = state.error != null,
      error = state.error,
      onRetry = {
        vm.add(HomeContract.Event.OnRefresh)
      },
      onEventClick = onEventClick,
      onFavoriteClick = { event ->
        if (event.isFavorite) {
          favsVm.add(FavoritesContract.Event.OnEventRemoved(event))
        } else {
          favsVm.add(FavoritesContract.Event.OnEventAdded(event))
        }
        vm.add(HomeContract.Event.OnFavoriteChanged(event.isFavorite))
      },
    )
  }
}

@Composable
private fun HomeScreenContent(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  hasError: Boolean,
  error: String?,
  onRetry: () -> Unit,
  upcomingEvents: List<EventPreview>,
  finishedEvents: List<EventPreview>,
  onEventClick: (EventPreview) -> Unit,
  onFavoriteClick: (EventPreview) -> Unit,
) {
  Box(modifier = modifier.fillMaxSize()) {
    if (!isLoading && !hasError) {
      if (upcomingEvents.isEmpty() && finishedEvents.isEmpty()) {
        EmptyItemsFallback(onRefresh = onRetry)
      } else {
        LazyColumn {
          buildHeadline()
          buildUpcomingEventsLayout(upcomingEvents) { event ->
            EventPreviewCard(
              event = event,
              onClick = onEventClick,
              onFavoriteClick = { onFavoriteClick(event) },
              isFavorite = event.isFavorite
            )
          }
          buildFinishedEventsLayout(finishedEvents) { event ->
            EventPreviewListItem(
              event = event,
              onClick = onEventClick,
              onFavoriteClick = { onFavoriteClick(event) },
              isFavorite = event.isFavorite
            )
          }
        }
      }
    }

    if (isLoading) {
      LazyColumn {
        buildHeadline()
        buildUpcomingEventsLayout(upcomingEvents) {
          ShimmerBox(
            modifier = Modifier.size(240.dp)
          )
        }
        buildFinishedEventsLayout(finishedEvents) { ShimmerItem() }
      }
    }

    if (hasError) ErrorFallback(message = error, onRetry = onRetry)
  }
}

private fun LazyListScope.buildHeadline() {
  item {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      "Dicoding Events",
      style = MaterialTheme.typography.headlineMedium,
      color = MaterialTheme.colorScheme.primary,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      "Discover all events held by Dicoding from the upcoming to the finished ones.",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(24.dp))
  }
}

private fun LazyListScope.buildUpcomingEventsLayout(
  events: List<EventPreview>,
  item: @Composable (EventPreview) -> Unit,
) {
  item {
    Text(
      "Upcoming Events",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
      items(items = events, key = { it.id }) { item(it) }
    }
    Spacer(modifier = Modifier.height(24.dp))
  }
}

private fun LazyListScope.buildFinishedEventsLayout(
  events: List<EventPreview>,
  item: @Composable (EventPreview) -> Unit,
) {
  item {
    Text(
      "Finished Events",
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.SemiBold,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
  }
  items(items = events, key = { it.id }) { item(it) }
  item { Spacer(modifier = Modifier.height(16.dp)) }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenLoading() {
  val events = listOf(
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
  )

  AppTheme {
    Scaffold {
      HomeScreenContent(
        isLoading = true,
        hasError = false,
        error = null,
        onRetry = {},
        upcomingEvents = events.take(2),
        finishedEvents = events.take(3),
        onEventClick = {},
        onFavoriteClick = {},
        modifier = Modifier.padding(it),
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenLoaded() {
  val events = listOf(
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
    EventPreview.fake(),
  )

  AppTheme {
    Scaffold {
      HomeScreenContent(
        isLoading = false,
        hasError = false,
        error = null,
        onRetry = {},
        upcomingEvents = events.take(2),
        finishedEvents = events.take(3),
        onEventClick = {},
        onFavoriteClick = {},
        modifier = Modifier.padding(it)
      )
    }
  }
}