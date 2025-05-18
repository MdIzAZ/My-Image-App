package com.example.myimage.presentation.fullimage_screen

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.myimage.R
import com.example.myimage.domain.model.MyImage
import com.example.myimage.presentation.component.DownloadOptions
import com.example.myimage.presentation.component.DownloadQualityOptionBottomSheet
import com.example.myimage.presentation.component.FullImageScreenTopAppBar
import com.example.myimage.presentation.util.SnackBarEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.math.max

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullImageScreen(
    image: MyImage?,
    snackBarEvent: Flow<SnackBarEvent>,
    snackBarHostState: SnackbarHostState,
    onProfileImageClick: (String) -> Unit,
    onBackButtonClick: () -> Unit,
    onDownloadOptionClick: (String, String?) -> Unit,
) {


    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState()
    var isBottomSheetOpened by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = true) {
        snackBarEvent.collect {
            snackBarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
        }
    }


    BackHandler(enabled = true) { onBackButtonClick() }

    DownloadQualityOptionBottomSheet(
        modifier = Modifier,
        sheetState = sheetState,
        onDismissRequest = { isBottomSheetOpened = false },
        isOpen = isBottomSheetOpened,
        onOptionClick = {
            scope.launch { sheetState.hide() }.invokeOnCompletion {
                if (!sheetState.isVisible) isBottomSheetOpened = false
            }
            val url = when (it) {
                DownloadOptions.SMALL -> image?.imageUrlSmall
                DownloadOptions.MEDIUM -> image?.imageUrlRegular
                DownloadOptions.LARGE -> image?.imageUrlRaw
            }

            url?.let {
                onDownloadOptionClick(url, image?.description?.take(20))
                Toast.makeText(context, "Download Started", Toast.LENGTH_LONG).show()
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        var isLoading by remember { mutableStateOf(true) }
        var isError by remember { mutableStateOf(false) }
        var scale by remember { mutableStateOf(1f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val isZoomed by remember { derivedStateOf { scale != 1f } }

        BoxWithConstraints {
            val maxX = (constraints.maxWidth * (scale - 1)) / 2
            val maxY = (constraints.maxHeight * (scale - 1)) / 2

            val transformState = rememberTransformableState { zoomChange, panChange, _ ->
                scale = max(1f, zoomChange * scale)
                offset = Offset(
                    (offset.x + panChange.x).coerceIn(-maxX, maxX),
                    (offset.y + panChange.y).coerceIn(-maxY, maxY)
                )
            }


            val imageLoader = rememberAsyncImagePainter(
                model = image?.imageUrlRaw,
                onState = {
                    isLoading = it is AsyncImagePainter.State.Loading
                    isError = it is AsyncImagePainter.State.Error
                }
            )

            if (isLoading && isError.not()) CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )

            Image(
                painter = if (isError.not()) imageLoader else painterResource(id = R.drawable.error) ,
                contentDescription = "Image",
                modifier = Modifier
                    .transformable(transformState)
                    .combinedClickable(
                        onClick = {
//                            showBars = !showBars
//                            windowInsetsController.toggleStatusBar(showBars)
                        },
                        onDoubleClick = {
                            if (isZoomed) {
                                offset = Offset.Zero
                                scale = 1f
                            } else {
                                scope.launch {
                                    transformState.animateZoomBy(3f)
                                }
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
            )
        }

        FullImageScreenTopAppBar(
            modifier = Modifier.align(Alignment.TopCenter),
            image = image,
            onBackButtonClick = onBackButtonClick,
            onDownloadIconClick = { isBottomSheetOpened = true },
            onProfileImageClick = onProfileImageClick
        )


    }
}
//    var showBars by rememberSaveable { mutableStateOf(false) }
//    val windowInsetsController = rememberWindowInsetsController()
//    LaunchedEffect(key1 = Unit) { windowInsetsController.toggleStatusBar(showBars) }
