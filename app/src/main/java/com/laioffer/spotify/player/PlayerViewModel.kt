package com.laioffer.spotify.player

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.laioffer.spotify.model.Album
import com.laioffer.spotify.model.Song
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val exoPlayer: ExoPlayer
) : ViewModel(), Player.Listener {

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()

    init {
        exoPlayer.addListener(this)

        viewModelScope.launch {
            flow {
                while (true) {
                    if (exoPlayer.isPlaying) {
                        emit(exoPlayer.currentPosition to exoPlayer.duration)
                    }
                    delay(1000)
                }
            }.collect {
                _uiState.value = _uiState.value.copy(currentMs = it.first, durationMs = it.second)
                Log.d("SpotifyPlayer", "Current ms: ${it.first}, Duration ms: ${it.second}")
            }
        }
    }

    fun load(song: Song, album: Album) {
        _uiState.value = PlayerUiState(album = album, song = song, isPlaying = false)
        val mediaItem = MediaItem.Builder().setUri(song.src).build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
    }

    fun play() {
        exoPlayer.play()
    }

    fun pause() {
        exoPlayer.pause()
    }

    fun seekTo(positionMs: Long) {
        _uiState.value = _uiState.value.copy(currentMs = positionMs)
        exoPlayer.seekTo(positionMs)
    }

    override fun onCleared() {
        exoPlayer.removeListener(this)
        super.onCleared()
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        Log.d("SpotifyPlayer", isPlaying.toString())
        _uiState.value = _uiState.value.copy(isPlaying = isPlaying)
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Log.d("SpotifyPlayer", error.toString())
    }

}

data class PlayerUiState(
    val album: Album? = null,
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val currentMs: Long = 0,
    val durationMs: Long = 0
)