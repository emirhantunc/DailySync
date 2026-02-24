package com.example.dailysync.features.home.presentation.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.dailysync.R
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.enums.FeedType
import com.example.dailysync.core.utils.getTimeAgo
import com.example.dailysync.features.home.presentation.model.GoalInfoPresentation
import com.example.dailysync.features.home.presentation.ui.components.ComposerSection
import com.example.dailysync.features.home.presentation.ui.components.PostItem
import com.example.dailysync.features.home.presentation.ui.components.ThoughtItem
import com.example.dailysync.features.home.presentation.viewmodel.HomeViewModel
import java.util.Collections.emptyList

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToProfile: (String) -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(uiState.shareSuccess) {
        if (uiState.shareSuccess) {
            snackBarHostState.showSnackbar(message = context.getString(R.string.thought_shared))
            viewModel.resetShareSuccess()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchFeeds()
    }


    LaunchedEffect(uiState.errorType) {
        uiState.errorType?.let { errorType ->
            val message = when (errorType) {
                ErrorType.EMPTY_THOUGHT -> context.getString(R.string.empty_thought)
                ErrorType.NETWORK_ERROR -> context.getString(R.string.network_error)
                else -> context.getString(R.string.unknown_error)
            }
            snackBarHostState.showSnackbar(message)
            viewModel.resetErrorType()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                onNotificationClick = onNavigateToNotifications,
                onMessagesClick = onNavigateToMessages,
                unreadCount = uiState.unreadNotificationCount
            )
        },
        containerColor = colorScheme.background,
        snackbarHost = { SnackbarHost(snackBarHostState) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ComposerSection(viewModel) }

            items(uiState.feeds) { feed ->

                val feedType = when (feed.type) {
                    "THOUGHT" -> FeedType.THOUGHT
                    "POST" -> FeedType.POST
                    else -> FeedType.THOUGHT
                }
                when (feed.type) {
                    "THOUGHT" -> {
                        ThoughtItem(
                            thought = feed.thoughtContent,
                            userName = feed.name,
                            userId = feed.userId,
                            timeAgo = getTimeAgo(feed.releaseTime, context = context),
                            likeCount = feed.likeNumber,
                            isLiked = uiState.likedFeeds.contains(feed.id),
                            onUserClick = { onNavigateToProfile(feed.userId) },
                            onLikeClick = { viewModel.toggleLike(feed.id, feedType,feed.targetId) })
                    }

                    "POST" -> {
                        feed.goals?.let { goals ->
                            PostItem(
                                userName = feed.name,
                                timeAgo = getTimeAgo(feed.releaseTime, context = context),
                                goalInfoList = goals.goals,
                                likeCount = feed.likeNumber,
                                isLiked = uiState.likedFeeds.contains(feed.id),
                                onUserClick = { onNavigateToProfile(feed.userId) },
                                onLikeClick = {
                                    viewModel.toggleLike(
                                        feedId = feed.id,
                                        contentType = feedType,
                                        contentId = feed.targetId
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopAppBar(
    onNotificationClick: () -> Unit = {},
    onMessagesClick: () -> Unit = {},
    unreadCount: Int = 0
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.home_name),
            style = MaterialTheme.typography.titleLarge,
            color = colorScheme.onSurface
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            IconButton(
                onClick = onMessagesClick, modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = stringResource(R.string.messages),
                    tint = colorScheme.onSurface
                )
            }

            Box {
                IconButton(
                    onClick = onNotificationClick, modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = stringResource(R.string.notifications),
                        tint = colorScheme.onSurface
                    )
                }

                if (unreadCount > 0) {
                    Badge(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 4.dp, end = 4.dp),
                        containerColor = colorScheme.error
                    ) {
                        Text(
                            text = if (unreadCount > 9) "9+" else unreadCount.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}






