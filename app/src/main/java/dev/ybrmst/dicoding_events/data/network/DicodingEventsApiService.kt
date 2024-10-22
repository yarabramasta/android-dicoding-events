package dev.ybrmst.dicoding_events.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DicodingEventsApiService {
  @GET("events")
  suspend fun getEvents(
    @Query("active") active: Int = -1,
    @Query("limit") limit: Int? = 20,
    @Query("q") query: String? = null,
  ): Response<GetEventsResponse>

  @GET("events/{id}")
  suspend fun getEventDetail(@Path("id") id: Int): Response<Any>
}