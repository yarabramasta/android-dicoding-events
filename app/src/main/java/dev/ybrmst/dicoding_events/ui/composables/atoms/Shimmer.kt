package dev.ybrmst.dicoding_events.ui.composables.atoms

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicoding_events.ui.theme.AppTheme

@Composable
fun ShimmerBox(
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  if (animate) {
    Box(
      modifier = modifier
        .clip(MaterialTheme.shapes.small)
        .shimmerEffect()
    )
  } else {
    Box(
      modifier = modifier
        .clip(MaterialTheme.shapes.small)
        .background(color = MaterialTheme.colorScheme.surfaceContainerHigh),
    )
  }
}

@Composable
fun ShimmerItem(
  modifier: Modifier = Modifier,
  animate: Boolean = true,
) {
  Box(modifier = modifier) {
    ListItem(
      leadingContent = {
        ShimmerBox(
          modifier = Modifier.size(64.dp),
          animate = animate
        )
      },
      headlineContent = {
        ShimmerBox(
          modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .height(24.dp),
          animate = animate
        )
      },
      overlineContent = {
        ShimmerBox(
          modifier = Modifier
            .width(56.dp)
            .height(12.dp),
          animate = animate
        )
      },
      supportingContent = {
        ShimmerBox(
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
          animate = animate
        )
      },
      trailingContent = {
        IconButton(onClick = { }, enabled = false) {
          Icon(
            Icons.Outlined.StarOutline,
            contentDescription = "Favorite"
          )
        }
      },
    )
  }
}

fun Modifier.shimmerEffect(): Modifier = composed {
  var size by remember { mutableStateOf(IntSize.Zero) }

  val transition = rememberInfiniteTransition()

  val startOffsetX by transition.animateFloat(
    initialValue = -2 * size.width.toFloat(),
    targetValue = 2 * size.width.toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(
        1000,
        easing = FastOutLinearInEasing,
        delayMillis = 150,
      ),
      repeatMode = RepeatMode.Restart,
    )
  )

  background(
    brush = Brush.linearGradient(
      colors = listOf(
        MaterialTheme.colorScheme.surfaceContainerHigh,
        MaterialTheme.colorScheme.surfaceContainer,
        MaterialTheme.colorScheme.surfaceContainerHigh,
      ),
      start = Offset(startOffsetX, 0f),
      end = Offset(startOffsetX + size.width, size.height.toFloat()),
    )
  ).onGloballyPositioned { size = it.size }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerBoxPreview() {
  AppTheme {
    ShimmerBox(
      modifier = Modifier
        .size(100.dp)
        .padding(24.dp)
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerItemPreview() {
  AppTheme {
    ShimmerItem()
  }
}