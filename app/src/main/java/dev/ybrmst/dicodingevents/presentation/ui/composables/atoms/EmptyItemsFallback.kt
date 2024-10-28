package dev.ybrmst.dicodingevents.presentation.ui.composables.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.ybrmst.dicodingevents.R
import dev.ybrmst.dicodingevents.presentation.ui.composables.isDarkThemeEnabled
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsViewModel

@Composable
fun EmptyItemsFallback(
  modifier: Modifier = Modifier,
  onRefresh: (() -> Unit)? = null,
  message: String? = null,
) {
  val vm = hiltViewModel<SettingsViewModel>()
  val settings by vm.state.collectAsState()

  EmptyItemsFallbackContent(
    modifier = modifier,
    isDarkTheme = isDarkThemeEnabled(settings.themeMode),
    message = message,
    onRefresh = onRefresh
  )
}

@Composable
private fun EmptyItemsFallbackContent(
  modifier: Modifier,
  isDarkTheme: Boolean = false,
  message: String? = null,
  onRefresh: (() -> Unit)? = null,
) {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Image(
      painter = painterResource(
        if (isDarkTheme) R.drawable.img_state_empty_illustration_dark
        else R.drawable.img_state_empty_illustration_light
      ),
      contentDescription = null,
      modifier = Modifier.size(240.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = message ?: "There's nothing to show here...",
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.outline,
      style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    if (onRefresh != null) {
      FilledTonalButton(onClick = onRefresh) {
        Text("Refresh")
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun EmptyItemsFallbackPreview() {
  AppTheme {
    Scaffold {
      EmptyItemsFallbackContent(
        onRefresh = {},
        modifier = Modifier.padding(it)
      )
    }
  }
}