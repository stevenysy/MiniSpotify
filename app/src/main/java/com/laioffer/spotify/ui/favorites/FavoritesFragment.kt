package com.laioffer.spotify.ui.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.laioffer.spotify.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_favorites, container, false)

        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme(colors = darkColors()) {
                    FavoritesScreen(viewModel = viewModel, onTap = {
                        val direction =
                            FavoritesFragmentDirections.actionFavoritesFragmentToPlaylistFragment(it)
                        findNavController().navigate(direction)
                    })
                }
            }
        }
    }

}