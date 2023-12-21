@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.willog_unsplash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.willog_unsplash.data.model.PhotoData
import com.example.willog_unsplash.navigation.Screens
import com.example.willog_unsplash.ui.components.CustomTopAppBar
import com.example.willog_unsplash.ui.components.DetailInfo
import com.example.willog_unsplash.ui.components.ImageFrame
import com.example.willog_unsplash.ui.components.SearchBar
import com.example.willog_unsplash.ui.events.SearchEvent
import com.example.willog_unsplash.ui.states.SearchState
import com.example.willog_unsplash.ui.theme.Willog_UnsplashTheme
import com.example.willog_unsplash.ui.theme.backGround
import com.example.willog_unsplash.ui.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Willog_UnsplashTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Unsplash()
                }
            }
        }
    }
}

@Composable
fun Unsplash() {
    val navController = rememberNavController()

    val snackbarHostState = remember { SnackbarHostState() }

    val title = remember { mutableStateOf("App Title") }
    val hasBookMark = remember { mutableStateOf(false) }

    BaseScreen(
        snackbarHostState = snackbarHostState,
        title = title.value,
        hasBookMark = hasBookMark.value
    ) { paddingValues ->

        NavHost(navController = navController, startDestination = Screens.SearchScreen.route) {

            composable(
                route = Screens.SearchScreen.route
            ) {
                title.value = "Search"
                hasBookMark.value = true
                val viewModel: SearchViewModel = hiltViewModel()
                val state = viewModel.state.collectAsStateWithLifecycle().value
                SearchScreen(
                    state = state,
                    onEvent = viewModel::onEvent
                )
            }

            composable(
                route = Screens.DetailScreen.route
            ) { backStackEntry ->
                title.value = "Details"
                hasBookMark.value = false
                DetailsScreen(navController, backStackEntry.arguments?.getString("imageId"))
            }

            composable(
                route = Screens.BookMarkScreen.route
            ) {
                title.value = "Bookmarks"
                hasBookMark.value = false
                BookmarksScreen(navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScreen(
    hasBookMark: Boolean = false,
    snackbarHostState: SnackbarHostState,
    title: String,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { CustomTopAppBar(title = title, hasBookMark = hasBookMark) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backGround) // 기본 배경색 지정
                    .padding(paddingValues), // Scaffold로부터 제공받는 padding 적용
                contentAlignment = Alignment.TopStart
            ) {
                content(paddingValues)
            }
        }
    )
}

@Composable
fun SearchScreen(
    state: SearchState = SearchState(),
    onEvent: (SearchEvent) -> Unit = { }
) {
    val query = rememberSaveable { mutableStateOf("") }
    val images =
        remember { mutableStateListOf<PhotoData>() } // Assuming PhotoData has a 'url' and 'id' property

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            SearchBar(
                hint = "Search",
                text = query.value,
                modifier = Modifier,
                focusRequester = FocusRequester(),
                visualTransformation = VisualTransformation.None,
                getNewString = { newText ->
                    query.value = newText
                }
            ) {
                onEvent(SearchEvent.GetSearchQuery(query.value))
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyVerticalGrid(columns = GridCells.Fixed(4)) {
                items(images.size) { index ->
                    ImageFrame(/*image = image.url*/) {
                        //navController.navigate("details/${image.id}")
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailsScreen(navController: NavController, imageId: String?) {
    // TODO: API를 통해 상세 이미지 정보를 가져오기
    // imageId를 사용하여 특정 이미지의 상세 정보를 표시
    Column {
        Scaffold(
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* 클릭 시 수행할 액션 */ },
                    shape = CircleShape,
                    containerColor = Color.White,
                ) {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = "BookMark",
                        tint = Color.Red
                    )
                }
            }
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                ) {
                    ImageFrame(/*image = image.url*/
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        //navController.navigate("details/${image.id}")
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(Color.White, shape = RoundedCornerShape(10.dp)),
                ) {
                    DetailInfo(type = "ID", value = "value")
                    Divider(color = Color(0xFFE9E9E9), thickness = 1.dp)
                    DetailInfo(type = "Author", value = "value")
                    Divider(color = Color(0xFFE9E9E9), thickness = 1.dp)
                    DetailInfo(type = "Size", value = "value")
                    Divider(color = Color(0xFFE9E9E9), thickness = 1.dp)
                    DetailInfo(type = "Created At", value = "value")
                }
            }
        }
    }
}

@Composable
fun BookmarksScreen(navController: NavController) {

    val images =
        remember { mutableStateListOf<PhotoData>() } // Assuming PhotoData has a 'url' and 'id' property

    // TODO: 북마크된 이미지 목록 표시
    LazyVerticalGrid(columns = GridCells.Fixed(4)) {
        items(images.size) { index ->
            ImageFrame(/*image = image.url*/) {
                //navController.navigate("details/${image.id}")
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreview() {
    Willog_UnsplashTheme {
        Unsplash()
    }
}