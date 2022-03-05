package com.esaldivia.marvelheroes.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.esaldivia.marvelheroes.R
import com.esaldivia.marvelheroes.common.Constants
import com.esaldivia.marvelheroes.common.Resource
import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.databinding.FragmentCharacterListBinding
import com.esaldivia.marvelheroes.openMarvelScope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterListFragment : Fragment() {

    private var _binding: FragmentCharacterListBinding? = null
    private val binding get() = _binding!!

    private val charactersViewModel: CharactersViewModel by inject()

    private var characterAdapter: CharacterAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KTP.openMarvelScope()
            .installViewModelBinding<CharactersViewModel>(this)
            .closeOnDestroy(this)
            .inject(this)
    }

    override fun onResume() {
        super.onResume()

        charactersViewModel.getCharacterList()

        charactersViewModel.charactersListLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Success -> {
                    characterAdapter = resource.value?.let { CharacterAdapter(it, this::onClick) }
                    binding.characterRecyclerView.adapter = characterAdapter
                }
                is Resource.Error -> {
                    resource.errorMessage
                }
                is Resource.Loading -> {
                    // TODO
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onClick(character: Character) {
        val bundle = bundleOf(Constants.CHARACTER_KEY to character)
        findNavController().navigate(R.id.characterDetailFragment, bundle)
    }
}