package com.voviihb.dz2

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var job: Job? = null

    val dogsList = mutableStateListOf<DogImage?>()

    fun loadDogImage() {
        _loading.value = true
        job = viewModelScope.launch {
            try {
                val response = mainRepository.loadDogImage()
                if (response.isSuccessful) {
                    dogsList += response.body()
                    _loading.value = false
                } else {
                    onError("Error : ${response.message()} ")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Exception occurred!")
            }

        }
    }

    private fun onError(message: String) {
        _errorMessage.value = message
        _loading.value = false
    }

    fun clearError(){
        _errorMessage.value = ""
    }


}