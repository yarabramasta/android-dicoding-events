package dev.ybrmst.dicodingevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import dagger.hilt.android.AndroidEntryPoint
import dev.ybrmst.dicodingevents.presentation.ui.composables.isDarkThemeEnabled
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  @Inject
  lateinit var application: MainApp

  private val settingsViewModel: SettingsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()
    setContent {
      LaunchedEffect(Unit) {
        settingsViewModel.add(SettingsContract.Event.OnFetch)
      }

      val darkTheme = isDarkThemeEnabled(application.settings.value.themeMode)

      AppTheme(darkTheme = darkTheme) {
        MainNavigation()
      }
    }
  }
}