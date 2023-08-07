package com.example.data.repositories

import com.example.data.MusicStorageManager
import com.example.data.interfaces.StorageInterface
import com.example.model.Song
import javax.inject.Inject

class StorageRepository @Inject constructor(
    private val musicStorageManager: MusicStorageManager
):StorageInterface {
    override suspend fun getAudioData(): List<Song> {
        return musicStorageManager.getAudioData()
    }
}