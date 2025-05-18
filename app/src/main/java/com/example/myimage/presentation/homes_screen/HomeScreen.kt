package com.example.myimage.presentation.homes_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.myimage.R
import com.example.myimage.domain.model.MyImage
import com.example.myimage.domain.model.NetworkStatus
import com.example.myimage.domain.repo.NetworkConnectivityObserver
import com.example.myimage.presentation.component.HomeScreenTopAppBar
import com.example.myimage.presentation.component.ImageVerticalGrid
import com.example.myimage.presentation.component.ZoomedImageCard
import com.example.myimage.presentation.util.SnackBarEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onRefresh: ()->Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    images: LazyPagingItems<MyImage>,
    favouriteImageIds: List<String>,
    snackBarEvent: Flow<SnackBarEvent>,
    snackBarHostState: SnackbarHostState,
    connectivityObserver: NetworkConnectivityObserver,
    onCardItemClick: (String) -> Unit,
    onSearchIconClick: () -> Unit,
    onSaveIconClick: () -> Unit,
    onToggle: (MyImage) -> Unit,
) {


    var isVisible by remember { mutableStateOf(false) }
    var activeImageUrlRegular by remember { mutableStateOf<MyImage?>(null) }
    val status by connectivityObserver.networkStatus.collectAsState()

    LaunchedEffect(key1 = Unit) {
        snackBarEvent.collect {
            snackBarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
        }
    }

    LaunchedEffect(key1 = status) {
        when (status) {
            NetworkStatus.CONNECTED -> {
                onRefresh()
            }

            NetworkStatus.DISCONNECTED -> {}
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .blur(
                if (isVisible) 10.dp else 0.dp
            )
    ) {


        Column {
            HomeScreenTopAppBar(
                onSearchIconClick = { onSearchIconClick() },
                scrollBehavior = scrollBehavior
            )

            ImageVerticalGrid(
                images = images,
                favouriteImageIds = favouriteImageIds,
                onCardItemClick = onCardItemClick,
                onImageDragStart = { image ->
                    activeImageUrlRegular = image
                    isVisible = true
                },
                onDragEnd = { isVisible = false },
                onToggle = onToggle
            )
        }

        FloatingActionButton(
            onClick = { onSaveIconClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.save), contentDescription = "save image")
        }


    }

    ZoomedImageCard(
        modifier = Modifier.fillMaxWidth(),
        image = activeImageUrlRegular,
        isVisible = isVisible,
    )

}