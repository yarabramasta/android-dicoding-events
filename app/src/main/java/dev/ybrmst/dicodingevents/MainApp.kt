package dev.ybrmst.dicodingevents

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import dev.ybrmst.dicodingevents.data.local.PreferencesDataSource
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class MainApp : Application(), SingletonImageLoader.Factory {
  @Inject
  lateinit var prefs: PreferencesDataSource

  var settings by mutableStateOf(SettingsContract.State.initial())

  override fun onCreate() {
    super.onCreate()

    CoroutineScope(Dispatchers.Default).launch {
      val themeMode = prefs.themeFlow.first()
      val dailyNotifOptIn = prefs.dailyNotifOptInFlow.first()

      settings = settings.copy(
        themeMode = themeMode,
        isOptInDailyNotif = dailyNotifOptIn,
      )
    }
  }

  override fun newImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
      .crossfade(true)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .logger(DebugLogger())
      .build()
  }
}