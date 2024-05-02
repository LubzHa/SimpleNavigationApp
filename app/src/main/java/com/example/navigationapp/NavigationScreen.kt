package com.example.navigationapp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navigationapp.data.models.AddressSuggestionEntry
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.annotation.generated.CircleAnnotation
import com.mapbox.maps.extension.compose.annotation.generated.PolylineAnnotation
import com.mapbox.maps.extension.style.layers.properties.generated.LineJoin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel
) {

    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Hidden,
        skipHiddenState = false
    )
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        scaffoldState = scaffoldState,
        sheetContent = {
            SuggestedAddressesList(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.80f)
                    .background(Color.White)
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                suggestedAddresses = viewModel.suggestedAddresses) {
                viewModel.setPointTo(it)
                coroutineScope.launch {
                    sheetState.hide()
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetSwipeEnabled = false,
        sheetDragHandle = null
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            MapboxMap(
                modifier = Modifier.fillMaxSize(),
                mapViewportState = viewModel.viewPortState,
            ) {
                // /!\ Ugly : forcing to update changes (making the route a mutableStateList doesn't work)
                if (viewModel.routeChanged) {
                    RouteLines(points = viewModel.route)
                } else {
                    RouteLines(points = viewModel.route)
                }

                if (viewModel.coordinatesChanged) {
                    CoordinatesPoints(
                        startCoordinates = viewModel.startPointCoordinates,
                        endCoordinates = viewModel.endPointCoordinates
                    )
                } else  {
                    CoordinatesPoints(
                        startCoordinates = viewModel.startPointCoordinates,
                        endCoordinates = viewModel.endPointCoordinates
                    )
                }

            }

            // Search fields
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(32.dp))
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    searchText = viewModel.startAddress,
                    hint = "Where are you coming from ?",
                    onSearch = {
                        viewModel.currentSearchField = SearchField.DEPARTURE
                        viewModel.searchForAddressSuggestions(it)
                        if (sheetState.currentValue == SheetValue.Hidden) {
                            coroutineScope.launch {
                                sheetState.expand()
                            }
                        }
                    }
                )
                Spacer(Modifier.height(8.dp))
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    searchText = viewModel.endAddress,
                    hint = "Where are you going to ?",
                    onSearch = {
                        viewModel.currentSearchField = SearchField.ARRIVAL
                        viewModel.searchForAddressSuggestions(it)
                        if (sheetState.currentValue == SheetValue.Hidden) {
                            coroutineScope.launch {
                                sheetState.expand()
                            }
                        }
                    },
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .padding(horizontal = 8.dp),
                    onClick = {
                        viewModel.searchForRoute()
                    }
                ) {
                    val text =
                        if (viewModel.startPointCoordinates == null || viewModel.endPointCoordinates == null) {
                            "Enter your departure and destination"
                        } else {
                            "Let's Go !"
                        }
                    Text(text = text)
                }
            }
        }
    }


}


@Composable
fun RouteLines(points: List<Point>) {
    PolylineAnnotation(
        points = points,
        lineJoin = LineJoin.ROUND,
        lineColorInt = Color.Blue.toArgb(),
        lineWidth = 3.0,
        lineBlur = 0.3,
    )
}


@Composable
fun CoordinatesPoints(
    startCoordinates: Point?,
    endCoordinates: Point?
) {
    startCoordinates?.let {
        CircleAnnotation(
            point = it,
            circleColorInt = Color.Blue.toArgb(),
            circleRadius = 7.0
        )
    }
    endCoordinates?.let {
        CircleAnnotation(
            point = it,
            circleColorInt = Color.Green.toArgb(),
            circleRadius = 7.0
        )
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    searchText: MutableState<String>,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by searchText

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier.weight(1.0f, fill = true)
            ) {
                BasicTextField(
                    value = searchText.value,
                    onValueChange = {
                        text = it
                        onSearch(it)
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = modifier
                        .fillMaxWidth()
                        .onFocusChanged {
                            isHintDisplayed = !it.isFocused && text.isEmpty()
                        }
                )
                if (isHintDisplayed) {
                    Text(
                        text = hint,
                        color = Color.LightGray,
                    )
                }
            }
            // Delete button
            IconButton(
                modifier = Modifier.size(40.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.Black,
                ),
                onClick = {
                    text = ""
                }) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
fun SuggestedAddressesList(
    modifier: Modifier = Modifier,
    suggestedAddresses: List<AddressSuggestionEntry>,
    onSuggestedAddressSelected: (AddressSuggestionEntry) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomEnd = 8.dp,
                    bottomStart = 8.dp
                )
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        items(suggestedAddresses) { addressSuggestion ->
            Column(
                modifier = modifier.clickable {
                    onSuggestedAddressSelected(addressSuggestion)
                },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = addressSuggestion.name,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1
                )
                addressSuggestion.address?.let {
                    Text(
                        text = it,
                        color = Color.Black,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}