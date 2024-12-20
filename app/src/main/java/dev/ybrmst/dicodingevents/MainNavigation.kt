package dev.ybrmst.dicodingevents

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import dev.ybrmst.dicodingevents.presentation.ui.composables.scopedViewModel
import dev.ybrmst.dicodingevents.presentation.ui.composables.screens.EventDetailScreen
import dev.ybrmst.dicodingevents.presentation.ui.composables.screens.MainScreen
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesContract
import dev.ybrmst.dicodingevents.presentation.viewmodel.FavoritesViewModel
import kotlinx.serialization.Serializable

@Composable
fun MainNavigation() {
  val navController = rememberNavController()

  NavHost(
    navController = navController,
    startDestination = AppRoute.IndexPage
  ) {
    navigation<AppRoute.IndexPage>(startDestination = AppRoute.MainPage) {
      composable<AppRoute.MainPage> { navBackStackEntry ->
        MainScreen(
          favsVm = navBackStackEntry.scopedViewModel(navController),
          onEventClick = {
            navController.navigate(AppRoute.EventDetailPage(it.id))
          },
        )
      }

      composable<AppRoute.EventDetailPage>(
        enterTransition = { scaleIntoContainer() },
        exitTransition = { scaleOutOfContainer(ScaleTransition.INWARDS) },
        popEnterTransition = { scaleIntoContainer(ScaleTransition.OUTWARDS) },
        popExitTransition = { scaleOutOfContainer() }
      ) {
        val eventId = it.toRoute<AppRoute.EventDetailPage>().eventId
        val favsVm: FavoritesViewModel = it.scopedViewModel(navController)

        EventDetailScreen(
          eventId = eventId,
          onPop = {
            navController.popBackStack()
            favsVm.add(FavoritesContract.Event.OnFetch)
          },
        )
      }
    }
  }
}

sealed class AppRoute {
  @Serializable
  data object IndexPage : AppRoute()

  @Serializable
  data object MainPage : AppRoute()

  @Serializable
  data class EventDetailPage(val eventId: Int) : AppRoute()
}

internal fun scaleIntoContainer(
  direction: ScaleTransition = ScaleTransition.INWARDS,
  initialScale: Float = if (direction == ScaleTransition.INWARDS) 1.1f else 0.9f,
): EnterTransition {
  return scaleIn(
    animationSpec = tween(250, 90),
    initialScale = initialScale
  ) + fadeIn(animationSpec = tween(250, 90))
}

internal fun scaleOutOfContainer(
  direction: ScaleTransition = ScaleTransition.OUTWARDS,
  targetScale: Float = if (direction == ScaleTransition.OUTWARDS) 1.1f else 0.9f,
): ExitTransition {
  return scaleOut(
    animationSpec = tween(250, 90),
    targetScale = targetScale
  ) + fadeOut(animationSpec = tween(delayMillis = 90))
}

internal enum class ScaleTransition {
  INWARDS, OUTWARDS
}