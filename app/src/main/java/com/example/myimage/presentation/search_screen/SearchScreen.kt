package com.example.myimage.presentation.search_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.myimage.domain.model.MyImage
import com.example.myimage.presentation.component.ImageVerticalGrid
import com.example.myimage.presentation.util.SnackBarEvent
import com.example.myimage.presentation.util.searchKeywords
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    searchedImages: LazyPagingItems<MyImage>,
    snackBarEvent: Flow<SnackBarEvent>,
    snackBarHostState: SnackbarHostState,
    favouriteImageIds: List<String>,
    onSearch: (String) -> Unit,
    onBackClick: () -> Unit,
    onImageCardClick: (String) -> Unit,
    onToggle: (MyImage) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var isChipsVisible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        snackBarEvent.collect {
            snackBarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
        }
    }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged { isChipsVisible = it.isFocused },
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {
                    onSearch(searchQuery)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                },
                active = false,
                onActiveChange = {},
                content = {},
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "search"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (searchQuery == "") onBackClick()
                            else searchQuery = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "close"
                        )
                    }
                }
            )

            AnimatedVisibility(visible = isChipsVisible) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(searchKeywords) { keyword ->
                        SuggestionChip(
                            onClick = {
                                searchQuery = keyword
                                onSearch(searchQuery)
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            label = { Text(text = keyword) },
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                labelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }
            }

            ImageVerticalGrid(
                images =searchedImages,
                favouriteImageIds = favouriteImageIds,
                onCardItemClick = onImageCardClick,
                onImageDragStart = { image -> },
                onDragEnd = { },
                onToggle = onToggle
            )
        }


    }
}

