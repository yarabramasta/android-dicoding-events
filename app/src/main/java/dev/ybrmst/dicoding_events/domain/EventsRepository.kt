package dev.ybrmst.dicoding_events.domain

import dev.ybrmst.dicoding_events.data.Resource

interface EventsRepository {
  suspend fun getEvents(
    active: Int? = -1,
    limit: Int? = null,
    query: String? = null,
  ): Resource<List<EventPreview>>

  suspend fun getEventDetail(eventId: Int): Resource<EventDetail>
}