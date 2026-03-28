package com.example.dailysync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.dailysync.ui.BottomItem
import com.example.dailysync.ui.DailySyncBottomBar
import com.example.dailysync.ui.DailySyncNavGraph
import com.example.dailysync.ui.navigation.Routes
import com.example.dailysync.ui.theme.DailySyncTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailySyncTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentScreen = navBackStackEntry?.destination?.route ?: BottomItem.HOME.route
                val arguments = navBackStackEntry?.arguments
                val noBottomBarScreens: List<Routes> = listOf(
                    Routes.SignInScreen,
                    Routes.SignUpScreen,
                    Routes.Notification,
                    Routes.ChatList,
                    Routes.ChatRoom
                )
                val showBottomBar =
                    currentScreen !in noBottomBarScreens.map { it.route } && arguments?.getString("userId") == null

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            DailySyncBottomBar(navController = navController)
                        }

                    }
                ) { innerPadding ->
                    val layoutDirection = LocalLayoutDirection.current
                    val padding = if (showBottomBar) innerPadding else PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        start = innerPadding.calculateStartPadding(layoutDirection),
                        end = innerPadding.calculateEndPadding(layoutDirection),
                        bottom = 0.dp
                    )
                    Box(modifier = Modifier.padding(paddingValues =padding)) {
                        DailySyncNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}