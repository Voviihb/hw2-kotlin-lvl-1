package com.voviihb.dz2

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

const val TAG = "MainViewModel"

class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var job: Job? = null

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable.localizedMessage ?: "Exception occurred!")
    }

    val dogsList = mutableStateListOf<DogImage?>()


    fun loadDogImage() {
        _loading.value = true
        job = viewModelScope.launch {
            val response = mainRepository.loadDogImage(exceptionHandler)

            if (response.isSuccessful) {
                dogsList += response.body()
                _loading.value = false
            } else {
                onError("Error : ${response.message()} ")
            }
        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}