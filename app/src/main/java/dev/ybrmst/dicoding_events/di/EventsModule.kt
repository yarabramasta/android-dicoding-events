package dev.ybrmst.dicoding_events.di

import dev.ybrmst.dicoding_events.data.network.EventsApi
import dev.ybrmst.dicoding_events.data.network.EventsRepositoryImpl
import dev.ybrmst.dicoding_events.domain.EventsRepository
import dev.ybrmst.dicoding_events.ui.viewmodel.event.detail.DetailViewModel
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
  single<EventsRepository> { EventsRepositoryImpl(get()) }

  viewModel { HomeViewModel(get()) }

  viewModel { DetailViewModel(get()) }
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