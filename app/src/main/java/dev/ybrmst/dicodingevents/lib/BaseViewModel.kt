package dev.ybrmst.dicodingevents.lib

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<
        State : ViewModelContract.State,
        Event : ViewModelContract.Event,
        Effect : ViewModelContract.Effect,
        >(initialState: State) : ViewModel() {

  private val mutableState = MutableStateFlow(initialState)
  val state: StateFlow<State>
    get() = mutableState.asStateFlow()

  protected fun setState(reducer: State.() -> State) {
    mutableState.value = reducer(mutableState.value)
  }

  private val effectFlow = Channel<Effect>(capacity = Channel.CONFLATED)
  val effect: Flow<Effect> = effectFlow.receiveAsFlow()

  protected fun sendEffect(effect: Effect) {
    effectFlow.trySend(effect)
  }

  abstract fun add(event: Event)
}