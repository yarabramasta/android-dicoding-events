package dev.ybrmst.dicodingevents.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicodingevents.data.local.EventsDatabase
import dev.ybrmst.dicodingevents.data.local.FavoriteEventDao
import dev.ybrmst.dicodingevents.data.network.DicodingEventsApiService
import dev.ybrmst.dicodingevents.data.network.EventsNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

  private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  @Provides
  @Singleton
  fun provideOkHttp() = OkHttpClient
    .Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(25, TimeUnit.SECONDS)
    .build()

  @Provides
  @Singleton
  fun provideRetrofit(okHttp: OkHttpClient): Retrofit =
    Retrofit
      .Builder()
      .baseUrl("https://event-api.dicoding.dev")
      .addConverterFactory(
        Json.asConverterFactory("application/json; charset=UTF8".toMediaType())
      )
      .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
      .client(okHttp)
      .build()

  @Provides
  @Singleton
  fun provideDicodingEventsApiService(retrofit: Retrofit): DicodingEventsApiService =
    retrofit.create(DicodingEventsApiService::class.java)

  @Provides
  @Singleton
  fun provideEventsNetworkDataSource(
    api: DicodingEventsApiService,
    @IoDispatcher ioDispatcher: CoroutineDispatcher,
  ): EventsNetworkDataSource = EventsNetworkDataSource(api, ioDispatcher)

  @Provides
  @Singleton
  fun provideAppDatabase(@ApplicationContext appContext: Context): EventsDatabase {
    return Room.databaseBuilder(
      appContext,
      EventsDatabase::class.java,
      "events_database"
    ).build()
  }

  @Provides
  @Singleton
  fun provideFavoriteEventDao(appDatabase: EventsDatabase): FavoriteEventDao {
    return appDatabase.favEventDao()
  }
}