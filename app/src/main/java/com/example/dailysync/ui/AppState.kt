package com.example.dailysync.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import com.example.dailysync.features.home.presentation.ui.HomeScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.dailysync.R
import com.example.dailysync.ui.navigation.Routes
import com.example.dailysync.features.auth.presentation.ui.SignInScreen
import com.example.dailysync.features.auth.presentation.ui.SignUpScreen
import com.example.dailysync.features.chat.presentation.ui.chatlist.ChatListScreen
import com.example.dailysync.features.chat.presentation.ui.chatroom.ChatRoomScreen
import com.example.dailysync.features.notification.presentation.ui.NotificationScreen
import com.example.dailysync.features.profile.presentation.ui.ProfileScreen
import com.example.dailysync.features.search.presentation.ui.SearchScreen
import com.example.dailysync.features.uploadposts.presentation.ui.UploadPostsScreen
import com.google.firebase.auth.FirebaseAuth


enum class BottomItem(
    val route: String,
    @StringRes val title: Int,
    val drawerTabIcon: ImageVector,
) {
    HOME(
        route = Routes.Home.route,
        title = R.string.home_name,
        drawerTabIcon = Icons.Default.Home
    ),
    SEARCH(
        route = Routes.Search.route,
        title = R.string.search_name,
        drawerTabIcon = Icons.Default.Search
    ),
    UPLOAD_POST(
        route = Routes.UploadPost.route,
        title = R.string.upload_post_name,
        drawerTabIcon = Icons.Default.Add
    ),
    PROFILE(
        route = Routes.Profile.createRoute(null),
        title = R.string.profile_name,
        drawerTabIcon = Icons.Default.AccountCircle
    ),
}

@Composable
fun DailySyncNavGraph(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val userId by viewModel.loginState.collectAsStateWithLifecycle()

    LaunchedEffect(userId) {
        if (userId == null) {
            if (navController.currentDestination?.route != Routes.SignInScreen.route) {
                navController.navigate(Routes.SignInScreen.route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        inclusive = true
                    }
                }
            }
        }
    }
    val startDestination = if (userId != null) Routes.Home.route else Routes.SignInScreen.route

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = Routes.SignUpScreen.route) {
            SignUpScreen(goSignInScreen = {
                navController.navigate(Routes.SignInScreen.route)
            })
        }

        composable(route = Routes.SignInScreen.route) {
            SignInScreen(onSingUpClick = {
                navController.navigate(Routes.SignUpScreen.route)
            }, onNavigateToHome = {
                navController.navigate(Routes.Home.route) {
                    popUpTo(navController.graph.id) { inclusive = true }
                }
            })
        }

        composable(route = Routes.Home.route) {
            HomeScreen(
                onNavigateToProfile = { userId ->
                    navController.navigate(Routes.Profile.createRoute(userId))
                },
                onNavigateToNotifications = {
                    navController.navigate(Routes.Notification.route)
                },
                onNavigateToMessages = {
                    navController.navigate(Routes.ChatList.route)
                }
            )
        }
        composable(route = Routes.UploadPost.route) {
            UploadPostsScreen(
                onUploadSuccess = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(
            route = Routes.Profile.route,
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(
                userId = userId,
                onLogoutSuccess = {
                    navController.navigate(Routes.SignInScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToChat = { chatRoomId, otherUserName ->
                    navController.navigate(Routes.ChatRoom.createRoute(chatRoomId, otherUserName))
                }
            )
        }

        composable(route = Routes.Search.route) {
            SearchScreen(onUserClick = { id ->
                navController.navigate(Routes.Profile.createRoute(id))
            })
        }

        composable(route = Routes.Notification.route) {
            NotificationScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToProfile = { userId ->
                    navController.navigate(Routes.Profile.createRoute(userId))
                }
            )
        }

        composable(route = Routes.ChatList.route) {
            ChatListScreen(
                onChatClick = { chatRoomId, otherUserName ->
                    navController.navigate(Routes.ChatRoom.createRoute(chatRoomId, otherUserName))
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.ChatRoom.route,
            arguments = listOf(
                navArgument("chatRoomId") {
                    type = NavType.StringType
                },
                navArgument("otherUserName") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val chatRoomId = backStackEntry.arguments?.getString("chatRoomId") ?: ""
            val otherUserName = backStackEntry.arguments?.getString("otherUserName") ?: ""
            ChatRoomScreen(
                chatRoomId = chatRoomId,
                otherUserName = otherUserName,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun DailySyncBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomItem.HOME,
        BottomItem.SEARCH,
        BottomItem.UPLOAD_POST,
        BottomItem.PROFILE
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    NavigationBar {
        items.forEach { item ->
            val isSelected = when (item) {
                BottomItem.PROFILE -> {
                    val userId = navBackStackEntry?.arguments?.getString("userId")
                    currentRoute?.startsWith("profile") == true && userId == null
                }

                else -> currentDestination?.hierarchy?.any { it.route == item.route } == true
            }

            NavigationBarItem(
                icon = { Icon(item.drawerTabIcon, contentDescription = null) },
                label = { Text(stringResource(id = item.title)) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}