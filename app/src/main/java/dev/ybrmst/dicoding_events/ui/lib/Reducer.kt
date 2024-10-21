package dev.ybrmst.dicoding_events.ui.lib

interface Reducer<State : Reducer.ViewState, Event : Reducer.ViewEvent, Effect : Reducer.ViewEffect> {
  interface ViewState

  interface ViewEvent

  interface ViewEffect

  fun dispatch(prevState: State, event: Event): Pair<State, Effect?>
}