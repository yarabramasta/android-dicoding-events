package dev.ybrmst.dicoding_events.ui.composable.nav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar(
  items: List<BottomNavItem>,
  selectedItemIndex: Int,
  onItemSelected: (Int) -> Unit,
) {
  NavigationBar {
    items.forEachIndexed { index, item ->
      NavigationBarItem(
        icon = {
          Icon(
            if (index == selectedItemIndex) item.selectedIcon else item.unselectedIcon,
            contentDescription = null,
          )
        },
        label = { Text(item.label) },
        selected = index == selectedItemIndex,
        onClick = { onItemSelected(index) }
      )
    }
  }
}