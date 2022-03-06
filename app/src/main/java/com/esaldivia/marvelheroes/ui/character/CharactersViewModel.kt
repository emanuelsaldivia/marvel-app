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
import com.esaldivia.marvelheroes.usecase.SecurityUseCase
import kotlinx.coroutines.launch
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module
import toothpick.ktp.delegate.inject

class CharactersViewModel : ViewModel() {
    private val characterRepository: CharacterRepository by inject()
    private val _charactersListLiveData = MutableLiveData<Resource<List<Character>?>>()
    val charactersListLiveData: LiveData<Resource<List<Character>?>>
        get() = _charactersListLiveData
    private val _characterLiveData: MutableLiveData<Resource<Character?>> = MutableLiveData()
    val characterLiveData: LiveData<Resource<Character?>>
        get() = _characterLiveData
    val securityUseCase: SecurityUseCase by inject()

    init {
        KTP.openMarvelScope()
            .installModules(module {
                bind<SecurityUseCase>().toInstance(SecurityUseCase())
            })
            .inject(this)
    }

    fun getCharacterList() {
        viewModelScope.launch(CoroutineDispatcher.IO) {
            _charactersListLiveData.postValue(Resource.Loading())
            val timeStamp = System.currentTimeMillis()
            val hash = securityUseCase.md5Encryption(timeStamp)
            when (val characterOutcome = characterRepository.getCharacters(timeStamp = timeStamp, hash = hash)) {
                is Outcome.Success -> {
                    val characterListResource = Resource.Success(characterOutcome.value)
                    _charactersListLiveData.postValue(characterListResource)
                }
                is Outcome.Error -> {
                    val error =
                        Resource.Error<List<Character>?>(characterOutcome.exceptionDetails?.cause)
                    _charactersListLiveData.postValue(error)
                }
            }
        }
    }

    fun getCharacter(characterId: Int) {
        viewModelScope.launch(CoroutineDispatcher.IO) {
            _characterLiveData.postValue(Resource.Loading())
            val timeStamp = System.currentTimeMillis()
            val hash = securityUseCase.md5Encryption(timeStamp)
            when (val characterOutcome = characterRepository.getCharacter(characterId, timeStamp, hash)) {
                is Outcome.Success -> {
                    val characterResource = Resource.Success(characterOutcome.value)
                    _characterLiveData.postValue(characterResource)
                }
                is Outcome.Error -> {
                    val error = Resource.Error<Character?>(characterOutcome.exceptionDetails?.cause)
                    _characterLiveData.postValue(error)
                }
            }
        }
    }
}