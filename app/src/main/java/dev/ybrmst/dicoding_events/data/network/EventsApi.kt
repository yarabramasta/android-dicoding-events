package dev.ybrmst.dicoding_events.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventsApi {
  @GET("events")
  suspend fun getEvents(
    @Query("active") active: Int = -1,
    @Query("limit") limit: Int? = null,
    @Query("q") query: String? = null,
  ): Response<EventsResponse>

  @GET("events/{id}")
  suspend fun getEventDetail(@Path("id") id: Int): Response<EventDetailResponse>

  companion object {
    const val BASE_URL = "https://event-api.dicoding.dev"
  }
}