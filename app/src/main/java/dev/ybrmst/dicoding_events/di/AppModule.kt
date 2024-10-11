package dev.ybrmst.dicoding_events.di

import android.content.Context
import dev.ybrmst.dicoding_events.feat.event.data.EventApi
import dev.ybrmst.dicoding_events.feat.event.data.EventRepositoryImpl
import dev.ybrmst.dicoding_events.feat.event.domain.EventRepository
import dev.ybrmst.dicoding_events.feat.event.presentation.EventsViewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppModule {
  val retrofit: Retrofit
  val eventApi: EventApi
  val eventRepo: EventRepository
  val eventsViewModel: EventsViewModel
}

class AppModuleImpl(val context: Context) : AppModule {
  override val retrofit: Retrofit by lazy {
    Retrofit.Builder()
      .baseUrl(EventApi.BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
  }

  override val eventApi: EventApi by lazy {
    retrofit.create(EventApi::class.java)
  }

  override val eventRepo: EventRepository by lazy {
    EventRepositoryImpl(eventApi)
  }

  override val eventsViewModel: EventsViewModel by lazy {
    EventsViewModel(eventRepo)
  }
}