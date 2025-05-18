package com.example.myimage.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NetworkStatusBar(
    modifier: Modifier,
    message: String,
    backgroundColor: Color,
    isVisible: Boolean,
) {

    AnimatedVisibility(
        modifier = modifier.height(86.dp),
        visible = isVisible,
        enter = slideInVertically(animationSpec = tween(600)) { h -> h },
        exit = slideOutVertically(animationSpec = tween(600)) { h -> h }
    ) {
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(10.dp)
            ) {

                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Spacer(modifier = Modifier)
        }

    }

}