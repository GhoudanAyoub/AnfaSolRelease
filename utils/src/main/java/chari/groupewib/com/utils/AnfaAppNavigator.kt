package ghoudan.anfaSolution.com.utils

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions


object AnfaAppNavigator {
    fun navigate(navController: NavController, action: NavDirections) {

        val appNavOptions = NavOptions.Builder().apply {
            setLaunchSingleTop(true)
            setEnterAnim(R.anim.slide_in_right)
            setExitAnim(R.anim.slide_out_left)
            setPopEnterAnim(R.anim.slide_in_left)
            setPopExitAnim(R.anim.slide_out_right)
        }.build()

        navController.navigate(directions = action, navOptions = appNavOptions)
    }
}
