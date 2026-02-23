package com.example.dailysync.features.auth.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.dailysync.R
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.ui.LoadingProgressBar
import com.example.dailysync.features.auth.presentation.ui.components.AuthTextField
import com.example.dailysync.features.auth.presentation.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    onSingUpClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        LoadingProgressBar()
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(R.string.sing_in_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AuthTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = stringResource(R.string.e_mail),
            leadingIcon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = stringResource(R.string.password),
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation()
        )

        val errorMessage = when (uiState.errorType) {
            ErrorType.NETWORK_ERROR -> stringResource(R.string.network_error)
            ErrorType.ACCOUNT_NOT_FOUND -> stringResource(R.string.account_not_found)
            ErrorType.INVALID_CREDENTIALS -> stringResource(R.string.account_not_found)
            else -> {
                stringResource(R.string.unknown_error)
            }
        }
        if (uiState.errorType != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.displaySmall
            )
        }

        Button(
            onClick = { viewModel.signIn() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                stringResource(R.string.sign_in),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }

        TextButton(
            onClick = { onSingUpClick() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                stringResource(R.string.ask_for_sign_up),
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}