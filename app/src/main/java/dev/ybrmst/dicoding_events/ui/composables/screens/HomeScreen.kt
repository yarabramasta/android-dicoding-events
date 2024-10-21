package dev.ybrmst.dicoding_events.ui.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import dev.ybrmst.dicoding_events.domain.models.EventPreview
import dev.ybrmst.dicoding_events.ui.composables.rememberFlowWithLifecycle
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

  LaunchedEffect(effect) {
    effect.collect { action ->
      when (action) {
        is HomeReducer.HomeEffect.NavigateToEventDetail -> {
          // TODO: Navigate to event detail screen
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
      errorMessage = state.error?.message,
      onErrorRetry = { vm.onRefresh() },
      upcomingEvents = state.upcomingEvents,
      finishedEvents = state.finishedEvents,
      onEventClick = { eventId -> vm.onEventClick(eventId) }
    )
  }
}

@Composable
private fun HomeScreenContent(
  modifier: Modifier = Modifier,
  isLoading: Boolean,
  hasError: Boolean,
  errorMessage: String? = null,
  onErrorRetry: () -> Unit,
  upcomingEvents: List<EventPreview>,
  finishedEvents: List<EventPreview>,
  onEventClick: (Int) -> Unit,
) {
  Box(modifier = modifier.fillMaxSize()) {
    when {
      isLoading -> {
        // TODO: Show loading indicator
      }

      hasError -> {
        Column(
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier.fillMaxSize(),
        ) {
          Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.onErrorContainer,
            modifier = Modifier.size(32.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            text = errorMessage ?: "An error occurred",
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onErrorContainer,
          )
          Spacer(modifier = Modifier.height(16.dp))
          FilledTonalButton(
            onClick = onErrorRetry,
            colors = ButtonDefaults.filledTonalButtonColors().copy(
              containerColor = MaterialTheme.colorScheme.errorContainer,
              contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
          ) {
            Text("Retry")
          }
        }
      }

      else -> {
        // TODO: Show upcoming and finished events
      }
    }
  }
}