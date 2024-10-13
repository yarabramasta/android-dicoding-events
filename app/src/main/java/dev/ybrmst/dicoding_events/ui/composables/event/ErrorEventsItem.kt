package dev.ybrmst.dicoding_events.ui.composables.event

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListScope.errorEventsItem(
  message: String = "Uh oh, looks like we can't retrieve the events right now.",
  onRetryClick: () -> Unit,
) {
  item {
    Text(
      message,
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.outline,
      modifier = Modifier
        .padding(bottom = 16.dp)
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
    )
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 24.dp)
    ) {
      Button(
        colors = ButtonDefaults.filledTonalButtonColors().copy(
          containerColor = MaterialTheme.colorScheme.errorContainer,
          contentColor = MaterialTheme.colorScheme.onErrorContainer
        ),
        onClick = { onRetryClick() },
        modifier = Modifier.fillMaxWidth(),
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
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
}