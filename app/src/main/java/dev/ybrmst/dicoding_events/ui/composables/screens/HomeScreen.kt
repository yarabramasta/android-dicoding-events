package dev.ybrmst.dicoding_events.ui.composables.screens

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.ybrmst.dicoding_events.AppRoute
import dev.ybrmst.dicoding_events.domain.errors.BaseError
import dev.ybrmst.dicoding_events.domain.models.EventPreview
import dev.ybrmst.dicoding_events.ui.composables.atoms.EmptyItemsFallback
import dev.ybrmst.dicoding_events.ui.composables.atoms.ErrorFallback
import dev.ybrmst.dicoding_events.ui.composables.atoms.EventPreviewCard
import dev.ybrmst.dicoding_events.ui.composables.atoms.EventPreviewListItem
import dev.ybrmst.dicoding_events.ui.composables.atoms.ShimmerBox
import dev.ybrmst.dicoding_events.ui.composables.atoms.ShimmerItem
import dev.ybrmst.dicoding_events.ui.composables.rememberFlowWithLifecycle
import dev.ybrmst.dicoding_events.ui.theme.AppTheme
import dev.ybrmst.dicoding_events.ui.viewmodels.HomeReducer
import dev.ybrmst.dicoding_events.ui.viewmodels.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  vm: HomeViewModel = hiltViewModel(),
) {
  val state by vm.state.collectAsStateWithLifecycle()
  val effect = rememberFlowWithLifecycle(vm.effect)
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect { action ->
      when (action) {
        is HomeReducer.HomeEffect.NavigateToEventDetail -> {
          navController.navigate(AppRoute.EventDetail(action.eventId))
        }

        is HomeReducer.HomeEffect.ShowFavoriteUpdateSuccessToast -> {
          Toast.makeText(context, "Favorite events updated", Toast.LENGTH_SHORT).show()
        }
        
        is HomeReducer.HomeEffect.ShowFavoriteUpdateErrorToast -> {
          Toast.makeText(context, "Failed to update favorite events", Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  var mounted by rememberSaveable { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    if (!mounted) {
      mounted = true
      vm.onFetch()
    }
  }

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.onRefresh() },
    modifier = Modifier.fillMaxSize(),
  ) {
    HomeScreenContent(
      modifier = modifier,
      isLoading = state.isFetching || state.isRefreshing,
      hasError = state.error != null,
      error = state.error,
      onRetry = { vm.onRefresh() },
      upcomingEvents = state.upcomingEvents,
      finishedEvents = state.finishedEvents,
      onEventClick = { eventId -> vm.onEventClick(eventId) },
      onFavoriteClick = { event -> vm.onFavoriteClick(event) },
    )
  }
}

@Composable
private fun HomeScreenContent(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  hasError: Boolean,
  error: BaseError?,
  onRetry: () -> Unit,
  upcomingEvents: List<EventPreview>,
  finishedEvents: List<EventPreview>,
  onEventClick: (Int) -> Unit,
  onFavoriteClick: (EventPreview) -> Unit ,
) {
  Box(modifier = modifier.fillMaxSize()) {
    if (isLoading) {
      LazyColumn {
        buildHeadline()
        buildUpcomingEventsLayout(upcomingEvents) {
          ShimmerBox(modifier = Modifier.size(240.dp))
        }
        buildFinishedEventsLayout(finishedEvents) {
          ShimmerItem()
        }
      }
    }

    if (hasError) {
      ErrorFallback(message = error?.message, onRetry = onRetry)
    }

    if (!isLoading && !hasError) {
      if (upcomingEvents.isEmpty() && finishedEvents.isEmpty()) {
        EmptyItemsFallback(onRefresh = onRetry)
      } else {
        LazyColumn {
          buildHeadline()
          buildUpcomingEventsLayout(upcomingEvents) { event ->
            EventPreviewCard(
              event = event,
              onClick = { onEventClick(event.id) },
              onFavoriteClick = { onFavoriteClick(event) },
              isFavorite = event.isFavorite
            )
          }
          buildFinishedEventsLayout(finishedEvents) { event ->
            EventPreviewListItem(
              event = event,
              onClick = { onEventClick(event.id) },
              onFavoriteClick = { onFavoriteClick(event) },
              isFavorite = event.isFavorite
            )
          }
        }
      }
    }
  }
}

private fun LazyListScope.buildHeadline() {
  item {
    Text(
      "Dicoding Events",
      style = MaterialTheme.typography.titleLarge,
      color = MaterialTheme.colorScheme.primary,
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
      style = MaterialTheme.typography.titleMedium,
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
      style = MaterialTheme.typography.titleMedium,
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