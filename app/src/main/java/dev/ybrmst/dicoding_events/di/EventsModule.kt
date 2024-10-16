package dev.ybrmst.dicoding_events.di

import dev.ybrmst.dicoding_events.data.network.EventsApi
import dev.ybrmst.dicoding_events.data.repo.EventRepositoryImpl
import dev.ybrmst.dicoding_events.domain.repo.EventRepository
import dev.ybrmst.dicoding_events.ui.viewmodel.event.detail.EventDetailViewModel
import dev.ybrmst.dicoding_events.ui.viewmodel.event.finished.FinishedEventsViewModel
import dev.ybrmst.dicoding_events.ui.viewmodel.event.upcoming.UpcomingEventsViewModel
import dev.ybrmst.dicoding_events.ui.viewmodel.home.HomeViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val eventsModule = module {
  factory { getHttpClient() }

  single {
    Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl(EventsApi.BASE_URL)
      .client(get())
      .build()
      .create(EventsApi::class.java)
  }
  single<EventRepository> { EventRepositoryImpl(get()) }

  viewModel { HomeViewModel(get()) }

  viewModel { EventDetailViewModel(get()) }

  viewModel { UpcomingEventsViewModel(get()) }

  viewModel { FinishedEventsViewModel(get()) }
}

private fun getHttpClient(): OkHttpClient {
  val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  return OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(25, TimeUnit.SECONDS)
    .build()
}