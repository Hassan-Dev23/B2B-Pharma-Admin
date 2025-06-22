package com.example.pharmaadminpro.ui.screen

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.pharmaadminpro.navigation.UserDetailsScreen
import com.example.pharmaadminpro.retrofit.dataTypes.AllUser
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UsersListUI(

    viewModel: AdminViewModel = AdminViewModel(),

    navBackStack: NavBackStack
) {
    val scope = rememberCoroutineScope()
    val state by viewModel.userListState.collectAsState()

    if (state is State.Success){
        var isRefreshing by remember { mutableStateOf(false) }
        val usersList = (state as State.Success).data.AllUsers
        Scaffold(modifier = Modifier.fillMaxSize()
        ) {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {

                    scope.launch {
                        isRefreshing = true
                        viewModel.getAllUsers()
                        delay(1000)
                        isRefreshing = false
                    }

                },
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
                state = rememberPullToRefreshState()
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(usersList) {
                        UserCard(it, navBackStack, viewModel)
                    }
                }
            }

        }
    }else{
        AnimatedVisibility(state is State.Loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                CircularProgressIndicator()

            }
        }
    }


}

@Composable
fun UserCard(
    user: AllUser,
    navBackStack: NavBackStack,
    viewModel: AdminViewModel
) {


    val isActive = user.isApproved == 1 && user.Blocked == 0

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = {
                viewModel.getUserById(user.USER_ID)
                navBackStack.add(UserDetailsScreen)
            }),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = user.USER_NAME,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = user.EMAIL,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            GlowingDotWithRadialEffect(isActive)
        }
    }
}


@Composable
fun GlowingDotWithRadialEffect(isActive: Boolean) {
    val dotColor = if (isActive) Color(0xFF01DA0B) else Color(0xE6EF0606)
    val pulse = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        while (true) {
            pulse.animateTo(.5f, tween(700, easing = LinearEasing))
            pulse.animateTo(.75f, tween(700, easing = LinearEasing))
        }
    }

    Box(
        modifier = Modifier
            .size(40.dp)
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        dotColor.copy(alpha = 0.4f),
                        Color.Transparent
                    ),
                    radius = size.minDimension / 3f
                ),
                center = center,
                radius = size.minDimension / 3f
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(16.dp * pulse.value)
                .background(dotColor, CircleShape)
        )
    }
}


