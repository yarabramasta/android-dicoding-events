package dev.ybrmst.dicoding_events.ui.composables.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.ybrmst.dicoding_events.di.eventsModule
import dev.ybrmst.dicoding_events.ui.composables.nav.BottomNavItem
import dev.ybrmst.dicoding_events.ui.composables.nav.BottomNavigationBar
import dev.ybrmst.dicoding_events.ui.theme.AppTheme
import org.koin.compose.KoinApplication

@Composable
fun MainScreen() {
  val items = listOf(
    BottomNavItem(
      title = "Home",
      route = "home",
      icon = Icons.Default.Home
    ),
    BottomNavItem(
      title = "Upcoming",
      route = "upcoming",
      icon = Icons.Default.Home
    ),
    BottomNavItem(
      title = "Finished",
      route = "past",
      icon = Icons.Default.Home
    ),
  )

  var currentRoute by remember { mutableStateOf("home") }

  Scaffold(bottomBar = {
    BottomNavigationBar(
      items = items,
      currentRoute = currentRoute,
      onItemClick = { currentRoute = it },
    )
  }) { innerPadding ->
    when (currentRoute) {
      "home" -> HomeScreen(modifier = Modifier.padding(innerPadding))
      "upcoming" -> UpcomingEventsScreen()
      "past" -> PastEventsScreen()
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
  KoinApplication(application = {
    modules(
      eventsModule
    )
  }) {
    AppTheme {
      MainScreen()
    }
  }
}