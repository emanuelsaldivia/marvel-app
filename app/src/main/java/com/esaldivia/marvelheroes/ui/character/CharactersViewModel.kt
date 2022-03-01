package com.esaldivia.marvelheroes.ui.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.esaldivia.marvelheroes.common.Resource
import com.esaldivia.marvelheroes.data.model.character.Character
import com.esaldivia.marvelheroes.data.repository.CharacterRepository
import com.esaldivia.marvelheroes.di.CoroutineDispatcher
import com.esaldivia.marvelheroes.network.Outcome
import com.esaldivia.marvelheroes.openMarvelScope
import kotlinx.coroutines.launch
import toothpick.ktp.KTP
import toothpick.ktp.delegate.inject

class CharactersViewModel : ViewModel() {
    private val characterRepository: CharacterRepository by inject()

    init {
        KTP.openMarvelScope().inject(this)
    }

    private val _charactersListLiveData = MutableLiveData<Resource<List<Character>?>>()
    val charactersListLiveData: LiveData<Resource<List<Character>?>>
        get() = _charactersListLiveData

    fun getCharacterList() {
        viewModelScope.launch(CoroutineDispatcher.IO){
            _charactersListLiveData.postValue(Resource.Loading())
            when (val characterOutcome = characterRepository.getCharacters()) {
                is Outcome.Success -> {
                    val characterListResource = Resource.Success(characterOutcome.value)
                    _charactersListLiveData.postValue(characterListResource)
                }
                is Outcome.Error -> {
                    val error = Resource.Error<List<Character>?>(characterOutcome.exceptionDetails?.cause)
                    _charactersListLiveData.postValue(error)
                }
            }
        }
    }
}