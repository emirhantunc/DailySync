 package com.example.dailysync.features.uploadposts.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.dailysync.R
import com.example.dailysync.features.uploadposts.presentation.ui.components.AddGoalCard
import com.example.dailysync.features.uploadposts.presentation.ui.components.BottomUploadBar
import com.example.dailysync.features.uploadposts.presentation.ui.components.ErrorDialog
import com.example.dailysync.features.uploadposts.presentation.ui.components.GoalItem
import com.example.dailysync.features.uploadposts.presentation.viewmodel.UploadPostsViewModel

@Composable
fun UploadPostsScreen(
    viewModel: UploadPostsViewModel = hiltViewModel(),
    onUploadSuccess: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackBarHostState = remember { SnackbarHostState() }
    val saveSuccessfullyMessage = stringResource(R.string.save_successfully_snackBar_message)

    LaunchedEffect(uiState.uploadSuccess) {
        if (uiState.uploadSuccess) {
            onUploadSuccess()
            viewModel.resetUploadSuccess()
        }
    }

    LaunchedEffect(uiState.saveSuccess) {
        if (uiState.saveSuccess) {
            snackBarHostState.showSnackbar(message = saveSuccessfullyMessage)
            viewModel.resetSaveSuccess()
        }
    }

    LaunchedEffect(uiState.errorType) {
        uiState.errorType?.let {
            viewModel.resetErrorType()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomUploadBar(
                onUploadClick = { viewModel.uploadSelectedGoals() },
                hasSelectedGoals = uiState.userGoals.any { it.isSelected },
                isLoading = uiState.isLoading
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AddGoalCard(
                        onSaveGoal = { goal, timeRange, target ->
                            viewModel.addNewGoal(goal, timeRange, target)
                        }
                    )
                }
                if (uiState.userGoals.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.my_goals),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    items(uiState.userGoals) { goal ->
                        GoalItem(
                            goal = goal,
                            onClick = { viewModel.toggleGoalSelection(goal.id) }
                        )
                    }
                }
            }

            if (uiState.errorType != null) {
                ErrorDialog(
                    errorType = uiState.errorType,
                    onDismiss = { viewModel.resetErrorType() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.upload_posts),
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}