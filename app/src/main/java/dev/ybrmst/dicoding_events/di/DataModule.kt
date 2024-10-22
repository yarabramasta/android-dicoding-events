package dev.ybrmst.dicoding_events.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicoding_events.data.repositories.EventsRepositoryImpl
import dev.ybrmst.dicoding_events.domain.repositories.EventsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
  @Binds
  fun provideEventsRepository(repository: EventsRepositoryImpl): EventsRepository
}