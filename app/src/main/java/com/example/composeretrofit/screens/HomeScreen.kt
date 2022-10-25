package com.example.composeretrofit.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.composeretrofit.MarsApiStatus
import com.example.composeretrofit.MarsViewModel
import com.example.composeretrofit.R
import com.example.composeretrofit.network.MarsProperty
import com.example.composeretrofit.redirectedImgSrcUrl


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(navController: NavController, marsViewModel: MarsViewModel, onSetTitle: (String) -> Unit, onShowDropdownMenu: (Boolean) -> Unit
){
    val appTitle = stringResource(id = R.string.app_name)
    val status by marsViewModel.status.observeAsState(MarsApiStatus.ERROR)
    val properties by marsViewModel.properties.observeAsState(listOf())

    LaunchedEffect(Unit) { onSetTitle(appTitle)
        onShowDropdownMenu(true)
    }
    
    Column( modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when(status){
            MarsApiStatus.DONE -> {
                LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 140.dp)
                ){
                    itemsIndexed(properties) {
                            index, property ->
                        MarsPhotoGridLayout(
                            property,
                            onPhotoCliked = { navController.navigate("details/$index")}
                        )
                    }
                }
            }
            MarsApiStatus.LOADING -> {
                Image(
                    painter = rememberImagePainter(data = R.drawable.loading_animation),
                    contentDescription = "Loading Properties",
                modifier = Modifier.fillMaxSize()
                )
            }
            else ->{
                Image(
                    painter = rememberImagePainter(data =R.drawable.ic_connection_error),
                    contentDescription = "Connection Error",
                    modifier = Modifier.fillMaxSize(0.5f)
                )
                Text(
                    "Error! Can't connect to the internet.\\n\" +\n" +
                            "                            \"Check your internet connection.\"",
                    color = MaterialTheme.colors.error

                )
            }
        }

    }

}

@Composable
fun MarsPhotoGridLayout(
    property: MarsProperty,
    onPhotoCliked: () -> Unit)
{
  Box(
      modifier = Modifier
          .padding(4.dp)
          .clickable { onPhotoCliked },
      contentAlignment = Alignment.BottomEnd
      ){
      Image(
          painter = rememberImagePainter(data = redirectedImgSrcUrl(property.imgSrcUrl),
              builder = {
                  placeholder(R.drawable.loading_animation)
                  error(R.drawable.ic_broken_image)
              }
              ),
            contentDescription = null,
            modifier = Modifier.height(120.dp),
            contentScale = ContentScale.Crop
          )
      if (!property.isRental){
          Icon(imageVector =Icons.Filled.ShoppingCart ,
              contentDescription = "Available For Sell",
              tint = Color.White,
              modifier = Modifier
                  .padding(8.dp)
                  .size(16.dp)
          )
      }
  }
}
