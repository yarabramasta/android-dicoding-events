package dev.ybrmst.dicoding_events.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicoding_events.data.local.DataStoreService
import dev.ybrmst.dicoding_events.data.network.DicodingEventsApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

  @Provides
  fun provideDataStoreService(
    @ApplicationContext context: Context,
  ): DataStoreService = DataStoreService(context)


  @Provides
  fun provideBaseUrl() = "https://event-api.dicoding.dev"

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  @Provides
  fun provideOkHttp() = OkHttpClient
    .Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(25, TimeUnit.SECONDS)
    .build()

  @Provides
  fun provideRetrofit(baseUrl: String, okHttp: OkHttpClient): Retrofit =
    Retrofit
      .Builder()
      .baseUrl(baseUrl)
      .addConverterFactory(GsonConverterFactory.create())
      .client(okHttp)
      .build()

  @Provides
  fun provideDicodingEventsApiService(retrofit: Retrofit): DicodingEventsApiService =
    retrofit.create(DicodingEventsApiService::class.java)
}