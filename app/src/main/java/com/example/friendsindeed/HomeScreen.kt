@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.friendsindeed

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(onCurrencyClick: () -> Unit = {}, currencySymbol: String = "₹") {
    CenterAlignedTopAppBar(
        title = { Text(text = "CreDebt",
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 30.sp
            )},
        actions = {
            IconButton(onClick = onCurrencyClick) {
                Text(
                    text = currencySymbol,
                    fontSize = 24.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
}

@ExperimentalMaterial3Api
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
    
    var showCurrencyDialog by remember {
        mutableStateOf(false)
    }
    
    var currencySymbol by remember {
        mutableStateOf(CurrencyManager.getCurrencySymbol(context))
    }

    val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


    val userRepository by lazy { UserRepository( context.applicationContext) }
    val onAddUser :(user: User)->Unit={user->
            runBlocking(IO) {
                    userRepository.insert(user = user)
            }
    }

    val users = userRepository.getall().observeAsState(emptyList())
    print("Database starting with no issues")


    Scaffold(
        topBar = { MyApp(onCurrencyClick = { showCurrencyDialog = true }, currencySymbol = currencySymbol) },
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
                        .fillMaxHeight(0.5f),
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
                            if (newname.isEmpty()) {
                                Toast.makeText(context, "Please enter a name", Toast.LENGTH_SHORT)
                                    .show()
                                return@OutlinedButton
                            }
                            val userid = pref.getInt("userid", 2)
                            pref.edit().putInt("userid", userid + 1).apply()
                            Toast.makeText(context, "Pls welcome $userid", Toast.LENGTH_SHORT)
                                .show()
                            onAddUser(User(uid = userid.toString(), name = newname, amount = 0))
                            newname = ""
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
            
            // Currency Selection Dialog
            if (showCurrencyDialog) {
                CurrencySelectionDialog(
                    onDismiss = { showCurrencyDialog = false },
                    onCurrencySelected = { currency ->
                        CurrencyManager.setCurrency(context, currency)
                        currencySymbol = CurrencyManager.getCurrencySymbol(context)
                        showCurrencyDialog = false
                    }
                )
            }

            Column(
                Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                var credit = 0
                var debt = 0
                for(user in users.value){
                    if (user.amount<0){
                        debt+=user.amount
                        }
                    else{
                        credit+=user.amount
                        }
                }
                HomeUpperPanel(credit,debt,currencySymbol)
                HomeLowerPanel(navController,users .value,currencySymbol)
            }
        }
    }
}

@Composable
fun HomeUpperPanel(credit:Int,debt:Int, currencySymbol: String){
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
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = "$currencySymbol${debt}",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Red)
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
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text(text = "$currencySymbol${credit}",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.Green)
            }
        }

    }
}



@Composable
fun HomeLowerPanel(navController: NavController, users: List<User>, currencySymbol: String){
    Row(modifier = Modifier.fillMaxSize()){
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(users) {_, element ->
                UserCard(
                    name = element.name,
                    indebt = element.amount < 0,
                    amount = element.amount,
                    navController = navController,
                    uid = element.uid,
                    currencySymbol = currencySymbol
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UserCard(name:String, indebt:Boolean,amount: Int,navController: NavController,uid:String, currencySymbol: String){

    var name by remember {
        mutableStateOf(name)
    }

    val context = LocalContext.current
    val userRepository by lazy { UserRepository( context.applicationContext) }

    var alertdialogval by remember {
        mutableStateOf(false)
    }

    val ondelete:(User)->Unit={user->
        runBlocking(IO) {
            userRepository.deleteuser(user)
        }
    }

    val ondeletedata:(String)->Unit={uid->
        runBlocking(IO) {
            userRepository.deletedata(uid)
        }
    }
    var editdialog by remember {
        mutableStateOf(false)
    }

    var editedname by remember {
        mutableStateOf("")
    }

    when{
        editdialog->{
            Dialog(onDismissRequest = { editdialog = false }) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)){
                    Column(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally){
                        OutlinedTextField(
                            value = editedname,
                            onValueChange = { editedname = it },
                            label = { Text("New Name") })
                        OutlinedButton(onClick = {
                            runBlocking(IO) {
                                userRepository.updateamount(
                                    User(
                                        uid = uid,
                                        name = editedname,
                                        amount = amount
                                    )
                                )
                                name = editedname
                            }
                            editdialog = false
                        }) {
                            Text(text = "Edit")
                        }
                    }
                }
            }
        }
    }

    when{
        alertdialogval->{
            AlertDialog(
                onDismissRequest = { alertdialogval = false },
                icon = { Icon(painter = painterResource(id = R.drawable.menu), contentDescription ="some" ) },
                confirmButton = {
                    TextButton(onClick = { alertdialogval = false
                    editdialog=true}) {
                        Text("Edit")
                    }
                },
                title = { Text(text = "Edit") },
                text = {
                    Text(text = "Do you want to edit or delete the user? ")
                },
                dismissButton = {
                    TextButton(onClick = {
                        if(amount!=0){
                            Toast.makeText(context,"Please clear the amount before deleting",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            ondeletedata(uid)
                            ondelete(User(uid = uid, name = name, amount = amount))
                            navController.popBackStack()
                            navController.navigate("HomeScreen")
                            alertdialogval = false
                        } }) {
                        Text("Delete")
                    }
                },
            )
        }
    }


    Card (
        modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .combinedClickable(onClick = {
                val json = Gson().toJson(User(uid = uid, name = name, amount = amount))
                navController.navigate(UserScreen.route + "/$json")

            },
                onLongClick = {
                    alertdialogval = true
                }),
    ) {
        Row(modifier= Modifier
            .padding(5.dp)
            .fillMaxHeight()
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.profile_user),
                contentDescription = "Account User",
                modifier = Modifier
                    .size(40.dp)
                    .padding(horizontal = 5.dp) // Aligning the Image to the center
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
                Text(text = "$currencySymbol${amount}",
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                    color = if(indebt) Color.Red else Color.Green )
            }

        }
    }
}

@Composable
fun CurrencySelectionDialog(
    onDismiss: () -> Unit,
    onCurrencySelected: (CurrencyManager.Currency) -> Unit
) {
    val context = LocalContext.current
    val currentCurrency = remember { CurrencyManager.getCurrentCurrency(context) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Select Currency",
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(CurrencyManager.supportedCurrencies) { currency ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCurrencySelected(currency)
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (currency.code == currentCurrency.code) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = currency.symbol,
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    Text(
                                        text = "${currency.name} (${currency.code})",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                if (currency.code == currentCurrency.code) {
                                    Icon(
                                        painter = painterResource(id = android.R.drawable.ic_menu_recent_history),
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
                
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

