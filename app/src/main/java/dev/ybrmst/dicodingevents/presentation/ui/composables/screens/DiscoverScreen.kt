package dev.ybrmst.dicodingevents.presentation.ui.composables.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.ybrmst.dicodingevents.AppRoute
import dev.ybrmst.dicodingevents.presentation.ui.composables.rememberFlowWithLifecycle
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.DiscoverViewModel

@Composable
fun DiscoverScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  vm: DiscoverViewModel = hiltViewModel(),
) {
//  val state by vm.state.collectAsStateWithLifecycle()
  val effect = rememberFlowWithLifecycle(vm.effect)
  val context = LocalContext.current

  LaunchedEffect(effect) {
    effect.collect { action ->
      when (action) {
        is DiscoverContract.Effect.NavigateToEventDetail -> {
          navController.navigate(AppRoute.EventDetailPage(action.eventId))
        }

        is DiscoverContract.Effect.ShowToast -> {
          Toast.makeText(
            context,
            action.message,
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  Box(modifier = modifier.fillMaxSize()) {}
}