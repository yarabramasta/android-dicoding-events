package dev.ybrmst.dicodingevents.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicodingevents.data.repositories.EventsRepositoryImpl
import dev.ybrmst.dicodingevents.data.repositories.PreferenceRepositoryImpl
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import dev.ybrmst.dicodingevents.domain.repositories.PreferenceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

  @Binds
  @Singleton
  abstract fun provideEventsRepository(eventsRepositoryImpl: EventsRepositoryImpl): EventsRepository

  @Binds
  @Singleton
  abstract fun providePreferenceRepository(preferenceRepositoryImpl: PreferenceRepositoryImpl): PreferenceRepository
}