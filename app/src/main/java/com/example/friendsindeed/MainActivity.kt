package com.example.friendsindeed

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.friendsindeed.ui.theme.FriendsInDeedTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            FriendsInDeedTheme {
                MaterialTheme {
                    MyNavigation()
                }
            }
        }
    }
}


@Composable
fun MyNavigation(){
    val navcontroller = rememberNavController()
    NavHost(navController = navcontroller, startDestination = HomeScreen.route){
        composable(HomeScreen.route){
            HomeScreen(navcontroller)
        }
        composable(
            UserScreen.route+"/{${UserScreen.username}}",
            arguments = listOf(
                navArgument(UserScreen.username){
                    type= NavType.StringType})){
                    UserScreen(it.arguments?.getString(UserScreen.username))
            }
        }
}



