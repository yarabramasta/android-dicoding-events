package dev.ybrmst.dicodingevents.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import com.skydoves.sandwich.messageOrNull
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.ybrmst.dicodingevents.domain.models.EventPreview
import dev.ybrmst.dicodingevents.domain.repositories.EventsRepository
import dev.ybrmst.dicodingevents.lib.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
  private val repo: EventsRepository,
) :
  BaseViewModel<
          DiscoverContract.State,
          DiscoverContract.Event,
          DiscoverContract.Effect>
    (initialState = DiscoverContract.State.initial()) {

  private var searchJob: Job? = null

  override fun add(event: DiscoverContract.Event) {
    when (event) {
      is DiscoverContract.Event.OnFetch -> {
        updateStateForCategory(
          category = event.category,
          isFetching = true
        )
        fetchEvents(category = event.category)
      }

      is DiscoverContract.Event.OnRefresh -> {
        updateStateForCategory(
          category = event.category,
          isRefreshing = true
        )
        fetchEvents(category = event.category)
      }

      is DiscoverContract.Event.OnSearchQueryChanged -> {
        searchJob?.cancel()

        if (event.query.isBlank()) {
          updateStateForCategory(
            category = event.category,
            isSearching = false
          )
          fetchEvents(category = event.category)
        } else {
          updateStateForCategory(
            category = event.category,
            isSearching = true,
            searchQuery = event.query
          )
          searchJob = viewModelScope.launch {
            delay(500L)
            fetchEvents(event.query, event.category)
          }
        }
      }

      is DiscoverContract.Event.OnEventFavoriteChanged -> {
        sendEffect(
          DiscoverContract.Effect.ShowToast(
            if (event.isFavorite) "Event removed from favorites"
            else "Event added to favorites"
          )
        )
      }

      is DiscoverContract.Event.OnSearchQueryCleared -> {
        setState {
          when (event.category) {
            DiscoverContract.EventCategory.UPCOMING -> copy(
              searchQueryUpcoming = "",
              isSearchingUpcoming = false
            )

            DiscoverContract.EventCategory.FINISHED -> copy(
              searchQueryFinished = "",
              isSearchingFinished = false
            )
          }
        }
        fetchEvents(category = event.category)
      }
    }
  }

  init {
    viewModelScope.launch {
      fetchEvents(category = DiscoverContract.EventCategory.UPCOMING)
      fetchEvents(category = DiscoverContract.EventCategory.FINISHED)
    }
  }

  private fun fetchEvents(
    searchQuery: String? = null,
    category: DiscoverContract.EventCategory,
  ) {
    viewModelScope.launch {
      repo.queryEvents(
        query = searchQuery,
        active = when (category) {
          DiscoverContract.EventCategory.UPCOMING -> 1
          DiscoverContract.EventCategory.FINISHED -> 0
        }
      ).collect {
        it
          .onSuccess {
            updateStateForCategory(
              category = category,
              isFetching = false,
              isRefreshing = false,
              isSearching = false,
              events = data
            )
          }
          .onFailure {
            updateStateForCategory(
              category = category,
              error = messageOrNull
                ?: "Uh oh! Unexpected error occurred.\nPlease try again."
            )
          }
      }
    }
  }


  private fun updateStateForCategory(
    category: DiscoverContract.EventCategory,
    isFetching: Boolean? = null,
    isRefreshing: Boolean? = null,
    isSearching: Boolean? = null,
    searchQuery: String? = null,
    events: List<EventPreview>? = null,
    error: String? = null,
  ) {
    setState {
      when (category) {
        DiscoverContract.EventCategory.UPCOMING -> copy(
          isFetchingUpcoming = isFetching ?: this.isFetchingUpcoming,
          isRefreshingUpcoming = isRefreshing ?: this.isRefreshingUpcoming,
          isSearchingUpcoming = isSearching ?: this.isSearchingUpcoming,
          searchQueryUpcoming = searchQuery ?: this.searchQueryUpcoming,
          upcomingEvents = events ?: this.upcomingEvents,
          errorUpcoming = error
        )

        DiscoverContract.EventCategory.FINISHED -> copy(
          isFetchingFinished = isFetching ?: this.isFetchingFinished,
          isRefreshingFinished = isRefreshing ?: this.isRefreshingFinished,
          isSearchingFinished = isSearching ?: this.isSearchingFinished,
          searchQueryFinished = searchQuery ?: this.searchQueryFinished,
          finishedEvents = events ?: this.finishedEvents,
          errorFinished = error
        )
      }
    }
  }
}