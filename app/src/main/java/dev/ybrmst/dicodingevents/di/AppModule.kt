package dev.ybrmst.dicodingevents.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicodingevents.MainApp
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class IoDispatcher

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class DefaultDispatcher

@Retention
@Qualifier
annotation class MainDispatcher

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

  @Provides
  @IoDispatcher
  fun provideIoDispatcher() = Dispatchers.IO

  @Provides
  @DefaultDispatcher
  fun provideDefaultDispatcher() = Dispatchers.Default

  @Provides
  @MainDispatcher
  fun provideMainDispatcher() = Dispatchers.Main

  @Provides
  @Singleton
  fun provideApplication(@ApplicationContext app: Context) = app as MainApp
}