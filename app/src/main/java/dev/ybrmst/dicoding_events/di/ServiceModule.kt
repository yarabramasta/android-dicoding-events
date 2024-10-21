package dev.ybrmst.dicoding_events.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicoding_events.data.local.DataStoreService

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
  @Provides
  fun provideDataStoreService(
    @ApplicationContext context: Context,
  ): DataStoreService = DataStoreService(context)
}