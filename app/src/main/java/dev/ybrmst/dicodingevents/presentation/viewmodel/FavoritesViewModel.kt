package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
  private val repo: EventsRepository,
) : BaseViewModel<
        FavoritesContract.State,
        FavoritesContract.Event,
        FavoritesContract.Effect>
  (initialState = FavoritesContract.State.initial()) {

  override fun add(event: FavoritesContract.Event) {
    when (event) {
      FavoritesContract.Event.OnFetch -> fetchFavEvents()
      is FavoritesContract.Event.OnEventAdded -> addFavorite(event.event)
      is FavoritesContract.Event.OnEventRemoved -> removeFavorite(event.event)
    }
  }

  init {
    add(FavoritesContract.Event.OnFetch)
  }

  private fun fetchFavEvents() {
    viewModelScope.launch {
      repo.getFavEvents().collect { (events, error) ->
        if (error != null) setState { copy(error = error.message) }
        else setState { copy(events = events) }
      }
    }
  }

  private fun addFavorite(event: EventPreview) {
    viewModelScope.launch {
      val (updatedEvent, error) = repo.addFavEvent(event)

      if (error != null) {
        setState { copy(error = "Failed to add event to favorites.") }
      } else {
        setState { copy(events = events + updatedEvent) }
        sendEffect(FavoritesContract.Effect.ShowToast("Event added to favorites."))
      }
    }
  }

  private fun removeFavorite(event: EventPreview) {
    viewModelScope.launch {
      val (updatedEvent, error) = repo.removeFavEvent(event)

      if (error != null) {
        setState {
          copy(error = "Failed to remove event from favorites.")
        }
      } else {
        setState {
          copy(
            events = events
              .map {
                if (it.id == updatedEvent.id) updatedEvent
                else it
              }
              .filter { it.isFavorite }
          )
        }
        sendEffect(FavoritesContract.Effect.ShowToast("Event removed from favorites."))
      }
    }
  }
}