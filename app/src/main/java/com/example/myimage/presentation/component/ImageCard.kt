package com.example.myimage.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myimage.domain.model.MyImage

@Composable
fun ImageCard(
    modifier: Modifier,
    image: MyImage?,
    isFavourite: Boolean,
    onToggle: () -> Unit,
) {

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(image?.imageUrlSmall)
        .crossfade(true)
        .build()

    val aspectRatio by remember {
        derivedStateOf { (image?.width?.toFloat() ?: 1f) / (image?.height?.toFloat() ?: 1f) }
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .then(modifier),
        shape = RoundedCornerShape(10.dp),
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize(),
            )
            FavouriteButton(
                modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
                isFavourite = isFavourite,
                onToggle = {onToggle()}
            )
        }


    }

}


@Composable
fun FavouriteButton(
    modifier: Modifier,
    isFavourite: Boolean,
    onToggle: () -> Unit,
) {
    FilledIconToggleButton(
        modifier = modifier,
        checked = isFavourite,
        onCheckedChange = { onToggle() },
        colors = IconButtonDefaults.iconToggleButtonColors(containerColor = Color.Transparent)
    ) {

        if (isFavourite) {
            Icon(Icons.Default.Favorite, contentDescription = "Favourite")
        } else {
            Icon(Icons.Default.FavoriteBorder, contentDescription = "Not Favourite")
        }

    }
}