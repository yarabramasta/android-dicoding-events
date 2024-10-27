package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val repo: EventsRepository,
) : BaseViewModel<
        HomeContract.State,
        HomeContract.Event,
        HomeContract.Effect>
  (initialState = HomeContract.State.initial()) {

  override fun add(event: HomeContract.Event) {
    when (event) {
      HomeContract.Event.OnFetch -> {
        setState { copy(isFetching = true, isRefreshing = false) }
        fetchEvents()
      }
      HomeContract.Event.OnRefresh -> {
        setState { copy(isRefreshing = true, isFetching = false) }
        fetchEvents()
      }
      is HomeContract.Event.OnEventClicked -> navigateToDetail(event.eventId)

      is HomeContract.Event.OnEventFavoriteChanged -> toggleFavorite(event.event)

      HomeContract.Event.OnScreenChanged -> {
        setState {
          copy(
            isFetching = false,
            isRefreshing = false,
            error = null
          )
        }
        fetchEvents()
      }
    }
  }

  init {
    add(HomeContract.Event.OnFetch)
  }

  private fun fetchEvents() {
    viewModelScope.launch {
      repo.getUpcomingFinishedEvents().collect {
        it
          .onSuccess {
            setState {
              copy(
                isFetching = false,
                isRefreshing = false,
                upcomingEvents = data.first,
                finishedEvents = data.second
              )
            }
          }
          .onFailure {
            setState {
              copy(
                isFetching = false,
                isRefreshing = false,
                error = messageOrNull
                  ?: "Uh oh! Unexpected error occurred.\nPlease try again."
              )
            }
          }
      }
    }
  }

  private fun navigateToDetail(eventId: Int) {
    sendEffect(HomeContract.Effect.NavigateToDetail(eventId))
  }

  private fun toggleFavorite(event: EventPreview) {
    viewModelScope.launch {
      val (updatedEvent, error) =
        if (event.isFavorite) repo.removeFavEvent(event)
        else repo.addFavEvent(event)

      if (error != null) {
        sendEffect(HomeContract.Effect.ShowToast("Failed to add event to favorites."))
      } else {
        setState {
          copy(
            upcomingEvents = updateEventList(upcomingEvents, updatedEvent),
            finishedEvents = updateEventList(finishedEvents, updatedEvent)
          )
        }

        sendEffect(
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