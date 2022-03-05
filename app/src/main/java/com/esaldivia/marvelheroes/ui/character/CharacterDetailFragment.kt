package com.esaldivia.marvelheroes.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.esaldivia.marvelheroes.common.Constants
import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.databinding.FragmentCharacterDetailBinding

class CharacterDetailFragment : Fragment() {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val character = arguments?.getParcelable<Character>(Constants.CHARACTER_KEY)
        character?.id
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}