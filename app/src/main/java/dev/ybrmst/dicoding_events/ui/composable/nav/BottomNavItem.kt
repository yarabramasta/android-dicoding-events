package dev.ybrmst.dicoding_events.ui.composable.nav

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
  val label: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector,
)