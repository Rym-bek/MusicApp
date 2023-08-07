package com.example.data.viewmodels

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.common.ServiceConnection
import com.example.data.repositories.StorageRepository
import com.example.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StorageViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val serviceConnection: ServiceConnection
) : ViewModel() {

    private val _songs: MutableLiveData<List<Song>> = MutableLiveData()
    val songs: LiveData<List<Song>> = _songs

    init {
        getAllSongs()
    }

    private suspend fun initServiceConnection() {
        //ожидание, пока плеер не инициализируется
        serviceConnection.isInitialized
            .first { it }
    }

    private fun getAllSongs() {
        viewModelScope.launch {
            _songs.value = storageRepository.getAudioData()
            setMediaItemsList(_songs.value!!)
        }
    }

    private suspend fun setMediaItemsList(songs: List<Song>) {
        val newItems: List<MediaItem> = songs.map { song ->
            val mediaMetadata = MediaMetadata.Builder()
                .setTitle(song.title)
                .setArtist(song.artist)
                .setArtworkUri(song.photoUri?.toUri())
                .build()

            MediaItem.Builder()
                .setUri(song.musicUri)
                .setMediaMetadata(mediaMetadata)
                .build()
        }
        initServiceConnection()
        serviceConnection.setMediaItemsList(newItems)
    }
}