package dev.ybrmst.dicoding_events

import android.app.Application
import dev.ybrmst.dicoding_events.di.eventsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DicodingEventsApp : Application() {
  override fun onCreate() {
    super.onCreate()

    startKoin {
      androidContext(this@DicodingEventsApp)
      modules(
        eventsModule
      )
    }
  }
}