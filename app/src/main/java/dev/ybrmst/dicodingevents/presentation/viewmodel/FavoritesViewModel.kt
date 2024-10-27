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
      FavoritesContract.Event.OnFetch -> {
        setState { copy(isFetching = true, isRefreshing = false) }
        fetchFavEvents()
      }

      FavoritesContract.Event.OnRefresh -> {
        setState { copy(isRefreshing = true, isFetching = false) }
        fetchFavEvents()
      }

      is FavoritesContract.Event.OnEventClicked -> navigateToDetail(event.eventId)

      is FavoritesContract.Event.OnEventRemoved -> removeFavorite(event.event)

      FavoritesContract.Event.OnScreenChanged -> {
        setState {
          copy(
            isFetching = false,
            isRefreshing = false,
            error = null
          )
        }
        fetchFavEvents()
      }
    }
  }

  init {
    add(FavoritesContract.Event.OnFetch)
  }

  private fun fetchFavEvents() {
    viewModelScope.launch {
      repo.getFavEvents().collect { (events, error) ->
        if (error != null) {
          setState {
            copy(
              isFetching = false,
              isRefreshing = false,
              error = error.message
            )
          }
        } else {
          setState {
            copy(
              isFetching = false,
              isRefreshing = false,
              favorites = events
            )
          }
        }
      }
    }
  }

  private fun navigateToDetail(eventId: Int) {
    sendEffect(FavoritesContract.Effect.NavigateToDetail(eventId))
  }

  private fun removeFavorite(event: EventPreview) {
    viewModelScope.launch {
      repo.removeFavEvent(event)
      setState { copy(favorites = favorites.filter { it.id != event.id }) }
      sendEffect(FavoritesContract.Effect.ShowToast("Event removed from favorites."))
    }
  }
}