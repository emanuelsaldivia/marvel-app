package com.esaldivia.marvelheroes.ui.character

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.esaldivia.marvelheroes.R
import com.esaldivia.marvelheroes.common.Constants
import com.esaldivia.marvelheroes.common.Resource
import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.databinding.FragmentCharacterDetailBinding
import com.esaldivia.marvelheroes.openMarvelScope
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject
import toothpick.smoothie.lifecycle.closeOnDestroy
import toothpick.smoothie.viewmodel.installViewModelBinding

class CharacterDetailFragment : Fragment() {
    private var _binding: FragmentCharacterDetailBinding? = null
    private val binding get() = _binding!!

    private val charactersViewModel: CharactersViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterDetailBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        KTP.openMarvelScope()
            .openSubScope(CharacterDetailFragment::class)
            .installViewModelBinding<CharactersViewModel>(this)
            .closeOnDestroy(this)
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val character = arguments?.getParcelable<Character>(Constants.CHARACTER_KEY)

        charactersViewModel.characterLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.characterDetailView.visibility = View.VISIBLE
                    binding.detailsNameTv.text = it.value?.name
                    val description = if (!it.value?.description.isNullOrEmpty()) {
                        it.value?.description
                    } else{
                        context?.getText(R.string.description_not_available)
                    }
                    binding.detailsDescriptionValueTv.text = description

                    val uri =
                        it.value?.thumbnailUrl?.toUri()?.buildUpon()?.scheme("https")?.build() ?: ""

                    context?.let { context ->
                        Glide.with(context)
                            .load(uri)
                            .error(R.drawable.marvel_logo)
                            .into(binding.detailsCharacterPoster)
                    }
                }
                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.characterDetailView.visibility = View.GONE
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_LONG).show()

                }
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.characterDetailView.visibility = View.GONE
                }
            }
        }

        character?.id?.let { charactersViewModel.getCharacter(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}