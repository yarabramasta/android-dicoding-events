package dev.ybrmst.dicoding_events.feat.event.presentation

import dev.ybrmst.dicoding_events.feat.event.domain.Event

data class EventsState(
  val highlightedEvents: List<Event> = emptyList(),
  val upcomingEvents: List<Event> = emptyList(),
  val pastEvents: List<Event> = emptyList(),

  val error: String? = null,

  val isLoading: Boolean = false,

  val searchQuery: String = "",
)