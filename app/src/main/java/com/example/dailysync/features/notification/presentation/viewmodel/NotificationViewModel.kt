package com.example.dailysync.features.notification.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.core.enums.ErrorType
import com.example.dailysync.core.exceptions.AppExceptions
import com.example.dailysync.features.notification.domain.usecases.NotificationUseCases
import com.example.dailysync.features.notification.presentation.mapper.toNotificationPresentationList
import com.example.dailysync.features.notification.presentation.models.NotificationPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NotificationState(
    val notifications: List<NotificationPresentation> = emptyList(),
    val unreadCount: Int = 0,
    val errorType: ErrorType? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationUseCases: NotificationUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationState())
    val uiState: StateFlow<NotificationState> = _uiState.asStateFlow()

    init {
        getNotifications()
        getUnreadCount()
    }

    private fun getNotifications() {
        viewModelScope.launch {
            notificationUseCases.getNotificationsUseCase().collect { result ->
                result.fold(
                    onSuccess = { notifications ->
                        _uiState.update { state ->
                            state.copy(
                                notifications = notifications.toNotificationPresentationList(),
                                isLoading = false
                            )
                        }
                    },
                    onFailure = { error ->
                        val errorType = when (error) {
                            is AppExceptions.Network.NoInternet -> ErrorType.NETWORK_ERROR
                            is AppExceptions.Auth.UserNotLoggedIn -> ErrorType.AUTH_ERROR
                            else -> ErrorType.UNKNOWN_ERROR
                        }
                        _uiState.update { state ->
                            state.copy(
                                errorType = errorType,
                                isLoading = false
                            )
                        }
                    }
                )
            }
        }
    }

    private fun getUnreadCount() {
        viewModelScope.launch {
            notificationUseCases.getUnreadCountUseCase().collect { result ->
                result.fold(
                    onSuccess = { count ->
                        _uiState.update { state ->
                            state.copy(unreadCount = count)
                        }
                    },
                    onFailure = { }
                )
            }
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            notificationUseCases.markAsReadUseCase(notificationId)
        }
    }

    fun markAllAsRead() {
        viewModelScope.launch {
            delay(500)
            val unreadNotifications = _uiState.value.notifications.filter { !it.isRead }
            Log.d("NotificationVM", "Marking ${unreadNotifications.size} notifications as read")
            unreadNotifications.forEach { notification ->
                Log.d("NotificationVM", "Marking notification ${notification.id} as read")
                notificationUseCases.markAsReadUseCase(notification.id)
            }
        }
    }

    fun resetErrorType() {
        _uiState.update { it.copy(errorType = null) }
    }
}
