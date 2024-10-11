package dev.ybrmst.dicoding_events.data.network

import dev.ybrmst.dicoding_events.data.Resource
import dev.ybrmst.dicoding_events.domain.EventDetail
import dev.ybrmst.dicoding_events.domain.EventPreview
import dev.ybrmst.dicoding_events.domain.EventsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class EventsRepositoryImpl(
  private val api: EventsApi,
) : EventsRepository {
  override suspend fun getHighlightedUpcomingEvents(): Flow<Resource<List<EventPreview>>> {
    return flow {
      emit(Resource.Loading())
      try {
        val res = api.getEvents(active = 1, limit = 5)
        val events = res.body()?.listEvents ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }
    }
  }

  override suspend fun searchEvents(
    status: Int,
    query: String,
  ): Flow<Resource<List<EventPreview>>> {
    return flow {
      emit(Resource.Loading())
      try {
        val res = api.getEvents(query = query, active = status)
        val events = res.body()?.listEvents ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }
    }
  }

  override suspend fun getUpcomingEvents(): Flow<Resource<List<EventPreview>>> {
    return flow {
      emit(Resource.Loading())
      try {
        val res = api.getEvents(active = 1)
        val events = res.body()?.listEvents ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }
    }
  }

  override suspend fun getPastEvents(): Flow<Resource<List<EventPreview>>> {
    return flow {
      emit(Resource.Loading())
      try {
        val res = api.getEvents(active = 0)
        val events = res.body()?.listEvents ?: emptyList()
        emit(Resource.Success(events))
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch data, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }
    }
  }

  override suspend fun getEventDetail(eventId: Int): Flow<Resource<EventDetail>> {
    return flow {
      emit(Resource.Loading())
      try {
        val res = api.getEventDetail(eventId)
        val event = res.body()?.event
        if (event != null) {
          emit(Resource.Success(event))
        } else {
          emit(Resource.Error("No event detail found."))
        }
      } catch (ex: IOException) {
        ex.printStackTrace()
        emit(Resource.Error("Can't fetch event detail, please try again later."))
      } catch (ex: Exception) {
        ex.printStackTrace()
        emit(Resource.Error("Uh oh, Something went wrong..."))
      }
    }
  }
}