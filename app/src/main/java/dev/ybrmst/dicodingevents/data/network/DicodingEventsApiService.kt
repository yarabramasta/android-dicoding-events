package dev.ybrmst.dicodingevents.data.network

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DicodingEventsApiService {
  @GET("events")
  suspend fun fetchEvents(
    @Query("active") active: Int = -1,
    @Query("limit") limit: Int? = 20,
    @Query("q") query: String? = null,
  ): ApiResponse<GetEventsApiResponse>

  @GET("events/{id}")
  suspend fun fetchEventDetail(@Path("id") id: Int): ApiResponse<GetEventDetailApiResponse>
}