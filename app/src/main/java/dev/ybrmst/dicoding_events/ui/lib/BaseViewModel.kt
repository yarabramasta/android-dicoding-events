package dev.ybrmst.dicoding_events.ui.lib

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect>(
  initialState: State,
  private val reducer: Reducer<State, Event, Effect>,
) : ViewModel() {
  private val _state: MutableStateFlow<State> =
    MutableStateFlow(initialState)
  val state: StateFlow<State>
    get() = _state.asStateFlow()

  private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
  val event: SharedFlow<Event>
    get() = _event.asSharedFlow()

  private val _effect = Channel<Effect>(capacity = Channel.CONFLATED)
  val effect = _effect.receiveAsFlow()

  fun sendEvent(event: Event) {
    val (newState, _) = reducer.dispatch(_state.value, event)
    _state.tryEmit(newState)
  }

  fun sendEffect(effect: Effect) {
    _effect.trySend(effect)
  }

  fun sendEventForEffect(event: Event) {
    val (newState, newEffect) = reducer.dispatch(_state.value, event)
    _state.tryEmit(newState)
    newEffect?.let { sendEffect(it) }
  }
}