package dev.ybrmst.dicoding_events

import android.app.Application
import dev.ybrmst.dicoding_events.di.AppModule
import dev.ybrmst.dicoding_events.di.AppModuleImpl

class DicodingEventsApp : Application() {
  companion object {
    lateinit var appModule: AppModule
  }

  override fun onCreate() {
    super.onCreate()
    appModule = AppModuleImpl(this)
  }
}