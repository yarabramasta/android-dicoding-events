package dev.ybrmst.dicoding_events.ui.composables.nav

import kotlinx.serialization.Serializable

@Serializable
data object MainRoute

@Serializable
data class EventDetailRoute(val eventId: Int)