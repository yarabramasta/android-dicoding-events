package dev.ybrmst.dicodingevents.domain.models

import dev.ybrmst.dicodingevents.lib.DateTimeParser
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventDetail(
  val id: Int,
  val name: String,
  val summary: String,
  val description: String,
  val imageLogo: String,
  val mediaCover: String,
  val category: String,
  val ownerName: String,
  val cityName: String,
  val quota: Int,
  val registrants: Int,
  val link: String,
  val isFavorite: Boolean = false,

  @Serializable(with = DateTimeParser::class)
  @SerialName("beginTime")
  val beginDate: LocalDateTime,

  @Serializable(with = DateTimeParser::class)
  @SerialName("endTime")
  val endDate: LocalDateTime,
)

internal fun extractTime(
  date: LocalDateTime,
) = LocalDateTime.Format {
  time(
    LocalTime.Format {
      hour(); char(':'); minute()
    }
  )
}.format(date)

internal fun formatDatePretty(
  date: LocalDateTime,
  includeWeekDay: Boolean = true,
) = LocalDateTime.Format {
  date(
    LocalDate.Format {
      if (includeWeekDay) {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
        chars(", ")
      }
      dayOfMonth()
      char(' ')
      monthName(MonthNames.ENGLISH_ABBREVIATED)
      char(' ')
      year()
    }
  )
}.format(date)

fun EventDetail.getDisplayDate(): String {
  if (beginDate.dayOfMonth == endDate.dayOfMonth) {
    val date = formatDatePretty(beginDate)
    val beginTime = extractTime(beginDate)
    val endTime = extractTime(endDate)

    return "$date at $beginTime - $endTime"
  } else {
    val beginDate = formatDatePretty(beginDate, includeWeekDay = false)
    val endDate = formatDatePretty(endDate, includeWeekDay = false)

    return "$beginDate - $endDate"
  }
}

fun EventDetail.toPreview(): EventPreview {
  return EventPreview(
    id = id,
    name = name,
    summary = summary,
    cityName = cityName,
    imageLogo = imageLogo,
  )
}