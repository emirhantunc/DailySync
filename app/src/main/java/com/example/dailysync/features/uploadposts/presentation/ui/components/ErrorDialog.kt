package com.example.dailysync.features.uploadposts.presentation.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.dailysync.R
import com.example.dailysync.core.enums.ErrorType

@Composable
fun ErrorDialog(
    errorType: ErrorType,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.error))
        },
        text = {
            Text(
                text = when (errorType) {
                    ErrorType.NETWORK_ERROR -> stringResource(R.string.network_error)
                    ErrorType.AUTH_ERROR -> stringResource(R.string.error)
                    ErrorType.UNKNOWN_ERROR -> stringResource(R.string.unknown_error)
                    else -> stringResource(R.string.unknown_error)
                }
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.ok))
            }
        }
    )
}