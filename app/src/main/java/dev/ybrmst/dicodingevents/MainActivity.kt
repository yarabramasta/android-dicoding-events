package dev.ybrmst.dicodingevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.ybrmst.dicodingevents.presentation.ui.composables.isDarkThemeEnabled
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var application: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      val darkTheme = isDarkThemeEnabled(application.settings.themeMode)

      AppTheme(darkTheme = darkTheme) {
        MainNavigation()
      }
    }
  }
}