package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import dev.ybrmst.dicodingevents.domain.models.EventDetail
import dev.ybrmst.dicodingevents.domain.models.getDisplayDate
import dev.ybrmst.dicodingevents.domain.models.getRemainingQuota
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.HtmlRenderer
import dev.ybrmst.dicodingevents.presentation.ui.composables.atoms.ShimmerBox
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberFlowWithLifecycle
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.EventDetailContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.EventDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
  modifier: Modifier = Modifier,
  onPop: () -> Unit,
  eventId: Int,
  vm: EventDetailViewModel = hiltViewModel(),
) {
  val state by vm.state.collectAsStateWithLifecycle()
  val effect = rememberFlowWithLifecycle(vm.effect)
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect {
      when (it) {
        is EventDetailContract.Effect.ShowToast -> {
          Toast.makeText(
            context,
            it.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  PullToRefreshBox(
    isRefreshing = state.isRefreshing,
    onRefresh = { vm.add(EventDetailContract.Event.OnRefresh(eventId)) },
    modifier = modifier.fillMaxSize(),
  ) {
    EventDetailScreenContent(
      modifier = modifier,
      event = state.event,
      isLoading = state.isFetching || state.isRefreshing,
      error = state.error,
      onPop = onPop,
      onRetry = {
        vm.add(EventDetailContract.Event.OnRefresh(eventId))
      },
      onFavoriteClick = {
        vm.add(EventDetailContract.Event.OnFavoriteChanged)
      },
    )
  }
}

@Composable
private fun EventDetailScreenContent(
  modifier: Modifier = Modifier,
  event: EventDetail?,
  isLoading: Boolean,
  error: String?,
  onPop: () -> Unit,
  onRetry: () -> Unit,
  onFavoriteClick: () -> Unit,
) {
  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      EventDetailTopBar(
        event = event,
        isLoading = isLoading,
        error = error,
        onPop = onPop,
        onFavoriteClick = onFavoriteClick
      )
    },
    bottomBar = {
      EventDetailBottomBar(
        isLoading = isLoading,
        error = error,
        event = event,
        onRetry = onRetry
      )
    }
  ) { innerPadding ->
    LazyColumn(
      contentPadding = PaddingValues(0.dp),
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
    ) {
      buildCover(isLoading, error, event?.mediaCover)

      if (isLoading || event == null || !error.isNullOrBlank()) {
        buildFallback(isLoading, error, event)
      }

      if (!isLoading && event != null) {
        buildContent(event)
      }
    }
  }

  BackHandler { onPop() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventDetailTopBar(
  event: EventDetail?,
  isLoading: Boolean,
  error: String?,
  onPop: () -> Unit,
  onFavoriteClick: () -> Unit,
) {
  TopAppBar(
    title = {
      if (!isLoading && event != null) {
        Text(
          event.name,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    },
    navigationIcon = {
      IconButton(onClick = onPop) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
      }
    },
    actions = {
      IconButton(
        onClick = {
          if (event != null) {
            onFavoriteClick()
          }
        },
        enabled = !isLoading && (event != null || !error.isNullOrBlank())
      ) {
        Icon(
          if (event?.isFavorite == true) Icons.Filled.Star
          else Icons.Outlined.StarOutline,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.tertiary
        )
      }
    },
  )
}

@Composable
private fun EventDetailBottomBar(
  isLoading: Boolean,
  error: String?,
  event: EventDetail?,
  onRetry: () -> Unit,
) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surface)
      .padding(24.dp)
  ) {
    if (!isLoading && (error != null || event == null)) {
      RetryButton(onRetry)
    } else {
      RegisterButton(event)
    }
  }
}

@Composable
private fun RetryButton(onRetry: () -> Unit) {
  Button(
    colors = ButtonDefaults.filledTonalButtonColors().copy(
      containerColor = MaterialTheme.colorScheme.errorContainer,
      contentColor = MaterialTheme.colorScheme.onErrorContainer
    ),
    onClick = onRetry,
    modifier = Modifier.fillMaxWidth(),
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Filled.Refresh,
        contentDescription = null,
        modifier = Modifier.padding(end = 8.dp),
      )
      Text("Retry")
    }
  }
}

