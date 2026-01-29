

sealed class Routes(val route: String){
    object SignInScreen:Routes("sign_in")
    object SignUpScreen:Routes("sign_up")
    object Home:Routes("home")

    object Search:Routes("search")

    object Profile : Routes("profile?userId={userId}") {
        fun createRoute(userId: String? = null): String {
            return if (userId != null) "profile?userId=$userId" else "profile"
        }
    }

}