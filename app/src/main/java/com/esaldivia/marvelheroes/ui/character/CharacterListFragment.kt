package com.esaldivia.marvelheroes.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        charactersViewModel.getCharacterList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setOnRefreshListener {
            charactersViewModel.getCharacterList()
            binding.swipeRefresh.isRefreshing = false
        }
        charactersViewModel.charactersListLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.characterRecyclerView.visibility = View.VISIBLE
                    characterAdapter = resource.value?.let { CharacterAdapter(it, this::onClick) }
                    binding.characterRecyclerView.adapter = characterAdapter
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.characterRecyclerView.visibility = View.GONE
                    Toast.makeText(context, resource.errorMessage, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.characterRecyclerView.visibility = View.GONE
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