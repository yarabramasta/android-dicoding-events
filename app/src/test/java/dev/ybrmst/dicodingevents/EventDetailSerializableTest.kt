package dev.ybrmst.dicodingevents

import dev.ybrmst.dicodingevents.domain.models.EventDetail
import dev.ybrmst.dicodingevents.domain.models.getDisplayDate
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Test


internal const val JSON_STRING_DIFF_DARY =
  "{\"id\":1,\"name\":\"name\",\"summary\":\"summary\",\"description\":\"description\",\"imageLogo\":\"imagelogo\",\"mediaCover\":\"mediacover\",\"category\":\"category\",\"ownerName\":\"ownername\",\"cityName\":\"cityname\",\"quota\":10,\"registrants\":10,\"beginTime\":\"2024-10-18 13:00:00\",\"endTime\":\"2024-10-19 17:00:00\",\"link\":\"link\"}"

internal const val JSON_STRING_SAME_DAY =
  "{\"id\":1,\"name\":\"name\",\"summary\":\"summary\",\"description\":\"description\",\"imageLogo\":\"imagelogo\",\"mediaCover\":\"mediacover\",\"category\":\"category\",\"ownerName\":\"ownername\",\"cityName\":\"cityname\",\"quota\":10,\"registrants\":10,\"beginTime\":\"2024-10-18 13:00:00\",\"endTime\":\"2024-10-18 17:00:00\",\"link\":\"link\"}"

class EventDetailSerializableTest {

  @Test
  fun `should be able to parse string dates to LocalDateTime`() {
    val event = Json.decodeFromString(
      EventDetail.serializer(),
      JSON_STRING_SAME_DAY
    )

    assertEquals(2024, event.beginDate.year)
    assertEquals(10, event.beginDate.monthNumber)
    assertEquals(18, event.beginDate.dayOfMonth)
    assertEquals(13, event.beginDate.hour)
    assertEquals(0, event.beginDate.minute)

    assertEquals(2024, event.endDate.year)
    assertEquals(10, event.endDate.monthNumber)
    assertEquals(18, event.endDate.dayOfMonth)
    assertEquals(17, event.endDate.hour)
    assertEquals(0, event.endDate.minute)
  }

  @Test
  fun `should be able to merge dates into display date for ui`() {
    val dateSameDay = Json.decodeFromString(
      EventDetail.serializer(),
      JSON_STRING_SAME_DAY
    ).getDisplayDate()

    val dateDiffDay = Json.decodeFromString(
      EventDetail.serializer(),
      JSON_STRING_DIFF_DARY
    ).getDisplayDate()

    assertEquals("Fri, 18 Oct 2024 at 13:00 - 17:00", dateSameDay)
    assertEquals("18 Oct 2024 - 19 Oct 2024", dateDiffDay)
  }
}