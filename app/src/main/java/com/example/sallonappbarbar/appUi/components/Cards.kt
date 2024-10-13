package com.example.sallonappbarbar.appUi.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.sallonappbarbar.R
import com.example.sallonappbarbar.appUi.viewModel.GetBarberDataViewModel
import com.example.sallonappbarbar.ui.theme.purple_200
import com.example.sallonappbarbar.ui.theme.sallonColor
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingPageText
import com.practicecoding.sallonapp.screens.initiatorScreens.OnBoardingText
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingBottomTextCard(
    navController: NavController,
    onBoardingTextList: List<OnBoardingPageText>,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
        border = BorderStroke(0.7.dp, Color.Gray),
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
                )
                .padding(top = 16.dp, start = 18.dp, end = 18.dp, bottom = 16.dp),
        ) {
            val pagerState = rememberPagerState(0, 0f) { onBoardingTextList.size }
            val scope = rememberCoroutineScope()
            HorizontalPager(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.TopCenter),
                state = pagerState,
                userScrollEnabled = false
            ) { text ->
                OnBoardingText(
                    mainHeading = onBoardingTextList[text].mainHeading,
                    bodyText = onBoardingTextList[text].bodyText,
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .align(Alignment.BottomStart)
            ) {
                repeat(3) { index ->
                    DotIndicator(selected = index == pagerState.currentPage)
                }
            }
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.BottomEnd),
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .padding(bottom = 32.dp, start = 32.dp)
                        .wrapContentSize(align = Alignment.BottomEnd)
                        .clip(RoundedCornerShape(50.dp)),
                    color = MaterialTheme.colorScheme.primary
                ) {

                    IconButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage > 0) {
                                    pagerState.animateScrollToPage(pagerState.currentPage - 1)
                                } else {
                                    /*TODO*/
                                    //open the welcome screen
                                }
                            }
                            onBackClick()
                        },
                        modifier = Modifier.background(color = colorResource(id = R.color.sallon_color))
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .padding(bottom = 32.dp, end = 32.dp, start = 16.dp)
                        .wrapContentSize(align = Alignment.BottomEnd)
                        .clip(RoundedCornerShape(50.dp)),
                    color = MaterialTheme.colorScheme.primary
                ) {

                    IconButton(
                        onClick = {
                            scope.launch {
                                if (pagerState.currentPage < 3) {
                                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                                } else {
                                    /*TODO*/
                                    //open the welcome screen
                                    // navController.navigate("welcome")
                                }
                            }
                            onNextClick()
                        },
                        modifier = Modifier.background(color = colorResource(id = R.color.sallon_color))
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight,
                            contentDescription = "Next",
                            tint = Color.White,
                            modifier = Modifier.size(45.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DotIndicator(selected: Boolean) {
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(8.dp)
            .background(
                color = if (selected) colorResource(id = R.color.sallon_color) else colorResource(id = R.color.grey),
                shape = CircleShape
            )
    )
}

//@Preview(showBackground = true)
@Composable
fun OnBoardingBottomTextCardPreview() {
    val onBoardingTextList = listOf(
        OnBoardingPageText(
            mainHeading = "Heading 1",
            bodyText = "Body 1"
        ),
        OnBoardingPageText(
            mainHeading = "Heading 2",
            bodyText = "Body 2"
        ),
        OnBoardingPageText(
            mainHeading = "Heading 3",
            bodyText = "Body 3"
        ),
    )
    val navController = rememberNavController()
    OnBoardingBottomTextCard(
        navController = navController,
        onBoardingTextList = onBoardingTextList,
        onNextClick = {},
        onBackClick = {}
    )
}



@Composable
fun HeadingText(
    bodyText: String
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = bodyText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun ServiceNameAndPriceCard(
    serviceName: String,
    serviceTime: String,
    servicePrice: String,
    count: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = serviceName,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = serviceTime + "mins",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = "Rs. $servicePrice",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(end = 8.dp)
        )
        if (count > 0) {
            Text(
                text = "×${count}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
fun ExpandableCard(
    title: String,
    expanded: Boolean,
    content: @Composable () -> Unit
) {
    var isExpanded by remember { mutableStateOf(expanded) }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, sallonColor, RoundedCornerShape(10.dp)),
            elevation = 4.dp,
            shape = RoundedCornerShape(10.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                        .clickable { isExpanded = !isExpanded }
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = Color.Black
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(1.dp))
        if (isExpanded) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    .border(1.dp, sallonColor, RoundedCornerShape(10.dp))
                ,
                elevation = 4.dp,
                shape = RoundedCornerShape(10.dp)
            ) {
                content()
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun DoubleCardPreview(){
//    val navController = rememberNavController()
//    OnBoardingBottomTextCard(
//        navController = navController,
//        onBoardingTextList = listOf(
//            OnBoardingPageText(
//                mainHeading = "Heading 1",
//                bodyText = "Body 1"
//            )
//        ) ,
//        onNextClick = { /*TODO*/ }) {
//
//    }
//}
