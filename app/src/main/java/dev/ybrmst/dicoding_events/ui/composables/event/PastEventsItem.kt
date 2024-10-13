package dev.ybrmst.dicoding_events.ui.composables.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicoding_events.domain.EventPreview

fun LazyListScope.pastEventsItem(
  isLoading: Boolean,
  events: List<EventPreview>,
  onCardClick: (EventPreview) -> Unit,
) {
  if (isLoading) {
    items(5) { index ->
      if (index == 0) {
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
      item {
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
        Box(modifier = Modifier.padding(horizontal = 8.dp)) {
          EventPreviewCardFallback(animate = false)
        }
      }
    } else {
      itemsIndexed(events) { index, event ->
        if (index == 0) {
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