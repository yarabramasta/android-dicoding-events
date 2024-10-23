package dev.ybrmst.dicodingevents.presentation.ui.composables.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
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
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.ThemeViewModel

@Composable
fun ErrorFallback(
  modifier: Modifier = Modifier,
  message: String? = null,
  onRetry: () -> Unit,
  vm: ThemeViewModel = hiltViewModel(),
) {
  val themeState by vm.state.collectAsState()

  ErrorFallbackContent(
    modifier = modifier,
    isDarkTheme = themeState.isDarkTheme,
    message = message,
    onRetry = onRetry
  )
}

@Composable
private fun ErrorFallbackContent(
  modifier: Modifier = Modifier,
  message: String? = null,
  isDarkTheme: Boolean = false,
  onRetry: () -> Unit,
) {
  Column(
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = modifier
      .fillMaxSize()
      .padding(16.dp),
  ) {
    Image(
      painter = painterResource(
        if (isDarkTheme) R.drawable.img_state_network_error_illustration_dark
        else R.drawable.img_state_network_error_illustration_light
      ),
      contentDescription = null,
      modifier = Modifier.size(240.dp)
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = message ?: "Uh oh! An error occurred.\nPlease try again...",
      textAlign = TextAlign.Center,
      color = MaterialTheme.colorScheme.outline,
      style = MaterialTheme.typography.bodyLarge
    )
    Spacer(modifier = Modifier.height(16.dp))
    FilledTonalButton(
      onClick = onRetry,
      colors = ButtonDefaults.filledTonalButtonColors().copy(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer
      )
    ) {
      Text("Retry")
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ErrorFallbackPreview() {
  AppTheme {
    Scaffold {
      ErrorFallbackContent(
        onRetry = {},
        modifier = Modifier.padding(it)
      )
    }
  }
}