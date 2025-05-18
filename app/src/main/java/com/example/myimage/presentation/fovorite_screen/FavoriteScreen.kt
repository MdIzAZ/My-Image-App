package com.example.myimage.presentation.fovorite_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.myimage.presentation.component.HomeScreenTopAppBar
import com.example.myimage.presentation.component.ImageVerticalGrid
import com.example.myimage.presentation.component.ZoomedImageCard
import com.example.myimage.presentation.util.SnackBarEvent
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    scrollBehavior: TopAppBarScrollBehavior,
    favouriteImages: LazyPagingItems<MyImage>,
    snackBarEvent: Flow<SnackBarEvent>,
    snackBarHostState: SnackbarHostState,
    favouriteImageIds: List<String>,
    onImageCardClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onSearchIconClick: () -> Unit,
    onToggle: (MyImage) -> Unit,
) {

    var isVisible by remember { mutableStateOf(false) }
    var activeImageUrlRegular by remember { mutableStateOf<MyImage?>(null) }

    LaunchedEffect(key1 = Unit) {
        snackBarEvent.collect {
            snackBarHostState.showSnackbar(it.message, duration = it.duration)
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
                title = "Favourite Images",
                onSearchIconClick = onSearchIconClick,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )

            ImageVerticalGrid(
                images = favouriteImages,
                favouriteImageIds = favouriteImageIds,
                onCardItemClick = onImageCardClick,
                onImageDragStart = { image ->
                    activeImageUrlRegular = image
                    isVisible = true
                },
                onDragEnd = { isVisible = false },
                onToggle = onToggle
            )
        }



    }

    if (favouriteImages.itemCount == 0) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(90.dp),
                    painter = painterResource(id = R.drawable.bookmark),
                    contentDescription = "No favourite Image",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "No Favourite Images")
            }

        }
    }
    ZoomedImageCard(
        modifier = Modifier.fillMaxWidth(),
        image = activeImageUrlRegular,
        isVisible = isVisible,
    )
}