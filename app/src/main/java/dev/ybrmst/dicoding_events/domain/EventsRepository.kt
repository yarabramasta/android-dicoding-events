package dev.ybrmst.dicoding_events.domain

import dev.ybrmst.dicoding_events.data.Resource
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
  suspend fun getHighlightedUpcomingEvents(): Flow<Resource<List<EventPreview>>>

  suspend fun searchEvents(
    status: Int,
    query: String,
  ): Flow<Resource<List<EventPreview>>>

  suspend fun getUpcomingEvents(): Flow<Resource<List<EventPreview>>>

  suspend fun getPastEvents(): Flow<Resource<List<EventPreview>>>

  suspend fun getEventDetail(eventId: Int): Flow<Resource<EventDetail>>
}