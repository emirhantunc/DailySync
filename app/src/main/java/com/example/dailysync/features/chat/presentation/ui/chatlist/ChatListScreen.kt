package com.example.dailysync.features.chat.presentation.ui.chatlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailysync.R
import com.example.dailysync.core.ui.LoadingProgressBar
import com.example.dailysync.features.chat.presentation.ui.chatlist.components.ChatRoomItem
import com.example.dailysync.features.chat.presentation.ui.chatlist.components.FollowingUserItem
import com.example.dailysync.features.chat.presentation.viewmodel.ChatListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    onChatClick: (String, String) -> Unit = { _, _ -> },
    onNavigateBack: () -> Unit = {},
    viewModel: ChatListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchChatRooms()
        viewModel.fetchFollowingUsers()
    }

    LaunchedEffect(uiState.navigateToChatRoom) {
        uiState.navigateToChatRoom?.let { (chatRoomId, userName) ->
            onChatClick(chatRoomId, userName)
            viewModel.resetNavigation()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.messages)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (uiState.chatRooms.isNotEmpty()) {
                    items(uiState.chatRooms) { chatRoom ->
                        ChatRoomItem(
                            userName = chatRoom.otherUserName,
                            lastMessage = chatRoom.lastMessage,
                            onClick = { onChatClick(chatRoom.id, chatRoom.otherUserName) }
                        )
                    }
                }

                if (uiState.followingUsers.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = stringResource(R.string.new_conversation),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    items(uiState.followingUsers) { user ->
                        FollowingUserItem(
                            userName = user.name,
                            onClick = { viewModel.startNewChat(user.id, user.name) }
                        )
                    }
                }

                if (uiState.chatRooms.isEmpty() && uiState.followingUsers.isEmpty() && !uiState.isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.select_friend),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            if (uiState.isLoading) {
                LoadingProgressBar()
            }
        }
    }
}

