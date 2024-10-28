package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.EventPreviewListItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.ShimmerItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.TabItem
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.TabView
import dev.ybrmst.dicodingevents.presentation.ui.composables.clearFocusOnKeyboardDismiss
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberFlowWithLifecycle
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberUpdatedFavoriteStatus
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverViewModel
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
  modifier: Modifier = Modifier,
  vm: DiscoverViewModel = hiltViewModel(),
  favsVm: FavoritesViewModel,
  onEventClick: (EventPreview) -> Unit = {},
) {
  val state by vm.state.collectAsStateWithLifecycle()
  val effect = rememberFlowWithLifecycle(vm.effect)
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect { action ->
      when (action) {
        is DiscoverContract.Effect.ShowToast -> {
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

  val tabItems = listOf(
    TabItem(
      title = "Upcoming",
      icon = Icons.Outlined.EventAvailable,
      selectedIcon = Icons.Filled.EventAvailable
    ),
    TabItem(
      title = "Finished",
      icon = Icons.Outlined.EventBusy,
      selectedIcon = Icons.Filled.EventBusy
    ),
  )

  TabView(
    modifier = modifier.fillMaxSize(),
    items = tabItems
  ) { activePage ->
    PullToRefreshBox(
      modifier = Modifier.fillMaxSize(),
      isRefreshing = state.isRefreshingUpcoming || state.isRefreshingFinished,
      onRefresh = {
        vm.add(
          DiscoverContract.Event.OnRefresh(
            when (activePage) {
              0 -> DiscoverContract.EventCategory.UPCOMING
              1 -> DiscoverContract.EventCategory.FINISHED
              else -> DiscoverContract.EventCategory.UPCOMING
            }
          )
        )
      },
    ) {
      when (activePage) {
        0 -> DiscoverScreenTabViewContent(
          category = DiscoverContract.EventCategory.UPCOMING,
          events = upcomingEvents,
          isLoading = state.isRefreshingUpcoming || state.isFetchingUpcoming || state.isSearchingUpcoming,
          error = state.errorUpcoming,
          onEventClick = onEventClick,
          searchQuery = state.searchQueryUpcoming,
          onSearch = {
            vm.add(
              DiscoverContract.Event.OnSearchQueryChanged(
                it,
                DiscoverContract.EventCategory.UPCOMING
              )
            )
          },
          onClearSearch = {
            vm.add(
              DiscoverContract.Event.OnSearchQueryCleared(
                DiscoverContract.EventCategory.UPCOMING
              )
            )
          },
          onFavoriteClick = {
            toggleFavoriteStatus(it, favsVm, vm)
          },
          onRetry = {
            vm.add(DiscoverContract.Event.OnRefresh(DiscoverContract.EventCategory.UPCOMING))
          },
        )

        1 -> DiscoverScreenTabViewContent(
          category = DiscoverContract.EventCategory.FINISHED,
          events = finishedEvents,
          isLoading = state.isRefreshingFinished || state.isFetchingFinished || state.isSearchingFinished,
          error = state.errorFinished,
          searchQuery = state.searchQueryFinished,
          onSearch = {
            vm.add(
              DiscoverContract.Event.OnSearchQueryChanged(
                it,
                DiscoverContract.EventCategory.FINISHED
              )
            )
          },
          onClearSearch = {
            vm.add(
              DiscoverContract.Event.OnSearchQueryCleared(
                DiscoverContract.EventCategory.FINISHED
              )
            )
          },
          onEventClick = onEventClick,
          onFavoriteClick = {
            toggleFavoriteStatus(it, favsVm, vm)
          },
          onRetry = {
            vm.add(DiscoverContract.Event.OnRefresh(DiscoverContract.EventCategory.FINISHED))
          },
        )
      }
    }
  }
}

private fun toggleFavoriteStatus(
  event: EventPreview,
  favsVm: FavoritesViewModel,
  vm: DiscoverViewModel,
) {
  if (event.isFavorite) {
    favsVm.add(FavoritesContract.Event.OnEventRemoved(event))
  } else {
    favsVm.add(FavoritesContract.Event.OnEventAdded(event))
  }
  vm.add(DiscoverContract.Event.OnEventFavoriteChanged(event.isFavorite))
}

@Composable
private fun DiscoverScreenTabViewContent(
  category: DiscoverContract.EventCategory,
  events: List<EventPreview>,
  isLoading: Boolean,
  error: String?,
  searchQuery: String = "",
  onSearch: (String) -> Unit,
  onClearSearch: () -> Unit,
  onEventClick: (EventPreview) -> Unit,
  onFavoriteClick: (EventPreview) -> Unit,
  onRetry: () -> Unit,
) {
  val focusManager = LocalFocusManager.current

  LazyColumn(modifier = Modifier.fillMaxSize()) {
    item {
      Spacer(modifier = Modifier.height(16.dp))
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearch,
        modifier = Modifier
          .fillMaxWidth()
          .clearFocusOnKeyboardDismiss(),
        shape = MaterialTheme.shapes.medium,
        placeholder = { Text(text = "Search...") },
        maxLines = 1,
        singleLine = true,
        leadingIcon = {
          Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = null
          )
        },
        trailingIcon = {
          Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            modifier = Modifier.clickable {
              if (searchQuery.isNotEmpty()) onClearSearch()
              focusManager.clearFocus()
            }
          )
        }
      )
      Spacer(modifier = Modifier.height(16.dp))
    }

    if (!isLoading && error.isNullOrBlank()) {
      if (events.isEmpty()) {
        item {
          Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
          ) {
            Text(
              "No ${category.name.lowercase(Locale.ROOT)} events found.",
              style = MaterialTheme.typography.bodyLarge,
              color = MaterialTheme.colorScheme.outline,
              textAlign = TextAlign.Center
            )
          }
        }
      } else {
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

    if (error != null) {
      item {
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            error,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
          )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
          colors = ButtonDefaults.filledTonalButtonColors().copy(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer
          ),
          onClick = onRetry,
          modifier = Modifier.fillMaxWidth(),
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Filled.Refresh,
              contentDescription = null,
              modifier = Modifier.padding(end = 8.dp),
            )
            Text("Retry")
          }
        }
      }
    }

    if (isLoading) {
      items(events) {
        ShimmerItem()
      }
    }
  }
}