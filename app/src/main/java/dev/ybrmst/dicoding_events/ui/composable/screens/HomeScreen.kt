package dev.ybrmst.dicoding_events.ui.composable.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.ybrmst.dicoding_events.domain.model.EventPreview
import dev.ybrmst.dicoding_events.ui.composable.atoms.ShimmerBox
import dev.ybrmst.dicoding_events.ui.composable.event.EventPreviewCard
import dev.ybrmst.dicoding_events.ui.composable.event.EventPreviewCardFallback
import dev.ybrmst.dicoding_events.ui.composable.event.FeaturedEventCard
import dev.ybrmst.dicoding_events.ui.composable.event.buildEventErrorFallback
import dev.ybrmst.dicoding_events.ui.composable.nav.EventDetailRoute
import dev.ybrmst.dicoding_events.ui.theme.AppTheme
import dev.ybrmst.dicoding_events.ui.viewmodel.home.HomeUiEvent
import dev.ybrmst.dicoding_events.ui.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  navController: NavHostController,
  vm: HomeViewModel = koinViewModel(),
) {
  val state by vm.state.collectAsStateWithLifecycle()

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.add(HomeUiEvent.OnRefresh) },
    modifier = modifier.fillMaxSize(),
  ) {
    HomeScreenContent(
      upcomingEvents = state.upcomingEvents,
      pastEvents = state.pastEvents,
      isLoading = state.isFetching || state.isRefreshing,
      isError = state.isError,
      onEventClick = { navController.navigate(EventDetailRoute(it.id)) },
      onRetry = { vm.add(HomeUiEvent.OnFetch) },
    )
  }
}

@Composable
private fun HomeScreenContent(
  modifier: Modifier = Modifier,
  upcomingEvents: List<EventPreview>,
  pastEvents: List<EventPreview>,
  isLoading: Boolean = false,
  isError: Boolean = false,
  onEventClick: (EventPreview) -> Unit = {},
  onRetry: () -> Unit = {},
) {

  LazyColumn(
    contentPadding = PaddingValues(vertical = 8.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    modifier = modifier.fillMaxSize(),
  ) {
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        "Dicoding Events",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
          .padding(bottom = 8.dp)
          .padding(horizontal = 24.dp)
      )
      Text(
        "Recommendation events for you!",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 24.dp)
      )
      Spacer(modifier = Modifier.height(16.dp))
    }

    if (!isError) {
      buildUpcomingEvents(
        isLoading = isLoading,
        events = upcomingEvents,
        onCardClick = onEventClick
      )
      buildPastEvents(
        isLoading = isLoading,
        events = pastEvents,
        onCardClick = onEventClick
      )
    } else {
      buildEventErrorFallback(onRetry)
    }
  }
}

fun LazyListScope.buildUpcomingEvents(
  isLoading: Boolean,
  events: List<EventPreview>,
  onCardClick: (EventPreview) -> Unit,
) {
  item {
    Text(
      "Upcoming Events",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier
        .padding(bottom = if (events.isNotEmpty() || isLoading) 16.dp else 8.dp)
        .padding(horizontal = 24.dp)
    )
    if (events.isEmpty() && !isLoading) {
      Text(
        "There are no upcoming events.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier
          .padding(bottom = 16.dp)
          .padding(horizontal = 24.dp)
      )
    }
    LazyRow(
      contentPadding = PaddingValues(horizontal = 24.dp),
      horizontalArrangement = Arrangement.spacedBy(24.dp),
      modifier = Modifier.fillMaxWidth(),
    ) {
      if (isLoading) {
        items(5) {
          ShimmerBox(modifier = Modifier.size(240.dp))
        }
      } else {
        if (events.isEmpty()) {
          items(3) {
            ShimmerBox(animate = false, modifier = Modifier.size(240.dp))
          }
        } else {
          items(events) {
            FeaturedEventCard(event = it, onClick = onCardClick)
          }
        }
      }
    }
  }
}

fun LazyListScope.buildPastEvents(
  isLoading: Boolean,
  events: List<EventPreview>,
  onCardClick: (EventPreview) -> Unit,
) {
  if (isLoading) {
    items(5) { index ->
      if (index == 0) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
          "Finished Events",
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier
            .padding(bottom = 8.dp)
            .padding(horizontal = 24.dp)
        )
      }
      Box(modifier = Modifier.padding(horizontal = 8.dp)) {
        EventPreviewCardFallback()
      }
    }
  } else {
    if (events.isEmpty()) {
      items(3) { index ->
        if (index == 0) {
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            "Finished Events",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
              .padding(bottom = 8.dp)
              .padding(horizontal = 24.dp)
          )
          Text(
            "There are no finished events.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
              .padding(bottom = 16.dp)
              .padding(horizontal = 24.dp)
          )
        }
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
          EventPreviewCardFallback(animate = false)
        }
      }
    } else {
      itemsIndexed(events) { index, event ->
        if (index == 0) {
          Spacer(modifier = Modifier.height(16.dp))
          Text(
            "Finished Events",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
              .padding(bottom = 8.dp)
              .padding(horizontal = 24.dp)
          )
        }
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
          EventPreviewCard(event = event, onClick = onCardClick)
        }
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultStatePreview() {

  val mockEvents = listOf(
    EventPreview(
      id = 8948,
      name = "[Offline] IDCamp Connect Roadshow - Solo",
      cityName = "Kota Surakarta",
      summary = "IDCamp Connect Roadshow 2024 akan dilaksanakan pada hari Jumat, 18 Oktober 2024 pukul 13.00 - 17.00 WIB",
      imageLogo = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-offline_idcamp_connect_roadshow_solo_logo_091024131113.png"
    ),
    EventPreview(
      id = 8933,
      name = "DevCoach 172: Flutter | Tingkatkan Pengalaman Pengguna dengan Lokalisasi dan Aksesibilitas",
      cityName = "Online",
      summary = "Acara ini sepenuhnya GRATIS dan akan diselenggarakan hari Jumat, 11 Oktober 2024 pukul 16.00 - 17.00 WIB Live di YouTube",
      imageLogo = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-devcoach_172_flutter_tingkatkan_pengalaman_pengguna_dengan_lokalisasi_dan_aksesibilitas_logo_041024134406.png"
    ),
    EventPreview(
      id = 8898,
      name = "[Offline Event] Baparekraf Developer Day 2024",
      cityName = "Kota Yogyakarta",
      summary = "Baparekraf Developer Day 2024 kembali hadir secara offline di Kota Yogyakarta, Daerah Istimewa Yogyakarta.",
      imageLogo = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-offline_event_baparekraf_developer_day_2024_logo_030924171506.png"
    )
  )

  AppTheme {
    Scaffold {
      HomeScreenContent(
        upcomingEvents = mockEvents,
        pastEvents = mockEvents,
        modifier = Modifier.padding(it)
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingStatePreview() {

  AppTheme {
    Scaffold {
      HomeScreenContent(
        upcomingEvents = emptyList(),
        pastEvents = emptyList(),
        isLoading = true,
        modifier = Modifier.padding(it)
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmptyStatePreview() {

  AppTheme {
    Scaffold {
      HomeScreenContent(
        upcomingEvents = emptyList(),
        pastEvents = emptyList(),
        modifier = Modifier.padding(it)
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ErrorStatePreview() {

  AppTheme {
    Scaffold {
      HomeScreenContent(
        upcomingEvents = emptyList(),
        pastEvents = emptyList(),
        isError = true,
        modifier = Modifier.padding(it)
      )
    }
  }
}