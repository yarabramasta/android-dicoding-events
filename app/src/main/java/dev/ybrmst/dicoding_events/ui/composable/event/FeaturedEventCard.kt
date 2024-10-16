package dev.ybrmst.dicoding_events.ui.composable.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import dev.ybrmst.dicoding_events.domain.model.EventPreview
import dev.ybrmst.dicoding_events.ui.composable.atoms.shimmerBrush

@Composable
fun FeaturedEventCard(
  modifier: Modifier = Modifier,
  event: EventPreview,
  onClick: (EventPreview) -> Unit,
) {
  var showShimmer by remember { mutableStateOf(true) }

  val imagePainter = rememberAsyncImagePainter(
    model = event.imageLogo,
    onState = {
      showShimmer = when (it) {
        AsyncImagePainter.State.Empty -> true
        is AsyncImagePainter.State.Error -> false
        is AsyncImagePainter.State.Loading -> true
        is AsyncImagePainter.State.Success -> false
      }
    }
  )

  Box(
    modifier = modifier
      .size(240.dp)
      .clip(MaterialTheme.shapes.medium)
      .background(shimmerBrush(show = showShimmer, targetValue = 1300f))
      .paint(painter = imagePainter, contentScale = ContentScale.FillBounds)
      .clickable { onClick(event) }
  ) {
    Column(
      horizontalAlignment = Alignment.Start,
      verticalArrangement = androidx.compose.foundation.layout.Arrangement.Bottom,
      modifier = modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
        .padding(16.dp)
    ) {
      Text(
        event.cityName,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 4.dp)
      )
      Text(
        event.name,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}