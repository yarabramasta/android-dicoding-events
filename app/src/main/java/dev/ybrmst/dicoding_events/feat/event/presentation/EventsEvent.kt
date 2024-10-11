package dev.ybrmst.dicoding_events.feat.event.presentation

sealed class EventsEvent {
  data object LoadHighlighted : EventsEvent()
  data object LoadUpcoming : EventsEvent()
  data object LoadPast : EventsEvent()
}