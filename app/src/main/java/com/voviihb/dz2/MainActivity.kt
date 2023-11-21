package com.voviihb.dz2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainRepository = MainRepository(ApiFactory.apiService)
        viewModel =
            ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainViewModel::class.java]

        val dogsList: MutableList<DogImage?> = mutableListOf()

        this.lifecycleScope.launch {
            viewModel.dogsImage
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collect { data ->
                    dogsList.add(data)
                    Log.d(TAG, data.toString())
                }
        }

        this.lifecycleScope.launch {
            viewModel.loading
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .distinctUntilChanged()
                .collect {
                    if (it) {
                        Log.d(TAG, "LOADING")
                    } else {
                        Log.d(TAG, "STOP LOADING")
                    }
                }
        }


        viewModel.loadDogImages()


        setContent {
            val list = remember {
                mutableStateListOf(dogsList)
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(list[0]) {
                        if (it != null) ListRow(model = it)
                    }
                    item {
                        Text("CHEEECk")
                    }
                }
                Row(
                    modifier = Modifier.weight(1f, false)
                ) {
                    Button(modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            viewModel.loadDogImages()
                        }
                    ) {
                        Text("Button push")
                    }
                }

            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

@Composable
fun ListRow(model: DogImage) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = model.message,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}