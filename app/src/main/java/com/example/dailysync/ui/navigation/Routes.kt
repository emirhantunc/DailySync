package com.example.dailysync.ui.navigation

sealed class Routes(val route: String){
    object SignInScreen:Routes("sign_in")
    object SignUpScreen:Routes("sign_up")
    object Home:Routes("home")
    object UploadPost: Routes("upload_post")

    object Search:Routes("search")

    object Notification:Routes("notification")

    object ChatList:Routes("chat_list")

    object ChatRoom:Routes("chat_room?chatRoomId={chatRoomId}&otherUserName={otherUserName}") {
        fun createRoute(chatRoomId: String, otherUserName: String): String {
            return "chat_room?chatRoomId=$chatRoomId&otherUserName=$otherUserName"
        }
    }

    object Profile : Routes("profile?userId={userId}") {
        fun createRoute(id: String? = null): String {
            return if (id != null) "profile?userId=$id" else "profile"
        }
    }
}