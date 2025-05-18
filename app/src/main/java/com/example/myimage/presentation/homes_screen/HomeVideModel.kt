package com.example.myimage.presentation.homes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myimage.domain.model.MyImage
import com.example.myimage.domain.repo.ImageRepository
import com.example.myimage.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVideModel @Inject constructor(
    private val repository: ImageRepository,
) : ViewModel() {

    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()


    var homeImages: StateFlow<PagingData<MyImage>> =
        repository.getEditorialImages()
            .catch {
                _snackBarEvent.send(SnackBarEvent("Failed to Load Unsplash Images"))
            }
            .cachedIn(viewModelScope)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                PagingData.empty()
            )


    val favouriteImageIds: StateFlow<List<String>> = repository.getAllFavouriteImageIds()
        .catch {
            _snackBarEvent.send(SnackBarEvent("Failed to get favourite images ids"))
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )


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