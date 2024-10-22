package dev.ybrmst.dicoding_events.ui.composables.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import dev.ybrmst.dicoding_events.domain.models.EventPreview
import dev.ybrmst.dicoding_events.ui.theme.AppTheme

@Composable
fun EventPreviewListItem(
  modifier: Modifier = Modifier,
  event: EventPreview,
  isFavorite: Boolean,
  onClick: (EventPreview) -> Unit,
  onFavoriteClick: (EventPreview) -> Unit,
) {
  ListItem(
    modifier = modifier.clickable { onClick(event) },
    leadingContent = {
      SubcomposeAsyncImage(
        model = event.imageLogo,
        contentDescription = event.name,
        loading = { ShimmerBox(modifier = Modifier.size(64.dp)) },
        error = {
          ShimmerBox(
            modifier = Modifier.size(64.dp),
            animate = false
          )
        },
        modifier = Modifier
          .size(64.dp)
          .aspectRatio(1f)
          .clip(MaterialTheme.shapes.small)
      )
    },
    headlineContent = {
      Text(
        event.name,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 8.dp)
      )
    },
    overlineContent = {
      Text(
        event.cityName,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(bottom = 4.dp)
      )
    },
    supportingContent = {
      Text(
        event.summary,
        maxLines = 3,
        color = MaterialTheme.colorScheme.outline,
        overflow = TextOverflow.Ellipsis,
      )
    },
    trailingContent = {
      IconButton(onClick = { onFavoriteClick(event) }) {
        Icon(
          if (isFavorite) Icons.Filled.Star
          else (Icons.Outlined.StarOutline),
          contentDescription = "Favorite",
          tint = MaterialTheme.colorScheme.tertiary
        )
      }
    },
  )
}

@Preview(showBackground = true)
@Composable
private fun EventPreviewListItemPreview() {
  val event = EventPreview.fake()

  AppTheme {
    Box(modifier = Modifier.padding(16.dp)) {
      EventPreviewListItem(
        event = event,
        isFavorite = false,
        onClick = {},
        onFavoriteClick = {},
      )
    }
  }
}