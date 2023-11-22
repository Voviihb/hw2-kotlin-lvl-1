package com.voviihb.dz2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    private val _dogsList = MutableStateFlow<List<DogImage?>>(mutableListOf())
    val dogsList: StateFlow<List<DogImage?>> = _dogsList


    fun loadDogImage() {
        _loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.loadDogImage()

            if (response.isSuccessful) {
                _dogsList.value += response.body()
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