package com.example.dailysync

import Routes
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
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
                val authRoutes:List<Routes> = listOf(Routes.SignInScreen,Routes.SignUpScreen)
                val showBottomBar = currentScreen !in authRoutes.map { it.route }


                Scaffold(
                    bottomBar = {
                        if (showBottomBar){
                            DailySyncBottomBar(navController = navController)
                        }

                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        DailySyncNavGraph(navController = navController)
                    }
                }
            }
        }
    }
}