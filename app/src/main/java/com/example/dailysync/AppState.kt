package com.example.dailysync

import Routes
import androidx.annotation.StringRes
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.dailysync.features.auth.presentation.ui.SignInScreen
import com.example.dailysync.features.auth.upresentation.ui.SignUpScreen
import com.example.dailysync.features.home.presentation.ui.HomeScreen
import com.example.dailysync.features.profile.presentation.ui.ProfileScreen
import com.example.dailysync.features.search.presentation.ui.SearchScreen


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
    PROFILE(
        route = Routes.Profile.route,
        title = R.string.profile_name,
        drawerTabIcon = Icons.Default.AccountCircle
    ),
}

@Composable
fun DailySyncNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.SignInScreen.route
    ) {
        composable(route = Routes.SignUpScreen.route) {
            SignUpScreen(navController = navController)

        }
        composable(route = Routes.SignInScreen.route) {
            SignInScreen(navController = navController, onSingInClick = {
                navController.navigate(Routes.SignUpScreen.route)
            })

        }
        composable(route = Routes.Home.route) {
            HomeScreen()
        }
        composable(route = Routes.Profile.route) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            ProfileScreen(userId = userId)
        }
        composable(route = Routes.Search.route) {
            SearchScreen(onUserClick = { id ->
                navController.navigate(Routes.Profile.createRoute(id))
            })
        }
    }
}

@Composable
fun DailySyncBottomBar(navController: NavHostController) {
    val items = listOf(
        BottomItem.HOME,
        BottomItem.SEARCH,
        BottomItem.PROFILE
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        items.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

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