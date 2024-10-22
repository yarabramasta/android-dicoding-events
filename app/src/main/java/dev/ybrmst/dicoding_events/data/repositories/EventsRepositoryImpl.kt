package dev.ybrmst.dicoding_events.data.repositories

import dev.ybrmst.dicoding_events.data.network.DicodingEventsApiService
import dev.ybrmst.dicoding_events.di.IoDispatcher
import dev.ybrmst.dicoding_events.domain.errors.FetchEventError
import dev.ybrmst.dicoding_events.domain.models.EventPreview
import dev.ybrmst.dicoding_events.domain.models.Result
import dev.ybrmst.dicoding_events.domain.repositories.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class EventsRepositoryImpl @Inject constructor(
  private val api: DicodingEventsApiService,
  @IoDispatcher private val ioDispatcher: CoroutineContext,
) : EventsRepository {
  override suspend fun fetchUpcomingEvents(q: String): Flow<Result<List<EventPreview>, FetchEventError>> {
    return flow {
      emit(Result.Loading)

      val events = fetchEvents(active = 1)

      if (events.isError()) {
        emit(events)
      } else {
        emit(Result.Success(events.getOrNull() ?: emptyList()))
      }
    }
  }

  override suspend fun fetchFinishedEvents(q: String): Flow<Result<List<EventPreview>, FetchEventError>> {
    return flow {
      emit(Result.Loading)

      val events = fetchEvents(active = 0)

      if (events.isError()) {
        emit(events)
      } else {
        emit(Result.Success(events.getOrNull() ?: emptyList()))
      }
    }
  }

  override suspend fun fetchUpcomingFinishedEvents(): Flow<Result<Pair<List<EventPreview>, List<EventPreview>>, FetchEventError>> {
    return flow {
      emit(Result.Loading)

      val upcomingEvents = fetchEvents(active = 1)
      val finishedEvents = fetchEvents(active = 0)

      if (upcomingEvents.isError() || finishedEvents.isError()) {
        emit(upcomingEvents as? Result.Error ?: finishedEvents as Result.Error)
      } else {
        emit(
          Result.Success(
            Pair(
              upcomingEvents.getOrNull() ?: emptyList(),
              finishedEvents.getOrNull() ?: emptyList()
            )
          )
        )
      }
    }
  }

  /**
   * Fetch events from the API
   *
   * @param q search query
   * @param limit limit of events to fetch
   * @param active active status of the events: 1 for upcoming, 0 for finished, -1 for all
   */
  private suspend fun fetchEvents(
    q: String? = null,
    limit: Int = 20,
    active: Int = -1,
  ): Result<List<EventPreview>, FetchEventError> {
    return try {
      withContext(ioDispatcher) {
        val res = api.getEvents(active = active, limit = limit, query = q)
        if (!res.isSuccessful) {
          Result.Error(FetchEventError.BadRequest(res.message()))
        } else {
          Result.Success(res.body()?.listEvents ?: emptyList())
        }
      }
    } catch (e: IOException) {
      Result.Error(FetchEventError.NetworkError())
    } catch (e: Exception) {
      Result.Error(FetchEventError.UnknownError())
    }
  }
}