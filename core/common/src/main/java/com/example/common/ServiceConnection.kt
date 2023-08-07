package com.example.common

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.model.Song
import com.example.player.PlaybackService
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ServiceConnection @Inject constructor(
    @ApplicationContext context: Context
) : Player.Listener
{

    private val sessionToken = SessionToken(context, ComponentName(context, PlaybackService::class.java))

    private val controller = MediaController.Builder(context, sessionToken).buildAsync()

    val mediaController: MutableStateFlow<MediaController?> = MutableStateFlow(null)

    private val player: MediaController?
        get() = mediaController.value

    private val _mediaState = MutableStateFlow<MediaState>(MediaState.Initial)
    val mediaState = _mediaState.asStateFlow()

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized.asStateFlow()


    private var job: Job? = null

    init{
        controller.addListener(
            {
                val controllerLocal = controller.get()
                mediaController.update {
                    controllerLocal
                }
                controllerLocal.addListener(this)
                if (player != null) {
                    _isInitialized.value = true
                }
            },
            MoreExecutors.directExecutor()
        )
        job = Job()
    }

    fun setMediaItemsList(mediaItemList: List<MediaItem>) {
        player?.setMediaItems(mediaItemList)
        player?.prepare()
    }

    private fun seekTo(position: Int)
    {
        player?.seekTo(position, C.TIME_UNSET)
    }

    private fun play(position: Int) {
        seekTo(position)
        if(!player?.isPlaying!!){
            player?.play()
        }
    }

    private fun resumePause() {
        if (player?.isPlaying == true) {
            player?.pause()
        } else {
            player?.play()
        }
    }

    private fun playNext() {
        if(player?.hasNextMediaItem() == true)
        {
            player?.seekToNext()
        }
        else
        {
            seekTo(0)
        }
    }

    private fun playPrevious() {
        if(player?.hasPreviousMediaItem() == true)
        {
            player?.seekToPrevious()
        }
        else
        {
            seekTo(player?.mediaItemCount?.minus(1) ?: 0)
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (
            events.contains(Player.EVENT_TRACKS_CHANGED)
        ) {
            _mediaState.value = MediaState.SongIndex(player.currentMediaItemIndex)

            val mediaMetadata = player.currentMediaItem?.mediaMetadata
            if (mediaMetadata != null) {
                val song = Song(
                    id = null,
                    title = mediaMetadata.title?.toString(),
                    artist = mediaMetadata.artist?.toString(),
                    photoUri = mediaMetadata.artworkUri.toString(),
                    musicUri = null,
                    duration = player.duration,
                    artistId = null,
                    albumId = null,
                    album = null,
                    trackNumber = null,
                    path = null,
                )
                _mediaState.value = MediaState.Song(song)
            }
            _mediaState.value =
                MediaState.Duration(player.duration)
        }
    }

    fun onPlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            PlayerEvent.Backward -> player?.seekBack()
            PlayerEvent.Forward -> player?.seekForward()
            PlayerEvent.PlayPause -> {
                resumePause()
            }
            PlayerEvent.PlayNext -> playNext()
            PlayerEvent.PlayPrevious -> playPrevious()
            PlayerEvent.Stop -> stopProgressUpdate()
            is PlayerEvent.UpdateProgress -> player?.seekTo((player?.duration!! * playerEvent.newProgress).toLong())
            is PlayerEvent.PlaySong -> {
                play(playerEvent.songIndex)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onIsPlayingChanged(isPlaying: Boolean) {
        _mediaState.value = MediaState.Playing(isPlaying = isPlaying)
        if (isPlaying) {
            GlobalScope.launch(Dispatchers.Main) {
                startProgressUpdate()
            }
        } else {
            stopProgressUpdate()
        }
    }

    private suspend fun startProgressUpdate() = job.run {
        while (true) {
            _mediaState.value = MediaState.Progress(player?.currentPosition!!)
            delay(900)
        }
    }

    private fun stopProgressUpdate() {
        job?.cancel()
    }

    sealed class PlayerEvent {
        object PlayPause : PlayerEvent()
        object PlayNext : PlayerEvent()
        object PlayPrevious : PlayerEvent()
        object Backward : PlayerEvent()
        object Forward : PlayerEvent()
        object Stop : PlayerEvent()
        data class UpdateProgress(val newProgress: Float) : PlayerEvent()
        data class PlaySong(val songIndex: Int) : PlayerEvent()
    }

    sealed class MediaState {
        object Initial : MediaState()
        data class Duration(val duration: Long) : MediaState()
        data class Progress(val progress: Long) : MediaState()
        data class Playing(val isPlaying: Boolean) : MediaState()
        data class Song(val song: com.example.model.Song) : MediaState()
        data class SongIndex(val songIndex: Int) : MediaState()
    }

}