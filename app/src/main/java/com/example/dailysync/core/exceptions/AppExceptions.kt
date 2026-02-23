package com.example.dailysync.core.exceptions



sealed class AppExceptions : Exception() {
    sealed class Network : AppExceptions() {
        object NoInternet : Network()
    }

    sealed class Auth : AppExceptions() {
        object AccountNotFound : Auth()
        object WeakPassword : Auth()
        object InvalidCredentials: Auth()
        object EmailOccupied: Auth()
        object UserNotLoggedIn: Auth()
    }

    sealed class Profile: AppExceptions(){
        object ProfileNotFound: Profile()
        object InvalidId: Profile()
    }
    sealed class Goal : AppExceptions() {
        object TimeSlotOccupied : Goal()
    }

    object MappingError: AppExceptions()
    object Unknown : AppExceptions()
}