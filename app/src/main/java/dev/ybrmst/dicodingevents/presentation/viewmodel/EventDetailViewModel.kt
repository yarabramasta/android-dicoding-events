package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.models.toPreview
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel @Inject constructor(
  private val savedHandleState: SavedStateHandle,
  private val repo: EventsRepository,
) :
  BaseViewModel<
          EventDetailContract.State,
          EventDetailContract.Event,
          EventDetailContract.Effect>
    (initialState = EventDetailContract.State.initial()) {

  override fun add(event: EventDetailContract.Event) {
    when (event) {
      is EventDetailContract.Event.OnFetch -> {
        setState { copy(isFetching = true, isRefreshing = false) }
        fetchEventDetail(event.eventId)
      }

      is EventDetailContract.Event.OnRefresh -> {
        setState { copy(isRefreshing = true, isFetching = false) }
        fetchEventDetail(event.eventId)
      }

      is EventDetailContract.Event.OnFavoriteChanged -> {
        toggleFavorite()
      }
    }
  }

  init {
    viewModelScope.launch {
      val eventId = savedHandleState.get<Int>("eventId")
      if (eventId != null) {
        add(EventDetailContract.Event.OnFetch(eventId))
      }
    }
  }

  private fun fetchEventDetail(eventId: Int) {
    viewModelScope.launch {
      repo.getEventDetail(eventId)
        .onSuccess {
          setState {
            copy(
              isFetching = false,
              isRefreshing = false,
              event = data
            )
          }
        }
        .onFailure {
          setState {
            copy(
              isFetching = false,
              isRefreshing = false,
              error = messageOrNull
            )
          }
        }
    }
  }

  private fun toggleFavorite() {
    viewModelScope.launch {
      val event = state.value.event?.toPreview()

      if (event != null) {
        val (updatedEvent, error) =
          if (event.isFavorite) repo.removeFavEvent(event)
          else repo.addFavEvent(event)

        if (error != null) {
          setState { copy(error = error.message) }
        } else {
          setState {
            copy(
              event = state
                .value
                .event?.copy(isFavorite = updatedEvent.isFavorite)
            )
          }
          sendEffect(
            EventDetailContract.Effect.ShowToast(
              if (event.isFavorite) "Event removed from favorites"
              else "Event added to favorites"
            )
          )
        }
      }
    }
  }
}