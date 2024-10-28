package dev.ybrmst.dicodingevents.presentation.ui.composables.atoms

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MainScreenBottomNavBar(
  items: List<BottomNavItem>,
  selectedItemIndex: Int,
  onItemSelected: (Int) -> Unit,
) {
  NavigationBar {
    items.forEachIndexed { index, item ->
      NavigationBarItem(
        icon = {
          Icon(
            if (selectedItemIndex == index) item.selectedIcon else item.icon,
            contentDescription = item.label
          )
        },
        label = { Text(item.label) },
        selected = selectedItemIndex == index,
        onClick = { onItemSelected(index) }
      )
    }
  }
}

data class BottomNavItem(
  val label: String,
  val selectedIcon: ImageVector,
  val icon: ImageVector,
)