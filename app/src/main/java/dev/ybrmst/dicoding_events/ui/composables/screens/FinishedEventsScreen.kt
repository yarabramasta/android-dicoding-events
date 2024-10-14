package dev.ybrmst.dicoding_events.ui.composables.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.ybrmst.dicoding_events.ui.composables.event.SearchEvents
import dev.ybrmst.dicoding_events.ui.composables.nav.EventDetailRoute
import dev.ybrmst.dicoding_events.ui.viewmodel.event.finished.FinishedEventsUiEvent
import dev.ybrmst.dicoding_events.ui.viewmodel.event.finished.FinishedEventsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinishedEventsScreen(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  vm: FinishedEventsViewModel = koinViewModel(),
) {
  val state by vm.state.collectAsStateWithLifecycle()

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.add(FinishedEventsUiEvent.OnRefresh) },
    modifier = modifier.fillMaxSize(),
  ) {
    SearchEvents(
      eventsType = "finished",
      events = state.events,
      onEventClick = { navController.navigate(EventDetailRoute(it)) },
      isLoading = state.isFetching || state.isRefreshing,
      isError = state.isError,
      searchQuery = state.query,
      onSearch = { vm.add(FinishedEventsUiEvent.OnQueryChanged(it)) },
      onClearSearch = { vm.add(FinishedEventsUiEvent.OnQueryCleared) },
      onRetry = { vm.add(FinishedEventsUiEvent.OnFetch) },
    )
  }
}