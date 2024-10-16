package dev.ybrmst.dicoding_events.ui.composable.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.ybrmst.dicoding_events.ui.composable.event.SearchEvents
import dev.ybrmst.dicoding_events.ui.composable.nav.EventDetailRoute
import dev.ybrmst.dicoding_events.ui.viewmodel.event.upcoming.UpcomingEventsUiEvent
import dev.ybrmst.dicoding_events.ui.viewmodel.event.upcoming.UpcomingEventsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingEventsScreen(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  vm: UpcomingEventsViewModel = koinViewModel(),
) {
  val state by vm.state.collectAsStateWithLifecycle()

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.add(UpcomingEventsUiEvent.OnRefresh) },
    modifier = modifier.fillMaxSize(),
  ) {
    SearchEvents(
      eventsType = "upcoming",
      events = state.events,
      onEventClick = { navController.navigate(EventDetailRoute(it)) },
      isLoading = state.isFetching || state.isRefreshing,
      isError = state.isError,
      searchQuery = state.query,
      onSearch = { vm.add(UpcomingEventsUiEvent.OnQueryChanged(it)) },
      onClearSearch = { vm.add(UpcomingEventsUiEvent.OnQueryCleared) },
      onRetry = { vm.add(UpcomingEventsUiEvent.OnFetch) },
    )
  }
}