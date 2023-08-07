package com.example.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
        val id: Long?,
        val title: String?,

        val artistId: Long?,
        val artist: String?,
        val albumId: Long?,
        val album: String?,

        val trackNumber: Int?,
        val path: String?,
        val duration: Long?,

        val photoUri: String?,
        val musicUri: String?,
    ) : Parcelable




