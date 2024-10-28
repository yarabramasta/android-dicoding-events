package dev.ybrmst.dicodingevents.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import dev.ybrmst.dicodingevents.domain.models.EventPreview

@Entity(
  tableName = "fav_events",
  indices = [Index(value = ["event_id"], unique = true)]
)
data class FavoriteEventEntity(
  @PrimaryKey(autoGenerate = true) val id: Int = 0,

  @ColumnInfo(name = "event_id") val eventId: Int,

  @ColumnInfo(index = true) val name: String,

  val summary: String,

  @ColumnInfo(name = "city_name") val cityName: String,

  @ColumnInfo(name = "image_logo") val imageLogo: String,
)

fun FavoriteEventEntity.toPreview(): EventPreview {
  return EventPreview(
    id = eventId,
    name = name,
    summary = summary,
    cityName = cityName,
    imageLogo = imageLogo,
  )
}