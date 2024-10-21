package dev.ybrmst.dicoding_events.domain.models

import kotlin.random.Random

data class EventPreview(
  val id: Int,
) {
  companion object {
    fun fake() = EventPreview(
      id = Random.nextInt()
    )
  }
}