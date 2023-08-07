package com.example.common.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.example.common.ServiceConnection
import com.example.common.ServiceConnection.MediaState
import com.example.common.ServiceConnection.PlayerEvent
import com.example.common.utils.FormatTime
import com.example.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(SavedStateHandleSaveableApi::class)
@HiltViewModel
class MediaViewModel @Inject constructor(
    private val serviceConnection: ServiceConnection,
    savedStateHandle: SavedStateHandle
):ViewModel() {

    var duration by savedStateHandle.saveable { mutableStateOf(0L) }
    var progress by savedStateHandle.saveable { mutableStateOf(0f) }
    var progressString by savedStateHandle.saveable { mutableStateOf("00:00") }
    var isPlaying by savedStateHandle.saveable { mutableStateOf(false) }
    var currentSong by savedStateHandle.saveable { mutableStateOf(
        Song(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
    ) }
    var songIndex by savedStateHandle.saveable { mutableStateOf(0) }
    private val _uiState = MutableStateFlow<UIState>(UIState.Initial)
    val uiState = _uiState.asStateFlow()

    init{
        viewModelScope.launch {
            serviceConnection.mediaState.collect { mediaState ->
                when (mediaState) {
                    MediaState.Initial -> _uiState.value = UIState.Initial
                    is MediaState.Playing -> isPlaying = mediaState.isPlaying
                    is MediaState.Progress -> calculateProgressValues(mediaState.progress)
                    is MediaState.Duration -> {
                        duration = mediaState.duration
                        _uiState.value = UIState.Ready
                    }
                    is MediaState.Song -> currentSong = mediaState.song
                    is MediaState.SongIndex -> songIndex = mediaState.songIndex
                    else -> {}
                }
            }
        }
    }

    fun onUIEvent(uiEvent: UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            UIEvent.Backward -> serviceConnection.onPlayerEvent(PlayerEvent.Backward)
            UIEvent.Forward -> serviceConnection.onPlayerEvent(PlayerEvent.Forward)
            UIEvent.PlayPrevious -> serviceConnection.onPlayerEvent(PlayerEvent.PlayPrevious)
            UIEvent.PlayNext -> serviceConnection.onPlayerEvent(PlayerEvent.PlayNext)
            UIEvent.PlayPause -> serviceConnection.onPlayerEvent(PlayerEvent.PlayPause)

            is UIEvent.PlaySong -> {
                songIndex = uiEvent.songIndex
                serviceConnection.onPlayerEvent(
                    PlayerEvent.PlaySong(
                        uiEvent.songIndex
                    )
                )
            }

            is UIEvent.UpdateProgress -> {
                progress = uiEvent.newProgress
                serviceConnection.onPlayerEvent(
                    PlayerEvent.UpdateProgress(
                        uiEvent.newProgress
                    )
                )
            }

            else -> {}
        }
    }

    override fun onCleared() {
        viewModelScope.launch {
            serviceConnection.onPlayerEvent(PlayerEvent.Stop)
        }
    }

    private fun calculateProgressValues(currentProgress: Long) {
        progress = if (currentProgress > 0) (currentProgress.toFloat() / duration) else 0f
        progressString = FormatTime.formatDuration(currentProgress)
    }

    sealed class UIEvent {
        object PlayPause : UIEvent()
        object Backward : UIEvent()
        object Forward : UIEvent()
        object PlayNext : UIEvent()
        object PlayPrevious : UIEvent()
        data class UpdateProgress(val newProgress: Float) : UIEvent()
        data class PlaySong(val songIndex: Int)  : UIEvent()
    }

    sealed class UIState {
        data object Initial : UIState()
        data object Ready : UIState()
    }

}