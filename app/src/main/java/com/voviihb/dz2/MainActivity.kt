package com.voviihb.dz2

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainRepository = MainRepository(ApiFactory.apiService)
        val viewModel =
            ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainViewModel::class.java]

        setContent {
            val listState = rememberLazyListState()
            val dogList = remember { viewModel.dogsList }
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
                    items(dogList) { dogImage ->
                        if (dogImage != null) ListRow(model = dogImage)
                    }
                    if (loading) {
                        item {
                            ShowLoading()
                        }
                    }
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
                        onClick = { viewModel.loadDogImage() }
                    ) {
                        Text("Load dogs")
                    }
                }
            }
//            LaunchedEffect(viewModel) {
//                viewModel.dogsList
//                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
//                    .distinctUntilChanged()
//                    .collect { data ->
//                        for ((index, value) in data.withIndex()) {
//                            Log.d(TAG, index.toString())
//                            if (value != null) {
//                                if (dogList.lastIndex >= index) {
//                                    Log.d(TAG, dogList.lastIndex.toString())
//                                    dogList[index] = value
//                                } else {
//                                    dogList.add(value)
//                                }
//                            }
//                        }
//                    }
//            }
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f),
            contentAlignment = Alignment.Center
        ) {
            Card {
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
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.img_description),
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator()
            }

            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = painterResource(id = R.drawable.no_image_error),
                    contentDescription = stringResource(R.string.img_description),
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            else -> {
                Image(
                    painter = painter,
                    contentDescription = stringResource(R.string.img_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(painter.intrinsicSize.width / painter.intrinsicSize.height)
                )
            }
        }
    }
}

fun LazyListState.isScrolledToTheEnd() =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1