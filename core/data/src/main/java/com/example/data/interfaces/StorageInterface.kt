package com.example.data.interfaces

import com.example.model.Song

interface StorageInterface {

    suspend fun getAudioData():List<Song>
}