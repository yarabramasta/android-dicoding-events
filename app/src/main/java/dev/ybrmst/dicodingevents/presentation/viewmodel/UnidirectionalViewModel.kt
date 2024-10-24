package dev.ybrmst.dicodingevents.presentation.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface UnidirectionalViewModel<State, Event, Effect> {
  val state: StateFlow<State>
  val effect: Flow<Effect>
  fun add(event: Event)
}