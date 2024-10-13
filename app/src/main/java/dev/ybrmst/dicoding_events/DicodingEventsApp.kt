package dev.ybrmst.dicoding_events

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import dev.ybrmst.dicoding_events.di.eventsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DicodingEventsApp : Application(), SingletonImageLoader.Factory {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidLogger()
      androidContext(this@DicodingEventsApp)
      modules(
        eventsModule
      )
    }


  }

  override fun newImageLoader(context: PlatformContext): ImageLoader {
    return ImageLoader.Builder(context)
      .diskCachePolicy(CachePolicy.ENABLED)
      .build()
  }
}