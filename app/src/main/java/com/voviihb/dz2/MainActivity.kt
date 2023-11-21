package com.voviihb.dz2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.flow.distinctUntilChanged

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainRepository = MainRepository(ApiFactory.apiService)
        viewModel =
            ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainViewModel::class.java]

        setContent {
            val listState = rememberLazyListState()
            val dogsList = remember { mutableStateListOf<DogImage>() }
            val loading by viewModel.loading.collectAsState(initial = false)
            val errorMsg by viewModel.errorMessage.collectAsState(initial = "")

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(dogsList) { dogImage ->
                        ListRow(model = dogImage)
                    }
                }

                if (loading) {
                    ShowLoading()
                }

                if (errorMsg != "") {
                    ShowError(msg = errorMsg)
                }

                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.loadDogImages() }
                    ) {
                        Text("Load dogs")

                    }
                }
            }
            LaunchedEffect(viewModel) {
                viewModel.dogsImage
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .distinctUntilChanged()
                    .collect { data ->
                        if (data != null) {
                            dogsList.add(data)
                            Log.d(TAG, data.toString())
                        }
                    }
            }
        }
        viewModel.loadDogImages()
    }

    companion object {
        const val TAG = "MainActivity"
    }
}

@Composable
fun ListRow(model: DogImage) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(1f),
            contentAlignment = Alignment.Center
            ) {
            Card{
                ShowDog(imageUrl = model.message)
            }
        }
    }
}

@Composable
fun ShowLoading() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator()
        }

    }
}

@Composable
fun ShowError(msg: String) {
    Toast.makeText(LocalContext.current, msg, Toast.LENGTH_LONG).show()
}

@Composable
fun ShowDog(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.img_description),
        error = painterResource(id = R.drawable.no_image_error),
        contentScale = ContentScale.Crop
    )
}

fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1