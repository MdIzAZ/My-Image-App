package com.example.myimage.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.myimage.domain.model.MyImage

@Composable
fun ImageVerticalGrid(
    images: LazyPagingItems<MyImage>,
    favouriteImageIds: List<String>,
    onCardItemClick: (String) -> Unit,
    onImageDragStart: (MyImage?) -> Unit,
    onDragEnd: () -> Unit,
    onToggle: (MyImage) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (images.loadState.refresh is LoadState.Loading && images.itemCount == 0) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (images.loadState.refresh is LoadState.Error && images.itemCount == 0) {
            val error = (images.loadState.refresh as LoadState.Error).error
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Failed to load images: ${error.localizedMessage ?: "Unknown error"}")
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { images.retry() }) {
                    Text(text = "Retry")
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 10.dp,
                contentPadding = PaddingValues(10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    count = images.itemCount,
                    key = { index -> images[index]?.id ?: index }
                ) { index ->
                    val image = images[index]
                    ImageCard(
                        modifier = Modifier
                            .clickable { image?.id?.let { onCardItemClick(it) } }
                            .pointerInput(image?.id) {
                                detectDragGesturesAfterLongPress(
                                    onDragStart = { onImageDragStart(image) },
                                    onDragEnd = { onDragEnd() },
                                    onDragCancel = { onDragEnd() },
                                    onDrag = { _, _ -> }
                                )
                            },
                        image = image,
                        isFavourite = image?.id in favouriteImageIds,
                        onToggle = { image?.let { onToggle(it) } }
                    )
                }
            }
        }
    }
}