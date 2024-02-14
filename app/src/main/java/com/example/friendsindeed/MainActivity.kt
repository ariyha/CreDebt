package com.example.friendsindeed

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.friendsindeed.ui.theme.FriendsInDeedTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
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


@RequiresApi(Build.VERSION_CODES.O)
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

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    var name: String,
    var amount:Int
)

@Dao
interface UserDao{
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user_table")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT amount FROM user_table WHERE name=:name")
    fun getamount(name:String)
}

@Database(entities = [User::class], version = 1)
abstract class UserDataBase:RoomDatabase(){
    abstract fun userDao():UserDao
}

class UserRepository(context: Context){
    private val database  = Room.databaseBuilder(context.applicationContext,UserDataBase::class.java,"users.db")
        .createFromAsset("database/users.db").build()

    fun getall() = database.userDao().getAll()
    fun insert(user: User) = database.userDao().insert(user)
    fun getamount(name: String) = database.userDao().getamount(name)


}