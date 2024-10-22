package dev.ybrmst.dicoding_events.domain.repositories

import dev.ybrmst.dicoding_events.domain.errors.FetchEventError
import dev.ybrmst.dicoding_events.domain.models.EventPreview
import dev.ybrmst.dicoding_events.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface EventsRepository {
  suspend fun fetchUpcomingEvents(q: String = ""): Flow<Result<List<EventPreview>, FetchEventError>>

  suspend fun fetchFinishedEvents(q: String = ""): Flow<Result<List<EventPreview>, FetchEventError>>

  suspend fun fetchUpcomingFinishedEvents(): Flow<Result<Pair<List<EventPreview>, List<EventPreview>>, FetchEventError>>
}