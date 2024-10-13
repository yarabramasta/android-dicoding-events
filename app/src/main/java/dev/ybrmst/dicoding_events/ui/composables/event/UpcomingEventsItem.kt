package dev.ybrmst.dicoding_events.ui.composables.event

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicoding_events.domain.EventPreview
import dev.ybrmst.dicoding_events.ui.composables.atoms.ShimmerBox

fun LazyListScope.upcomingEventsItem(
  isLoading: Boolean,
  events: List<EventPreview>,
  onCardClick: (EventPreview) -> Unit,
) {
  item {
    Text(
      "Upcoming Events",
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier.padding(
        bottom = if (events.isNotEmpty() || isLoading) 16.dp else 8.dp
      )
    )
    if (events.isEmpty() && !isLoading) {
      Text(
        "There are no upcoming events.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline,
        modifier = Modifier.padding(bottom = 16.dp)
      )
    }
    LazyRow(
      contentPadding = PaddingValues(horizontal = 0.dp),
      horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
      if (isLoading) {
        items(5) {
          ShimmerBox(modifier = Modifier.size(200.dp))
        }
      } else {
        if (events.isEmpty()) {
          items(5) {
            ShimmerBox(animate = false, modifier = Modifier.size(200.dp))
          }
        } else {
          items(events) {
            ShimmerBox(
              animate = events.isNotEmpty(), modifier = Modifier.size(200.dp)
            )
          }
        }
      }
    }
  }
}