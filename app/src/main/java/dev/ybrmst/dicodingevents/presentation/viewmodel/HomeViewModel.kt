package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
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

  private val effectFlow =
    Channel<HomeContract.Effect>(capacity = Channel.CONFLATED)
  override val effect = effectFlow.receiveAsFlow()

  override fun add(event: HomeContract.Event) {
    when (event) {
      HomeContract.Event.OnFetch -> fetchEvents(isRefresh = false)
      HomeContract.Event.OnRefresh -> fetchEvents(isRefresh = true)
      is HomeContract.Event.OnEventClicked -> navigateToDetail(event.eventId)
      is HomeContract.Event.OnEventFavoriteChanged -> toggleFavorite(event.event)
    }
  }

  init {
    add(HomeContract.Event.OnFetch)
  }

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
    effectFlow.trySend(HomeContract.Effect.NavigateToDetail(eventId))
  }

  private fun toggleFavorite(event: EventPreview) {
    viewModelScope.launch {
      val (updatedEvent, error) =
        if (event.isFavorite) repo.removeFavEvent(event)
        else repo.addFavEvent(event)

      if (error != null) {
        effectFlow.trySend(
          HomeContract.Effect.ShowToast("Failed to add event to favorites.")
        )
      } else {
        mutableState.value = mutableState.value.copy(
          upcomingEvents = updateEventList(
            mutableState.value.upcomingEvents,
            updatedEvent
          ),
          finishedEvents = updateEventList(
            mutableState.value.finishedEvents,
            updatedEvent
          )
        )

        effectFlow.trySend(
          HomeContract.Effect.ShowToast(
            if (updatedEvent.isFavorite) "Event added to favorites."
            else "Event removed from favorites."
          )
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