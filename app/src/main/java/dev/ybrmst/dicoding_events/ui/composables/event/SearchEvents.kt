package dev.ybrmst.dicoding_events.ui.composables.event

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicoding_events.domain.EventPreview
import dev.ybrmst.dicoding_events.ui.theme.AppTheme

@Composable
fun SearchEvents(
  modifier: Modifier = Modifier,
  eventsType: String = "upcoming",
  events: List<EventPreview>,
  isLoading: Boolean = false,
  isError: Boolean = false,
  searchQuery: String = "",
  onEventClick: (Int) -> Unit = {},
  onSearch: (String) -> Unit = {},
  onClearSearch: () -> Unit = {},
  onRetry: () -> Unit = {},
) {
  if (eventsType != "upcoming" && eventsType != "finished") {
    throw IllegalArgumentException("Invalid events type: $eventsType")
  }

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(vertical = 24.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    item {
      Box(
        modifier = Modifier
          .padding(bottom = 8.dp)
          .padding(horizontal = 8.dp)
      ) {
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { onSearch(it) },
          modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
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
              modifier = Modifier.clickable { if (searchQuery.isNotEmpty()) onClearSearch() }
            )
          }
        )
      }
    }

    when {
      isLoading -> {
        items(5) {
          Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            EventPreviewCardFallback()
          }
        }
      }

      isError -> buildEventErrorFallback { onRetry() }

      events.isEmpty() -> {
        item {
          Text(
            if (searchQuery.isNotEmpty()) "There are no results for \"$searchQuery\" in $eventsType events..." else "There are no $eventsType events found.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
              .padding(bottom = 16.dp)
              .padding(horizontal = 24.dp)
          )
        }
      }

      else -> {
        items(events) { event ->
          Box(modifier = Modifier.padding(horizontal = 8.dp)) {
            EventPreviewCard(event = event) { onEventClick(it.id) }
          }
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
      SearchEvents(
        events = mockEvents,
        modifier = Modifier.padding(it),
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingStatePreview() {
  AppTheme {
    Scaffold {
      SearchEvents(
        events = emptyList(),
        modifier = Modifier.padding(it),
        isLoading = true
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmptyStatePreview() {
  AppTheme {
    Scaffold {
      SearchEvents(
        events = emptyList(),
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
      SearchEvents(
        events = emptyList(),
        modifier = Modifier.padding(it),
        isError = true
      )
    }
  }
}