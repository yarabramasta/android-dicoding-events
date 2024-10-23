package dev.ybrmst.dicodingevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.ThemeViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      DicodingEventsTheme {
        MainNavigation()
      }
    }
  }
}

@Composable
private fun DicodingEventsTheme(
  vm: ThemeViewModel = hiltViewModel(),
  content: @Composable () -> Unit,
) {
  val themeState by vm.state.collectAsState()

  AppTheme(darkTheme = themeState.isDarkTheme) { content() }
}