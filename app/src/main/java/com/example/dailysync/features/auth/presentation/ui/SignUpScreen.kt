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
import androidx.compose.material.icons.filled.Person
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
import com.example.dailysync.features.auth.presentation.viewmodel.SignUpViewModel

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    goSignInScreen: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        LoadingProgressBar()
    }
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            goSignInScreen()
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
            text = stringResource(R.string.create_new_account),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        AuthTextField(
            value = uiState.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = stringResource(R.string.name_surname),
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            ErrorType.WEAK_PASSWORD -> stringResource(R.string.weak_password)
            ErrorType.EMAIL_OCCUPIED -> stringResource(R.string.email_occupied)
            ErrorType.UNKNOWN_ERROR -> stringResource(R.string.unknown_error)
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
            onClick = { viewModel.signUp() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text(
                stringResource(R.string.sign_up),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp)
            )
        }

        TextButton(
            onClick = { goSignInScreen() }
        ) {
            Text(stringResource(R.string.have_account), color = MaterialTheme.colorScheme.primary)
        }
    }
}