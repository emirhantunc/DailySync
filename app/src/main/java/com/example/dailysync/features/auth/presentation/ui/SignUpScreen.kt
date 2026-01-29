package com.example.dailysync.features.auth.upresentation.ui

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dailysync.features.auth.presentation.ui.components.AuthTextField
import com.example.dailysync.features.auth.presentation.viewmodel.SignUpViewModel
import kotlinx.coroutines.flow.update

@Composable
fun SignUpScreen(navController: NavController, viewModel: SignUpViewModel = hiltViewModel()) {


    val uiState by viewModel.uiState.collectAsState()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Yeni Hesap Oluştur",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Hedeflerini takip etmeye bugün başla.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        AuthTextField(
            value = uiState.name,
            onValueChange = { viewModel.onNameChange(it) },
            label = "Ad Soyad",
            leadingIcon = Icons.Default.Person
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            onClick = { viewModel.signUp() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Kayıt Ol", style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp))
        }

        TextButton(
            onClick = { navController.popBackStack() }
        ) {
            Text("Zaten hesabın var mı? Giriş Yap", color = MaterialTheme.colorScheme.primary)
        }
    }
}