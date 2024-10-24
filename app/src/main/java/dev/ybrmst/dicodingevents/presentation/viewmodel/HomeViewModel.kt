package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repo: EventsRepository,
) :
  ViewModel(),
  UnidirectionalViewModel<HomeContract.State, HomeContract.Event, HomeContract.Effect> {

  private val mutableState = MutableStateFlow(HomeContract.State.initial())
  override val state: StateFlow<HomeContract.State>
    get() = mutableState.asStateFlow()

  private val effectFlow = MutableSharedFlow<HomeContract.Effect>()
  override val effect: SharedFlow<HomeContract.Effect>
    get() = effectFlow.asSharedFlow()

  override fun add(event: HomeContract.Event) {
    when (event) {
      HomeContract.Event.OnFetch -> fetchEvents(isRefresh = false)
      HomeContract.Event.OnRefresh -> fetchEvents(isRefresh = true)
      is HomeContract.Event.OnEventClicked -> navigateToDetail(event.eventId)
      is HomeContract.Event.OnEventFavoriteChanged -> toggleFavorite(event.event)
    }
  }

  private val upcomingEvents: List<EventPreview>
    get() = mutableState.value.upcomingEvents

  private val finishedEvents: List<EventPreview>
    get() = mutableState.value.finishedEvents

  private fun fetchEvents(isRefresh: Boolean) {
    viewModelScope.launch {
      mutableState.value = mutableState.value.copy(
        isFetching = !isRefresh,
        isRefreshing = isRefresh
      )

      repo.getUpcomingFinishedEvents().collect {
        it
          .onSuccess {
            mutableState.value = mutableState.value.copy(
              isFetching = false,
              isRefreshing = false,
              upcomingEvents = data.first,
              finishedEvents = data.second
            )
          }
          .onFailure {
            mutableState.value = mutableState.value.copy(
              isFetching = false,
              isRefreshing = false,
              error = this.messageOrNull
                ?: "Uh oh! Unexpected error occurred.\nPlease try again."
            )
          }
      }
    }
  }

  private fun navigateToDetail(eventId: Int) {
    effectFlow.tryEmit(HomeContract.Effect.NavigateToDetail(eventId))
  }

  private fun toggleFavorite(event: EventPreview) {
    viewModelScope.launch {
      val (updatedEvent, error) =
        if (event.isFavorite) repo.removeFavEvent(event)
        else repo.addFavEvent(event)

      if (error != null) {
        effectFlow.tryEmit(
          HomeContract.Effect.ShowToast("Failed to add event to favorites.")
        )
      } else {
        mutableState.value = mutableState.value.copy(
          upcomingEvents = updateEventList(upcomingEvents, updatedEvent),
          finishedEvents = updateEventList(finishedEvents, updatedEvent)
        )
      }
    }
  }

  private fun updateEventList(
    events: List<EventPreview>,
    updatedEvent: EventPreview,
  ): List<EventPreview> = events.map {
    if (it.id == updatedEvent.id) updatedEvent else it
  }
}

interface HomeContract {
  data class State(
    val isFetching: Boolean,
    val isRefreshing: Boolean,
    val error: String? = null,
    val upcomingEvents: List<EventPreview>,
    val finishedEvents: List<EventPreview>,
  ) {
    companion object {
      private val events = listOf(
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
        EventPreview.fake(),
      ).distinctBy { it.id }

      fun initial() = State(
        isFetching = false,
        isRefreshing = false,
        upcomingEvents = events.take(2),
        finishedEvents = events.takeLast(3)
      )
    }
  }

  sealed class Event {
    data object OnFetch : Event()
    data object OnRefresh : Event()
    data class OnEventClicked(val eventId: Int) : Event()
    data class OnEventFavoriteChanged(val event: EventPreview) : Event()
  }

  sealed class Effect {
    data class NavigateToDetail(val eventId: Int) : Effect()

    data class ShowToast(val message: String) : Effect()
  }
}