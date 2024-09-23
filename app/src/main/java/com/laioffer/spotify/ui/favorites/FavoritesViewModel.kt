package com.laioffer.spotify.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laioffer.spotify.model.Album
import com.laioffer.spotify.repository.FavoriteAlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoriteAlbumRepository: FavoriteAlbumRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState(emptyList()))
    val uiState: StateFlow<FavoritesUiState> = _uiState

    init {
        viewModelScope.launch {
            favoriteAlbumRepository.fetchFavoriteAlbums().collect {
                _uiState.value = FavoritesUiState(it)
            }
        }
    }

}

data class FavoritesUiState(val albums: List<Album>)