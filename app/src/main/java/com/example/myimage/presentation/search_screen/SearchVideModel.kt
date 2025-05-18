package com.example.myimage.presentation.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myimage.domain.model.MyImage
import com.example.myimage.domain.repo.ImageRepository
import com.example.myimage.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchVideModel @Inject constructor(
    private val repository: ImageRepository,
) : ViewModel() {

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()

    private val _searchImages = MutableStateFlow<PagingData<MyImage>>(PagingData.empty())
    val searchImages = _searchImages


    val favouriteImageIds: StateFlow<List<String>> =
        repository.getAllFavouriteImageIds()
            .catch {
                _snackBarEvent.send(SnackBarEvent("Failed to Load Favourite Images"))
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )


    fun searchImages(query: String) {
        viewModelScope.launch {
            try {
                repository
                    .searchImages(query)
                    .cachedIn(viewModelScope)
                    .collect {
                        _searchImages.value = it
                    }
            } catch (e: Exception) {
                _snackBarEvent.send(SnackBarEvent("You are Offline \n Unable to Load images"))
            }
        }
    }

    fun toggleFavourite(image: MyImage) {
        viewModelScope.launch {
            try {
                val isMarkedAsFavourite = repository.toggleFavourite(image)
                if (isMarkedAsFavourite) _snackBarEvent.send(SnackBarEvent("Marked as Favourite"))
                else _snackBarEvent.send(SnackBarEvent("Removed from Favourite"))

            } catch (e: Exception) {
                _snackBarEvent.send(SnackBarEvent("Failed to Toggle Favourite status"))
            }
        }
    }


}