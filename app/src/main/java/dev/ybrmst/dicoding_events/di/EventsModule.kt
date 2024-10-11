package dev.ybrmst.dicoding_events.di

import dev.ybrmst.dicoding_events.data.network.EventsApi
import dev.ybrmst.dicoding_events.data.network.EventsRepositoryImpl
import dev.ybrmst.dicoding_events.domain.EventsRepository
import dev.ybrmst.dicoding_events.ui.viewmodel.detail.EventDetailViewModel
import dev.ybrmst.dicoding_events.ui.viewmodel.highlighted.HighlightedUpcomingEventsViewModel
import dev.ybrmst.dicoding_events.ui.viewmodel.past.PastEventsViewModel
import dev.ybrmst.dicoding_events.ui.viewmodel.upcoming.UpcomingEventsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val eventsModule = module {
  single {
    Retrofit.Builder()
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl(EventsApi.BASE_URL)
      .build()
      .create(EventsApi::class.java)
  }

  single<EventsRepository>(qualifier = named("EventsRepositoryImpl")) {
    EventsRepositoryImpl(get())
  }

  viewModel {
    HighlightedUpcomingEventsViewModel(get(qualifier = named("EventsRepositoryImpl")))
    UpcomingEventsViewModel(get(qualifier = named("EventsRepositoryImpl")))
    PastEventsViewModel(get(qualifier = named("EventsRepositoryImpl")))
    EventDetailViewModel(get(qualifier = named("EventsRepositoryImpl")))
  }
}