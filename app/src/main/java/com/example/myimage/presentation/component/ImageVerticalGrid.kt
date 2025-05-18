package com.example.myimage.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
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
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(200.dp),
        verticalItemSpacing = 10.dp,
        contentPadding = PaddingValues(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(images.itemCount) { index ->
            val image = images[index]
            ImageCard(
                modifier = Modifier
                    .clickable { image?.id?.let { onCardItemClick(it) } }
                    .pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = {
                                onImageDragStart(image)
                            },
                            onDragEnd = {
                                onDragEnd()
                            },
                            onDragCancel = {},
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