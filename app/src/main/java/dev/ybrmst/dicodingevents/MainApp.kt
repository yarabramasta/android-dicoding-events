package dev.ybrmst.dicodingevents

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import dev.ybrmst.dicodingevents.presentation.viewmodel.SettingsContract

@HiltAndroidApp
class MainApp : Application(), SingletonImageLoader.Factory {
  val settings = mutableStateOf(SettingsContract.State.initial())

  override fun newImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
      .crossfade(true)
      .memoryCachePolicy(CachePolicy.ENABLED)
      .diskCachePolicy(CachePolicy.ENABLED)
      .logger(DebugLogger())
      .build()
  }
}