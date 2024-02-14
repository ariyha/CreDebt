package com.example.friendsindeed

interface Destinations{
    val route:String
}

object HomeScreen:Destinations{
    override val route: String = "HomeScreen"
}

object UserScreen:Destinations{
    override val route: String="UserScreen"
    const val username = "username"
}