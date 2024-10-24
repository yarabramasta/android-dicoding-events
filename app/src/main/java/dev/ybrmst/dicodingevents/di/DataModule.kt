package dev.ybrmst.dicodingevents.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.ybrmst.dicodingevents.data.repositories.EventsRepositoryImpl
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

  @Binds
  fun provideEventsRepository(eventsRepositoryImpl: EventsRepositoryImpl): EventsRepository
}