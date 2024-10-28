package dev.ybrmst.dicodingevents.data.network

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.mapFailure
import com.skydoves.sandwich.mapSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import dev.ybrmst.dicodingevents.di.IoDispatcher
import dev.ybrmst.dicodingevents.domain.models.AppError
import dev.ybrmst.dicodingevents.domain.models.EventDetail
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.models.toPreview
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.Response
import javax.inject.Inject

class EventsNetworkDataSource @Inject constructor(
  private val api: DicodingEventsApiService,
  @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) {
  fun getUpcomingFinishedEvents(): Flow<ApiResponse<Pair<List<EventPreview>, List<EventPreview>>>> {
    return getEvents(active = 1, limit = 5).combine(
      getEvents(
        active = 0,
        limit = 10
      )
    ) { upcoming, finished ->
      if (upcoming.second != null || finished.second != null) {
        ApiResponse.Failure.Error(
          payload = upcoming.second?.message ?: finished.second?.message
        )
      } else {
        ApiResponse.Success(data = upcoming.first to finished.first)
      }
    }.flowOn(ioDispatcher)
  }

  fun queryEvents(
    active: Int,
    query: String? = null,
  ): Flow<ApiResponse<List<EventPreview>>> {
    return getEvents(query = query, active = active).map {
      if (it.second != null) {
        ApiResponse.Failure.Error(payload = it.second?.message)
      } else {
        ApiResponse.Success(data = it.first)
      }
    }.flowOn(ioDispatcher)
  }

  suspend fun getEventDetail(id: Int): ApiResponse<EventDetail> {
    return api.fetchEventDetail(id)
      .mapSuccess { this.event }
      .mapFailure {
        when (this) {
          is ApiResponse.Failure.Error -> {
            val statusCode = (payload as? Response<*>)?.code() ?: 500
            when (statusCode) {
              400 -> AppError.BadRequest()
              404 -> AppError.NotFound()
              500 -> AppError.InternalServerError()
              else -> AppError.Network()
            }
          }

          is ApiResponse.Failure.Exception -> {
            AppError.Network()
          }

          else -> AppError.Network()
        }
      }
  }

  private fun getEvents(
    query: String? = null,
    limit: Int = 20,
    active: Int = -1,
  ): Flow<Pair<List<EventPreview>, AppError?>> {
    return flow {
      val res = api.fetchEvents(query = query, limit = limit, active = active)

      res
        .suspendOnSuccess {
          val events = data.listEvents.map { it.toPreview() }
          emit(events to null)
        }
        .suspendOnError {
          val statusCode = (payload as? Response<*>)?.code() ?: 500
          when (statusCode) {
            404 -> emit(emptyList<EventPreview>() to AppError.NotFound())
            500 -> emit(emptyList<EventPreview>() to AppError.InternalServerError())
            else -> emit(emptyList<EventPreview>() to AppError.BadRequest())
          }
        }
        .suspendOnException {
          emit(emptyList<EventPreview>() to AppError.Network())
        }
    }.flowOn(ioDispatcher)
  }
}