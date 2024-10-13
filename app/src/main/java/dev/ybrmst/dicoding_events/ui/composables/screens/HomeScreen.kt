package dev.ybrmst.dicoding_events.ui.composables.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import dev.ybrmst.dicoding_events.domain.EventPreview
import dev.ybrmst.dicoding_events.ui.composables.event.errorEventsItem
import dev.ybrmst.dicoding_events.ui.composables.home.pastEventsItem
import dev.ybrmst.dicoding_events.ui.composables.home.upcomingEventsItem
import dev.ybrmst.dicoding_events.ui.theme.AppTheme
import dev.ybrmst.dicoding_events.ui.viewmodel.home.HomeEvent
import dev.ybrmst.dicoding_events.ui.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  vm: HomeViewModel = koinViewModel(),
) {
  val state by vm.state.collectAsStateWithLifecycle()

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.add(HomeEvent.OnRefresh) },
  ) {

    HomeScreenContent(
      upcomingEvents = state.upcomingEvents,
      pastEvents = state.pastEvents,
      isLoading = state.isFetching || state.isRefreshing,
      isError = state.isError,
      onCardClick = { event -> println("[${event.id}] Event clicked: $event") },
      onRetryClick = { vm.add(HomeEvent.OnFetch) },
      modifier = modifier
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
  onCardClick: (EventPreview) -> Unit = {},
  onRetryClick: () -> Unit = {},
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
      upcomingEventsItem(
        isLoading = isLoading,
        events = upcomingEvents,
        onCardClick = onCardClick
      )
      pastEventsItem(
        isLoading = isLoading, events = pastEvents, onCardClick = onCardClick
      )
    } else {
      errorEventsItem { onRetryClick() }
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
    ), EventPreview(
      id = 8933,
      name = "DevCoach 172: Flutter | Tingkatkan Pengalaman Pengguna dengan Lokalisasi dan Aksesibilitas",
      cityName = "Online",
      summary = "Acara ini sepenuhnya GRATIS dan akan diselenggarakan hari Jumat, 11 Oktober 2024 pukul 16.00 - 17.00 WIB Live di YouTube",
      imageLogo = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-devcoach_172_flutter_tingkatkan_pengalaman_pengguna_dengan_lokalisasi_dan_aksesibilitas_logo_041024134406.png"
    ), EventPreview(
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