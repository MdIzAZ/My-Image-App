package com.example.myimage.presentation.fullimage_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myimage.domain.model.MyImage
import com.example.myimage.domain.repo.Downloader
import com.example.myimage.domain.repo.ImageRepository
import com.example.myimage.presentation.util.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    savedStateHandle: SavedStateHandle,
    private val downloader: Downloader,
) : ViewModel() {

    private val imageId: String = checkNotNull(savedStateHandle["imageId"])
    private val _snackBarEvent = Channel<SnackBarEvent>()
    val snackBarEvent = _snackBarEvent.receiveAsFlow()


    var image: MyImage? by mutableStateOf(null)
        private set

    init {
        getImageById()
    }

    private fun getImageById() {
        viewModelScope.launch {
            try {
                val result = repository.getFullImageById(imageId)
                image = result
            } catch (e: Exception) {
                _snackBarEvent.send(SnackBarEvent("Failed to Load Full Image"))
            }


        }
    }

    fun downloadImage(url: String, fileName: String?) {
        viewModelScope.launch {
            viewModelScope.launch {
                try {
                    downloader.downloadFile(url, fileName)
                } catch (e: Exception) {
                    _snackBarEvent.send(SnackBarEvent( "Failed to Download Image"))
                }

            }
        }
    }

}