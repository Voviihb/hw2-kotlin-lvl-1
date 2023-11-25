package com.voviihb.dz2

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {
    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _dogsList = mutableStateListOf<String>()
    val dogsList: List<String> = _dogsList

    fun loadDogImages() {
        _loading.value = true
        viewModelScope.launch {
            try {
                val response = mainRepository.loadDogImages()
                if (response.isSuccessful) {
                    if (response.body()?.message != null) {
                        _dogsList += response.body()!!.message
                    }
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

    fun clearError() {
        _errorMessage.value = ""
    }


}