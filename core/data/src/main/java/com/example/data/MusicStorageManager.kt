package com.example.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.WorkerThread
import com.example.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class MusicStorageManager  @Inject constructor(
    @ApplicationContext val context: Context){

    private var cursor: Cursor? = null

    private val projection = arrayOf(
        MediaStore.Audio.Media._ID,
        MediaStore.Audio.Media.TITLE,
        MediaStore.Audio.Media.ALBUM_ID,
        MediaStore.Audio.Media.ALBUM,
        MediaStore.Audio.Media.ARTIST_ID,
        MediaStore.Audio.Media.ARTIST,
        MediaStore.Audio.Media.DURATION,
        MediaStore.Audio.Media.TRACK,
        MediaStore.Audio.Media.DATA,
    )

    private val selection = "${MediaStore.Audio.Media.IS_MUSIC} = ?"

    private val selectionArgs = arrayOf("1")

    private val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} ASC"

    private val albumArtUri = Uri.parse("content://media/external/audio/albumart")

    @WorkerThread
    fun getAudioData(): List<Song>{
        return getAllSongsFromCursor()
    }

    private fun getAllSongsFromCursor(): List<Song> {
        val songs = mutableListOf<Song>()

        cursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            arrayOf("1"),
            null
        )

        cursor?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)
            val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val artistIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
            val trackNumberColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            while(cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val album = cursor.getString(albumColumn)
                val artistId = cursor.getLong(artistIdColumn)
                val artist = cursor.getString(artistColumn)
                val duration = cursor.getLong(durationColumn)
                val track = cursor.getInt(trackNumberColumn)
                val path = cursor.getString(dataColumn)
                val musicUri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id).toString()
                val albumName = if (album == MediaStore.UNKNOWN_STRING) "Unknown Album" else album
                val artistName = if (artist == MediaStore.UNKNOWN_STRING) "Unknown Artist" else artist
                val photoUri = ContentUris.withAppendedId(albumArtUri, albumId).toString()
                val trackNumber = if (track > 1000) track % 1000 else track

                songs.add(
                    Song(id = id,
                        title = title,
                        artistId = artistId,
                        artist = artistName,
                        albumId = albumId,
                        album = albumName,
                        trackNumber = trackNumber,
                        path = path,
                        duration = duration,
                        photoUri = photoUri,
                        musicUri = musicUri,
                        ))
            }
        }
        return songs
    }
}