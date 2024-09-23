package com.laioffer.spotify.ui.playlist

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.laioffer.spotify.R
import com.laioffer.spotify.model.Album
import com.laioffer.spotify.model.Song
import com.laioffer.spotify.player.PlayerUiState
import com.laioffer.spotify.player.PlayerViewModel

@Composable
fun PlaylistScreen(playlistViewModel: PlaylistViewModel, playerViewModel: PlayerViewModel) {
    val playlistUiState by playlistViewModel.uiState.collectAsState()
    val playerUiState by playerViewModel.uiState.collectAsState()

    PlaylistScreenContent(
        playlistUiState = playlistUiState,
        playerUiState = playerUiState,
        onTapFavorite = {
            Log.d("PlaylistScreen", "Tapped favorite $it")
            playlistViewModel.toggleFavorite(it)
        },
        onTapSong = {
            playerViewModel.load(it, playlistUiState.album)
            playerViewModel.play()
        }
    )
}

@Composable
private fun PlaylistScreenContent(
    playlistUiState: PlaylistUiState,
    playerUiState: PlayerUiState,
    onTapFavorite: (Boolean) -> Unit,
    onTapSong: (Song) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Cover(
            album = playlistUiState.album,
            isFavorite = playlistUiState.isFavorite,
            onTapFavorite = onTapFavorite
        )

        PlaylistHeader(playlistUiState.album)

        PlaylistContent(
            songs = playlistUiState.songs,
            currentSong = playerUiState.song,
            onTapSong = onTapSong
        )
    }
}

@Composable
private fun Cover(
    album: Album,
    isFavorite: Boolean,
    onTapFavorite: (Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.TopEnd)
                    .clickable {
                        onTapFavorite(!isFavorite)
                    },
                painter = painterResource(
                    id = if (isFavorite) {
                        R.drawable.ic_favorite_24
                    } else {
                        R.drawable.ic_unfavorite_24
                    }
                ),
                tint = if (isFavorite) {
                    Color.Green
                } else {
                    Color.Gray
                },
                contentDescription = "Favorite icon"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1.0f)
                    .align(Alignment.Center)
            ) {
                // Vinyl background
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.vinyl_background),
                    contentDescription = "Vinyl background"
                )

                // Album cover image
                AsyncImage(
                    model = album.cover,
                    contentDescription = "Album cover image",
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(1.0f)
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    contentScale = ContentScale.FillBounds
                )
            }
        }

        Text(
            text = album.description,
            modifier = Modifier.padding(top = 4.dp),
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
    }
}

@Composable
private fun PlaylistHeader(album: Album) {
    Column {
        Text(
            text = album.name,
            style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(top = 16.dp),
            color = Color.White
        )

        Text(
            text = stringResource(id = R.string.album_info, album.artists, album.year),
            style = MaterialTheme.typography.body2,
            color = Color.LightGray,
        )
    }
}

@Composable
private fun PlaylistContent(songs: List<Song>, currentSong: Song?, onTapSong: (Song) -> Unit) {
    val state = rememberLazyListState()

    LazyColumn(state = state) {
        items(songs) { song ->
            Song(song, currentSong == song, onTapSong = onTapSong)
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun Song(song: Song, isPlaying: Boolean, onTapSong: (Song) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                onTapSong(song)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.0f)) {
            Text(
                text = song.name,
                style = MaterialTheme.typography.body2,
                color = if (isPlaying) {
                    Color.Green
                } else {
                    Color.White
                },
            )

            Text(
                text = song.lyric,
                style = MaterialTheme.typography.caption,
                color = Color.Gray,
            )
        }
        Text(
            text = song.length,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.body2,
            color = Color.LightGray,
        )
    }
}