package com.example.myimage.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.memory.MemoryCache
import coil.request.ImageRequest
import com.example.myimage.domain.model.MyImage

@Composable
fun ZoomedImageCard(modifier: Modifier, image: MyImage?, isVisible: Boolean) {

    val imageRequest = ImageRequest.Builder(LocalContext.current)
        .data(image?.imageUrlRegular)
        .crossfade(true)
        .placeholderMemoryCacheKey(MemoryCache.Key(image?.imageUrlSmall ?: ""))
        .build()

    AnimatedVisibility(
        visible = isVisible,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut()
    ) {
        ElevatedCard(
            modifier = modifier
                .padding(20.dp, 50.dp, 20.dp)
                .border(5.dp, MaterialTheme.colorScheme.outline, RectangleShape)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(CircleShape)
                        .size(40.dp),
                    model = image?.photographerImageUrl,
                    contentDescription = "Photographer image",
                )

                Text(
                    text = image?.photographerName ?: "Anonymous",
                    style = MaterialTheme.typography.labelLarge
                )

            }

            AsyncImage(
                model = imageRequest,
                contentDescription = "image",
            )
        }
    }


}