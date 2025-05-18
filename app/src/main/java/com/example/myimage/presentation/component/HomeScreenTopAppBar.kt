package com.example.myimage.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myimage.R
import com.example.myimage.domain.model.MyImage


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTopAppBar(
    title: String = "My Images",
    onSearchIconClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior,
    navigationIcon:  @Composable () -> Unit = {}
) {

    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append(title.split(" ").first())
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.secondary)) {
                        append(" ${title.split(" ").last()}")
                    }
                },
                fontWeight = FontWeight.ExtraBold,
                fontStyle = FontStyle.Italic
            )
        },
        navigationIcon = navigationIcon,

        actions = {
            IconButton(onClick = { onSearchIconClick() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullImageScreenTopAppBar(
    modifier: Modifier,
    image: MyImage?,
    onBackButtonClick: () -> Unit,
    onDownloadIconClick: () -> Unit,
    onProfileImageClick: (String) -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = Color.White,
            scrolledContainerColor = Color.Transparent
        ),
        title = {},
        actions = {
            IconButton(onClick = { onDownloadIconClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "Download"
                )
            }
        },
        navigationIcon = {
            Row {
                IconButton(onClick = { onBackButtonClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                AsyncImage(
                    model = image?.photographerImageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { image?.let { onProfileImageClick(it.photographerProfileLink) } }
                )

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .clickable { image?.let { onProfileImageClick(it.photographerProfileLink) } }
                ) {
                    Text(
                        text = image?.photographerName ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = image?.photographerUserName ?: "",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

    )
}