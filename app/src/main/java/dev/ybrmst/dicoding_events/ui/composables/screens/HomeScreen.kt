package dev.ybrmst.dicoding_events.ui.composables.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicoding_events.domain.EventPreview
import dev.ybrmst.dicoding_events.ui.composables.event.EventPreviewCard
import dev.ybrmst.dicoding_events.ui.composables.event.EventPreviewCardFallback
import dev.ybrmst.dicoding_events.ui.theme.AppTheme
import dev.ybrmst.dicoding_events.ui.viewmodel.home.HomeEvent
import dev.ybrmst.dicoding_events.ui.viewmodel.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
  modifier: Modifier = Modifier,
  vm: HomeViewModel = koinViewModel(),
) {
  val state by vm.state.collectAsState()

  LaunchedEffect(Unit) {
    vm.add(HomeEvent.OnFetchEvents)
  }

  HomeScreenContent(
    highlights = state.highlights,
    events = state.events,
    isLoading = state.isFetching,
    onCardClick = { event ->
      println("[${event.id}] Event card clicked: $event")
    },
    modifier = modifier
  )
}

@Composable
private fun HomeScreenContent(
  modifier: Modifier = Modifier,
  highlights: List<EventPreview>,
  events: List<EventPreview>,
  isLoading: Boolean,
  onCardClick: (EventPreview) -> Unit,
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(vertical = 24.dp),
    contentPadding = PaddingValues(0.dp)
  ) {
    item {
      Text(
        text = "Dicoding Events",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
          .padding(bottom = 8.dp)
          .padding(horizontal = 24.dp)
      )
    }
    item {
      Text(
        text = "Recommendation events for you!",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
          .padding(bottom = 24.dp)
          .padding(horizontal = 24.dp)
      )
    }
    item {
      Text(
        text = "Highlights",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
          .padding(bottom = 8.dp)
          .padding(horizontal = 24.dp)
      )
    }
    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
    item {
      Text(
        text = "Events",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
          .padding(bottom = 8.dp)
          .padding(horizontal = 24.dp)
      )
    }
    if (isLoading) {
      items(3) {
        Box(modifier = modifier.padding(horizontal = 8.dp)) {
          EventPreviewCardFallback()
        }
      }
    } else {
      if (events.isEmpty()) {
        item {
          Text(
            text = "No upcoming events...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
              .padding(bottom = 24.dp)
              .padding(horizontal = 24.dp)
          )
        }
        items(1) {
          Box(modifier = modifier.padding(horizontal = 8.dp)) {
            EventPreviewCardFallback(animate = false)
          }
        }
      } else {
        items(events) { event ->
          EventPreviewCard(
            event = event,
            onEventClick = { onCardClick(event) },
          )
        }
      }
    }
  }
}

private val events = listOf(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DefaultStatePreview() {

  AppTheme {
    Scaffold { innerPadding ->
      HomeScreenContent(
        highlights = events,
        events = events,
        isLoading = false,
        onCardClick = {},
        modifier = Modifier.padding(innerPadding)
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingStatePreview() {

  AppTheme {
    Scaffold { innerPadding ->
      HomeScreenContent(
        highlights = events,
        events = events,
        isLoading = true,
        onCardClick = {},
        modifier = Modifier.padding(innerPadding)
      )
    }
  }
}

@Preview(
  showBackground = true, showSystemUi = true
)
@Composable
private fun EmptyStatePreview() {

  AppTheme {
    Scaffold { innerPadding ->
      HomeScreenContent(
        highlights = emptyList(),
        events = emptyList(),
        isLoading = false,
        onCardClick = {},
        modifier = Modifier.padding(innerPadding)
      )
    }
  }
}