@Composable
private fun RegisterButton(event: EventDetail?) {
  val context = LocalContext.current
  FilledTonalButton(
    onClick = {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event?.link ?: ""))
      context.startActivity(intent)
    },
    modifier = Modifier.fillMaxWidth()
  ) {
    Text("Register")
  }
}

private fun LazyListScope.buildCover(
  isLoading: Boolean,
  error: String?,
  cover: String?,
) {
  item {
    when {
      isLoading || !error.isNullOrBlank() || cover == null -> {
        ShimmerBox(
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
        )
      }

      else -> {
        SubcomposeAsyncImage(
          model = cover,
          loading = {
            ShimmerBox(
              modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
            )
          },
          error = {
            ShimmerBox(
              animate = false,
              modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            )
          },
          contentDescription = null,
          alignment = Alignment.TopCenter,
          contentScale = ContentScale.FillBounds,
          modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
        )
      }
    }
  }
}

private fun LazyListScope.buildFallback(
  isLoading: Boolean,
  error: String?,
  event: EventDetail?,
) {
  val message = when {
    isLoading -> error ?: "Fetching event details..."

    event == null -> error
      ?: "Looks like we couldn't find the event details you're looking for."

    else -> null
  }

  item {
    Spacer(modifier = Modifier.height(16.dp))
    message?.let {
      Text(
        it,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .fillMaxWidth()
      )
    }
  }
}

fun LazyListScope.buildContent(event: EventDetail) {
  val metadata = listOf(
    EventDetailMetadataItem(
      icon = Icons.Filled.Category,
      text = event.category
    ),
    EventDetailMetadataItem(
      icon = Icons.Filled.Event,
      text = event.getDisplayDate()
    ),
    EventDetailMetadataItem(
      icon = Icons.Filled.PeopleAlt,
      text = event.getRemainingQuota()
    ),
  )

  item {
    Spacer(modifier = Modifier.height(16.dp))
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)
    ) {
      Text(
        event.cityName,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.secondary,
        modifier = Modifier.padding(bottom = 4.dp)
      )
      Text(
        event.name,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.padding(bottom = 8.dp)
      )
      Text(
        event.summary,
        maxLines = 3,
        color = MaterialTheme.colorScheme.outline,
        overflow = TextOverflow.Ellipsis,
      )
    }
    Spacer(modifier = Modifier.height(16.dp))
  }
  items(metadata) { EventDetailMetadata(it) }
  item {
    Spacer(modifier = Modifier.height(16.dp))
    HtmlRenderer(
      html = event.description,
      modifier = Modifier.padding(horizontal = 16.dp)
    )
  }
}

@Stable
private data class EventDetailMetadataItem(
  val icon: ImageVector,
  val text: String,
)

@Composable
private fun EventDetailMetadata(metadata: EventDetailMetadataItem) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp)
  ) {
    Icon(
      imageVector = metadata.icon,
      contentDescription = null,
      tint = MaterialTheme.colorScheme.tertiary,
      modifier = Modifier.size(16.dp)
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(
      text = metadata.text,
      style = MaterialTheme.typography.labelLarge,
      color = MaterialTheme.colorScheme.tertiary,
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventDetailScreenLoadingPreview() {
  AppTheme {
    EventDetailScreenContent(
      event = null,
      isLoading = true,
      error = null,
      onPop = {},
      onRetry = {},
      onFavoriteClick = {},
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventDetailScreenNotFoundPreview() {
  AppTheme {
    EventDetailScreenContent(
      event = null,
      isLoading = false,
      error = null,
      onPop = {},
      onRetry = {},
      onFavoriteClick = {},
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EventDetailScreenPreview() {
  val event = EventDetail.fake()

  AppTheme {
    EventDetailScreenContent(
      event = event,
      isLoading = false,
      error = null,
      onPop = {},
      onRetry = {},
      onFavoriteClick = {},
    )
  }
}