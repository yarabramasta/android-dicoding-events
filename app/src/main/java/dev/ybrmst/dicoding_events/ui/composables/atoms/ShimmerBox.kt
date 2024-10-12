package dev.ybrmst.dicoding_events.ui.composables.atoms

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicoding_events.ui.theme.AppTheme

@Composable
fun shimmerBrush(
  show: Boolean = true,
  targetValue: Float = 1000f,
): Brush {
  return if (show) {
    val shimmerColors = listOf(
      MaterialTheme.colorScheme.surfaceContainerHigh.copy(),
      MaterialTheme.colorScheme.surfaceContainer.copy(),
      MaterialTheme.colorScheme.surfaceContainerHigh.copy(),
    )

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnimation = transition.animateFloat(
      initialValue = 0f,
      targetValue = targetValue,
      animationSpec = infiniteRepeatable(
        animation = tween(
          1000,
          easing = FastOutLinearInEasing,
          delayMillis = 150
        ),
        repeatMode = RepeatMode.Restart,
        initialStartOffset = StartOffset(300)
      ),
      label = "translateAnimation"
    )
    Brush.linearGradient(
      colors = shimmerColors,
      start = Offset.Zero,
      end = Offset(x = translateAnimation.value, y = translateAnimation.value),
    )
  } else {
    Brush.linearGradient(
      colors = listOf(Color.Transparent, Color.Transparent),
      start = Offset.Zero,
      end = Offset.Zero,
    )
  }
}

@Composable
fun ShimmerBox(
  modifier: Modifier = Modifier,
  animate: Boolean = true,
  show: Boolean = true,
) {
  if (!animate) {
    Box(
      modifier = modifier
        .clip(RoundedCornerShape(6.dp))
        .background(MaterialTheme.colorScheme.surfaceContainer)
    )
  } else {
    Box(
      modifier = modifier
        .clip(RoundedCornerShape(6.dp))
        .background(shimmerBrush(show = show, targetValue = 1300f))
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerBoxPreview() {
  AppTheme {
    ShimmerBox(
      modifier = Modifier
        .size(64.dp)
        .padding(8.dp),
      animate = true,
      show = true
    )
  }
}