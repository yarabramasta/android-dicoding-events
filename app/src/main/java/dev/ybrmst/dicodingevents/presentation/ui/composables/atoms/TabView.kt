package dev.ybrmst.dicodingevents.presentation.ui.composables.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.ybrmst.dicodingevents.presentation.ui.theme.AppTheme
import kotlinx.coroutines.launch

@Composable
fun TabView(
  modifier: Modifier = Modifier,
  items: List<TabItem>,
  content: @Composable (Int) -> Unit,
) {
  val coroutineScope = rememberCoroutineScope()
  val pagerState = rememberPagerState { items.size }

  Column(modifier = modifier.fillMaxSize()) {
    TabRow(
      selectedTabIndex = pagerState.currentPage,
      modifier = Modifier.fillMaxWidth()
    ) {
      items.forEachIndexed { index, item ->
        Tab(
          modifier = Modifier.fillMaxWidth(),
          selected = pagerState.currentPage == index,
          onClick = {
            coroutineScope.launch {
              pagerState.animateScrollToPage(index)
            }
          },
          text = { Text(item.title) },
          icon = {
            Icon(
              if (pagerState.currentPage == index) item.selectedIcon
              else item.icon,
              contentDescription = item.title
            )
          },
          selectedContentColor = MaterialTheme.colorScheme.primary,
          unselectedContentColor = MaterialTheme.colorScheme.outline,
        )
      }
    }
    HorizontalPager(
      state = pagerState,
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp)
          .padding(bottom = 16.dp)
      ) {
        content(pagerState.currentPage)
      }
    }
  }
}

data class TabItem(
  val title: String,
  val selectedIcon: ImageVector,
  val icon: ImageVector,
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TabViewPreview() {
  val tabItems = listOf(
    TabItem(
      title = "Upcoming",
      icon = Icons.Outlined.EventAvailable,
      selectedIcon = Icons.Filled.EventAvailable
    ),
    TabItem(
      title = "Finished",
      icon = Icons.Outlined.EventBusy,
      selectedIcon = Icons.Filled.EventBusy
    ),
  )

  AppTheme {
    Scaffold {
      TabView(
        modifier = Modifier.padding(it),
        items = tabItems
      ) { selectedIndex ->
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          item {
            Text("Current Page: ${tabItems[selectedIndex].title}")
          }
        }
      }
    }
  }
}