package dev.ybrmst.dicodingevents.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicodingevents.data.local.PreferenceService
import dev.ybrmst.dicodingevents.data.network.DicodingEventsApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

  @Provides
  fun providePreferenceService(
    @ApplicationContext context: Context,
  ): PreferenceService = PreferenceService(context)

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
      .addConverterFactory(
        Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
      )
      .client(okHttp)
      .build()

  @Provides
  fun provideDicodingEventsApiService(retrofit: Retrofit): DicodingEventsApiService =
    retrofit.create(DicodingEventsApiService::class.java)
}