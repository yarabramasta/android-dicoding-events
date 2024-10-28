package dev.ybrmst.dicodingevents.presentation.ui.composables.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.SubcomposeAsyncImage
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme

@Composable
fun EventPreviewCard(
  modifier: Modifier = Modifier,
  event: EventPreview,
  isFavorite: Boolean,
  onClick: (EventPreview) -> Unit,
  onFavoriteClick: (EventPreview) -> Unit,
) {
  Box(
    modifier = modifier
      .size(240.dp)
      .shadow(2.dp, MaterialTheme.shapes.medium)
      .clip(MaterialTheme.shapes.medium)
      .clickable { onClick(event) }
  ) {
    SubcomposeAsyncImage(
      model = event.imageLogo,
      contentDescription = event.name,
      loading = { ShimmerBox(modifier = Modifier.fillMaxSize()) },
      error = {
        ShimmerBox(
          modifier = Modifier.fillMaxSize(),
          animate = false
        )
      },
      modifier = Modifier
        .fillMaxSize()
        .aspectRatio(1f)
    )

    Column(
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Top,
      modifier = modifier
        .fillMaxSize()
        .background(
          shape = MaterialTheme.shapes.medium,
          brush = Brush.verticalGradient(
            listOf(
              Color.Transparent,
              MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
            )
          ),
        )
        .zIndex(1f)
    ) {
      Box(modifier = Modifier.align(Alignment.End)) {
        IconButton(onClick = { onFavoriteClick(event) }) {
          Icon(
            if (isFavorite) Icons.Filled.Star
            else Icons.Outlined.StarOutline,
            contentDescription = "Favorite",
            tint = MaterialTheme.colorScheme.tertiary
          )
        }
      }

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
      ) {
        Text(
          event.cityName,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          event.name,
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          fontWeight = FontWeight.Bold,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun EventPreviewCardPreview() {
  val event = EventPreview.fake()

  AppTheme {
    Box(modifier = Modifier.padding(16.dp)) {
      EventPreviewCard(
        event = event,
        isFavorite = false,
        onClick = {},
        onFavoriteClick = {},
      )
    }
  }
}