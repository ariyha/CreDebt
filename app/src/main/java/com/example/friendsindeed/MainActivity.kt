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
            UserScreen.route+"/{${UserScreen.uid}}",
            arguments = listOf(
                navArgument(UserScreen.uid){
                    type= NavType.StringType})){
                    UserScreen(it.arguments?.getString(UserScreen.uid))
            }
        }
}

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey
    var uid: String,
    var name:String,
    var amount:Int
)

@Entity(tableName = "transaction_table")
data class Transaction (
    @PrimaryKey
    var tid: String,
    var uid:String,
    var description:String,
    var debt:String,
    var paid:String,
    var date:String,
    var time:String,
    var amount:Int
)

@Dao
interface UserDao{
    @Insert
    fun insert(user: User)

    @Query("SELECT * FROM user_table")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT amount FROM user_table WHERE uid=:uid")
    fun getamount(uid:String):Int

    @Insert
    fun inserttransact(transaction: Transaction)

    @Query("SELECT * FROM transaction_table WHERE uid=:uid")
    fun gettransact(uid:String): LiveData<List<Transaction>>

    @Query("SELECT * FROM user_table WHERE uid=:uid")
    fun getuser(uid:String): LiveData<List<User>>

    @Query("UPDATE transaction_table SET paid=:valu WHERE tid=:tid")
    fun updatepaid(tid:String,valu:String)

    @Query("UPDATE user_table SET amount=:amount WHERE uid=:uid")
    fun updateamount(uid:String,amount:Int)

}

@Database(entities = [User::class,Transaction::class], version = 1)
abstract class UserDataBase:RoomDatabase(){
    abstract fun userDao():UserDao
}

class UserRepository(context: Context){
    private val database  = Room.databaseBuilder(context.applicationContext,UserDataBase::class.java,"users.db").build()

    fun getall() = database.userDao().getAll()
    fun insert(user: User) = database.userDao().insert(user)
    fun getamount(uid: String) = database.userDao().getamount(uid)

    fun gettransact(uid: String) = database.userDao().gettransact(uid)

    fun inserttransact(transaction: Transaction) = database.userDao().inserttransact(transaction)


    fun updatepaid(tid: String,valu: String) = database.userDao().updatepaid(tid,valu)

    fun updateamount(uid: String, amount: Int) = database.userDao().updateamount(uid,amount)
}