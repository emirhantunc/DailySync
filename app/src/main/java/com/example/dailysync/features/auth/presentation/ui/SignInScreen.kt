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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dailysync.features.auth.presentation.ui.components.AuthTextField
import com.example.dailysync.features.auth.presentation.viewmodel.SignInViewModel

@Composable
fun SignInScreen(
    navController: NavController,
    onSingInClick: () -> Unit,
    viewModel: SignInViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            navController.navigate("home") {
                popUpTo("signIn") { inclusive = true }
            }
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
            text = "DailySync",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Yolculuğuna kaldığın yerden devam et.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AuthTextField(
            value = uiState.email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = "E-posta",
            leadingIcon = Icons.Default.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = uiState.password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = "Şifre",
            leadingIcon = Icons.Default.Lock,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { viewModel.signIn() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Giriş Yap", style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp))
        }

        TextButton(
            onClick = { onSingInClick() },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Hesabın yok mu? Kayıt Ol", color = MaterialTheme.colorScheme.secondary)
        }
    }
}