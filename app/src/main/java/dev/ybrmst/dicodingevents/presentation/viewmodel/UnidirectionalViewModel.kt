package dev.ybrmst.dicodingevents.presentation.viewmodel

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UnidirectionalViewModel<State, Event, Effect> {
  val state: StateFlow<State>
  val effect: SharedFlow<Effect>
  fun add(event: Event)
}