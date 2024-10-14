package dev.ybrmst.dicoding_events.domain

import dev.ybrmst.dicoding_events.data.Resource

interface EventsRepository {
  suspend fun getEvents(
    active: Int? = -1,
    limit: Int = 20,
    query: String? = null,
  ): Resource<List<EventPreview>>

  suspend fun getEventDetail(eventId: Int): Resource<EventDetail>
}