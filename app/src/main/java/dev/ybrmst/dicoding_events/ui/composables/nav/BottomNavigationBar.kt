package dev.ybrmst.dicoding_events.ui.composables.nav

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
  items: List<BottomNavItem>,
  onItemClick: (String) -> Unit,
  currentRoute: String,
) {
  Text("Bottom Navigation Bar")
}