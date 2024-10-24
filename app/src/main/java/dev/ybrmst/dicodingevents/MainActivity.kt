package dev.ybrmst.dicodingevents

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
  val state by vm.state.collectAsState()
  val context = LocalContext.current as ComponentActivity

  DisposableEffect(state.isDarkTheme) {
    context.enableEdgeToEdge(
      statusBarStyle =
      if (!state.isDarkTheme) {
        SystemBarStyle.light(
          Color.TRANSPARENT,
          Color.TRANSPARENT
        )
      } else SystemBarStyle.dark(Color.TRANSPARENT),
    )

    onDispose {}
  }

  AppTheme(darkTheme = state.isDarkTheme) {
    content()
  }
}