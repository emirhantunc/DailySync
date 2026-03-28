package com.example.dailysync.ui


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailysync.core.userprofile.domain.repository.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor
    (
    private val userProfileRepository: UserProfileRepository
) :
    ViewModel() {
    private val _loginState = userProfileRepository.getCurrentUserId()

    val loginState: StateFlow<String?> = _loginState.map { result ->
        result.getOrNull()
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
}