@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.friendsindeed

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp() {
        CenterAlignedTopAppBar(
            title = { Text(text = "My App",
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 30.sp
                )},
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painter = painterResource(id = R.drawable.search), contentDescription = "")
                }
            }
            )
}

@Composable
fun HomeScreen(navController: NavController){
    var newname by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val userRepository by lazy { UserRepository( context.applicationContext) }
    val onAddUser :(user: User)->Unit={user->
            runBlocking(IO) {
                userRepository.insert(user = user)
            }
    }

    val users = userRepository.getall().observeAsState(emptyList())


    Scaffold(
        topBar = { MyApp() },
        floatingActionButton = { ExtendedFloatingActionButton(
            onClick = { showBottomSheet=true },
            icon = { Icon(painter = painterResource(id = R.drawable.plus_new), "Extended floating action button.",
                modifier = Modifier.size(20.dp)) },
            text = { Text(text = "New") },
        )
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (showBottomSheet){
                ModalBottomSheet(onDismissRequest = { showBottomSheet=false }, sheetState = sheetState) {
                    Column(modifier= Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f),
                        ){
                        Text(
                            text = "Add Name",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(20.dp)
                        )
                        OutlinedTextField(
                            value = newname,
                            onValueChange = { newname = it },
                            label = { Text("Name") },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth()
                        )
                        OutlinedButton(onClick = {
                            Toast.makeText(context, "Pls welcome $newname", Toast.LENGTH_SHORT)
                                .show()
                            onAddUser(User(name = newname, amount = 0))
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                            modifier = Modifier.padding(20.dp)) {
                            Text(text = "Add User")
                        }
                    }                }
            }

            Column(
                Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                HomeUpperPanel()
                HomeLowerPanel(navController,users.value)

            }
        }
    }
}

@Composable
fun HomeUpperPanel(){
    val context = LocalContext.current
    Row {
        Card(modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth(0.5f)
            .padding(horizontal = 5.dp)
            .fillMaxHeight(0.1f),
            onClick = {Toast.makeText(context,"Thank you for using the app",Toast.LENGTH_SHORT).show()}
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()){
                Text(text = "You Owe",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace)
                Text(text = "₹1000",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White)
            }
        }
        Card(modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .fillMaxHeight(0.1f),
            onClick = {Toast.makeText(context,"Thank you for using the app",Toast.LENGTH_SHORT).show()}
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()){
                Text(text = "Owed to you",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace)
                Text(text = "₹1000",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White)
            }
        }

    }
}



@Composable
fun HomeLowerPanel(navController: NavController, users: List<User>){
    val context = LocalContext.current
    Row(modifier = Modifier.fillMaxSize()){
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(users) {_, element ->
                UserCard(
                    name = element.name,
                    indebt = element.amount < 0,
                    amount = element.amount,
                    navController = navController,
                    context = context,
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserCard(name:String, indebt:Boolean,amount: Int,navController: NavController,context:Context){
    Card (
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth(),
        onClick = {
            Toast.makeText(context, "Hello Motherfuckersssss",
                Toast.LENGTH_SHORT).show()
            navController.navigate(UserScreen.route+"/Nithish Ariyha")

        },

    ) {
        Row(modifier= Modifier
            .padding(5.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.profile_user),
                contentDescription = "Account User",
                modifier = Modifier
                    .size(50.dp) // Aligning the Image to the center
            )
            Text(
                text = name,
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(0.65f),
                color = Color.White
            )
            Column(verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally){
                Text(text = "₹${amount}",
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                    color = if(indebt) Color.Green else Color.Red )
            }

        }
    }
}

