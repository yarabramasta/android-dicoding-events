package dev.ybrmst.dicoding_events.ui.composables.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import dev.ybrmst.dicoding_events.domain.EventPreview
import dev.ybrmst.dicoding_events.ui.composables.atoms.ShimmerBox
import dev.ybrmst.dicoding_events.ui.composables.atoms.shimmerBrush
import dev.ybrmst.dicoding_events.ui.theme.AppTheme

@Composable
fun EventPreviewCard(
  modifier: Modifier = Modifier,
  event: EventPreview,
  onEventClick: () -> Unit = { },
) {
  val (showShimmer, setShowShimmer) = remember { mutableStateOf(true) }

  ListItem(
    leadingContent = {
      AsyncImage(
        model = event.imageLogo,
        contentDescription = event.name,
        onState = {
          when (it) {
            AsyncImagePainter.State.Empty -> setShowShimmer(true)
            is AsyncImagePainter.State.Error -> setShowShimmer(false)
            is AsyncImagePainter.State.Loading -> setShowShimmer(true)
            is AsyncImagePainter.State.Success -> setShowShimmer(false)
          }
        },
        modifier = Modifier
          .size(64.dp)
          .aspectRatio(1f)
          .clip(RoundedCornerShape(6))
          .background(shimmerBrush(show = showShimmer, targetValue = 1300f))
      )
    },
    overlineContent = { Text(event.cityName) },
    headlineContent = {
      Text(
        event.name,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(vertical = 8.dp)
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
    modifier = modifier.clickable { onEventClick() }
  )
}

@Composable
fun EventPreviewCardFallback(
  animate: Boolean = true,
) {

  ListItem(
    leadingContent = {
      ShimmerBox(
        modifier = Modifier
          .size(64.dp)
      )
    },
    overlineContent = {
      ShimmerBox(
        modifier = Modifier
          .width(64.dp)
          .height(16.dp)
      )
    },
    headlineContent = {
      ShimmerBox(
        animate = animate,
        modifier = Modifier
          .padding(vertical = 8.dp)
          .fillMaxWidth()
          .height(32.dp)
      )
    },
    supportingContent = {
      ShimmerBox(
        animate = animate,
        modifier = Modifier
          .fillMaxWidth()
          .height(56.dp)
      )
    },
  )
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
  val event = EventPreview(
    id = 8948,
    name = "[Offline] IDCamp Connect  Roadshow - Solo",
    cityName = "Kota Surakarta",
    summary = "IDCamp Connect Roadshow 2024 akan dilaksanakan pada hari Jumat, 18 Oktober 2024 pukul 13.00 - 17.00 WIB",
    imageLogo = "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/event/dos-offline_idcamp_connect_roadshow_solo_logo_091024131113.png",
  )

  AppTheme {
    EventPreviewCard(event = event)
  }
}

@Preview(showBackground = true)
@Composable
private fun FallbackPreview() {
  AppTheme {
    EventPreviewCardFallback()
  }
}