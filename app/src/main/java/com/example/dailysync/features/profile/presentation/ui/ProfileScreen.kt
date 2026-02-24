package com.example.dailysync.features.profile.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailysync.R
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.ui.DailySyncAlertDialog
import com.example.dailysync.core.utils.getTimeAgo
import com.example.dailysync.features.profile.presentation.ui.components.NoActivitiesCard
import com.example.dailysync.features.profile.presentation.ui.components.PostCard
import com.example.dailysync.features.profile.presentation.ui.components.ProfileThoughtItem
import com.example.dailysync.features.profile.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userId: String? = null,
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit = {},
    onNavigateToChat: (String, String) -> Unit = { _, _ -> }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile = uiState.profile
    val isOwnProfile = userId == null || userId.isBlank()
    val context = LocalContext.current
    val ownId = uiState.ownId

    LaunchedEffect(userId) {
        viewModel.getProfile(userId)
    }

    LaunchedEffect(uiState.logoutSuccess) {
        if (uiState.logoutSuccess) {
            onLogoutSuccess()
        }
    }
    LaunchedEffect(isOwnProfile) {
        if (!isOwnProfile) {
            viewModel.getOwnId()
        }
    }

    LaunchedEffect(uiState.navigateToChatRoom) {
        uiState.navigateToChatRoom?.let { (chatRoomId, userName) ->
            onNavigateToChat(chatRoomId, userName)
            viewModel.resetChatNavigation()
        }
    }

    val isFollowing = profile.followers.contains(ownId)

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = {
                    Text(
                        stringResource(R.string.profile_name),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    if (isOwnProfile) {
                        IconButton(onClick = { viewModel.showLogoutDialog() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.Logout,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        uiState.error?.let {
            val errorMessage = when (it) {
                ErrorType.NETWORK_ERROR -> R.string.network_error
                ErrorType.ACCOUNT_NOT_FOUND -> R.string.no_account
                else -> {
                    R.string.unknown_error
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(errorMessage),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }

        } ?: run {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Transparent)
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(50.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }

                            AnimatedVisibility(
                                visible = true,
                                enter = fadeIn() + slideInVertically()
                            ) {
                                Text(
                                    text = profile.name.ifEmpty { stringResource(R.string.loading) },
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        if (isOwnProfile) {
                            Button(
                                onClick = { },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                contentPadding = PaddingValues(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 4.dp,
                                    pressedElevation = 8.dp
                                )
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    stringResource(R.string.set_profile),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        } else {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        viewModel.createOrGetChatRoom(userId, profile.name)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(16.dp)
                                ) {
                                    Text(
                                        text = stringResource(R.string.messages),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                val followButtonContainerColor = if (isFollowing) {
                                    MaterialTheme.colorScheme.surfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.primary
                                }

                                val followButtonContentColor = if (isFollowing) {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                } else {
                                    MaterialTheme.colorScheme.onPrimary
                                }

                                val followButtonTextResId =
                                    if (isFollowing) R.string.following else R.string.follow

                                Button(
                                    onClick = {
                                        viewModel.toggleFollow(userId)
                                        viewModel.toggleFollow(userId)
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    contentPadding = PaddingValues(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = followButtonContainerColor,
                                        contentColor = followButtonContentColor
                                    ),
                                    elevation = ButtonDefaults.buttonElevation(
                                        defaultElevation = if (isFollowing) 0.dp else 4.dp,
                                        pressedElevation = 2.dp
                                    )
                                ) {
                                    Text(
                                        text = if (uiState.loading) stringResource(R.string.loading)
                                        else stringResource(followButtonTextResId),
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.last_activities),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (uiState.userPosts.isNotEmpty()) {
                                Text(
                                    text = "${uiState.userPosts.size} ${stringResource(R.string.upload_posts)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (uiState.userPosts.isEmpty()) {
                            NoActivitiesCard(modifier = modifier)
                        } else {
                            val pagerState =
                                rememberPagerState(pageCount = { uiState.userPosts.size })

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                HorizontalPager(
                                    state = pagerState,
                                    modifier = Modifier.fillMaxWidth(),
                                    pageSpacing = 16.dp
                                ) { page ->
                                    val post = uiState.userPosts[page]
                                    PostCard(
                                        userName = profile.name,
                                        timeAgo = getTimeAgo(post.releaseTime, context = context),
                                        goalInfoList = post.goals.goals,
                                        likeCount = post.likeNumber,
                                        isLiked = true,
                                        onLikeClick = {},
                                        onCompletedClicked = { id ->
                                            viewModel.toggleGoalCompletion(post.postId, id)
                                        }
                                    )
                                }

                                if (uiState.userPosts.size > 1) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        repeat(uiState.userPosts.size) { index ->
                                            Box(
                                                modifier = Modifier
                                                    .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (pagerState.currentPage == index)
                                                            MaterialTheme.colorScheme.primary
                                                        else
                                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                                alpha = 0.3f
                                                            )
                                                    )
                                            )
                                            if (index != uiState.userPosts.size - 1) {
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                uiState.userThoughts.forEach { thought ->
                    item {
                        ProfileThoughtItem(
                            thought = thought.thought,
                            timeAgo = getTimeAgo(thought.releaseTime, context = context),
                            likeCount = thought.likeNumber
                        )
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }



    if (uiState.showLogoutDialog) {
        DailySyncAlertDialog(
            title = stringResource(R.string.logout_dialog_title),
            subtitle = stringResource(R.string.logout_dialog_message),
            onDismiss = { viewModel.hideLogoutDialog() },
            onConfirm = { viewModel.signOut() },
            confirmButtonText = stringResource(R.string.confirm),
            dismissButtonText = stringResource(R.string.cancel)
        )
    }
}

