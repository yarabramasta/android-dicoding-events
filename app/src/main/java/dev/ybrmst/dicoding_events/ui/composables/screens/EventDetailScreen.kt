package dev.ybrmst.dicoding_events.ui.composables.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
  modifier: Modifier = Modifier,
  navController: NavController,
  eventId: Int,
) {
  Scaffold(
    topBar = {
      TopAppBar(
        title = {},
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(
              Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Back"
            )
          }
        },
        actions = {
          IconButton(onClick = { /*TODO*/ }) {
            Icon(
              Icons.Outlined.StarOutline,
              contentDescription = "Favorite"
            )
          }
        },
      )
    }
  ) { innerPadding ->
    Box(
      modifier = modifier.padding(innerPadding)
    ) {}
  }
}