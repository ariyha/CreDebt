@file:Suppress("NAME_SHADOWING")

package com.example.friendsindeed

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.abs


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(user: String?){
    val user = Gson().fromJson(user, User::class.java)

    val context = LocalContext.current
    val userRepository by lazy { UserRepository( context.applicationContext) }


    val accamount:MutableState<Int> = remember {
        mutableStateOf(0)
    }
    accamount.value =  user.amount


    val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


    var description by remember {
        mutableStateOf("")
    }

    var amount by remember {
        mutableStateOf("")
    }
    var credit by remember {
        mutableStateOf(false)
    }

    var date by remember {
        mutableStateOf(LocalDate.now().toString())
    }

    var hour by remember {
        mutableStateOf(LocalTime.now().hour)
    }

    var minute by remember {
        mutableStateOf(LocalTime.now().minute)
    }

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    val calstate  = UseCaseState()
    val timesate = UseCaseState()


    val oninserttransact :(transaction: Transaction)->Unit={transaction->
        runBlocking(Dispatchers.IO) {
            userRepository.inserttransact(transaction)
        }
    }

    val updateamount:(user:User) -> Unit = { user->
        runBlocking(Dispatchers.IO) {
            userRepository.updateamount(user)
        }
    }


    val transacts = user.uid.let { userRepository.gettransact(it).observeAsState(emptyList()) }


    CalendarDialog(state = calstate, selection =CalendarSelection.Date{
        dates -> date = dates.toString()}
    )

    ClockDialog(state = timesate, selection =ClockSelection.HoursMinutes{
        hours, minutes ->  hour=hours
                            minute=minutes
    } )

    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = { MyApp()  },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { Toast.makeText(context,"Hell bitchesss",Toast.LENGTH_SHORT).show()
                          showBottomSheet=true},
                icon = { Icon(painter = painterResource(id = R.drawable.plus_new), "Extended floating action button.",
                    modifier = Modifier.size(20.dp)) },
                text = { Text(text = "New") },

            )
        }
    ) {padding->
        Surface(modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            if (showBottomSheet){
                ModalBottomSheet(onDismissRequest = { showBottomSheet=false }, sheetState = sheetState) {
                    Column(modifier= Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    ){
                        Row(modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween){
                            Text(
                                text = "Add Transaction",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(20.dp)
                            )
                            Row{
                                Text(
                                    text = "Debt",
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Switch(checked = credit,
                                    onCheckedChange = { credit = it},
                                    thumbContent = {
                                        if (credit) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.down_arrow_filled),
                                                contentDescription = " "
                                            )
                                        } else {
                                            Icon(
                                                painter = painterResource(id = R.drawable.up_arrow_filled),
                                                contentDescription = " "
                                            )
                                        }
                                    }
                                    )
                                Text(
                                    text = "Credit",
                                    fontSize = 15.sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }                        
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description") },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                        )

                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount") },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )


                        Row(modifier=Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly){
                            OutlinedButton(
                                onClick = { calstate.show() },
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(text = date)
                            }

                            OutlinedButton(
                                onClick = { timesate.show() },
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(text = "$hour/$minute")
                            }
                        }

                        OutlinedButton(onClick = {
                            val tid = pref.getInt("tid", 2)
                            pref.edit().putInt("tid", tid+1).apply()
                            oninserttransact(Transaction(
                                tid = tid.toString(),
                                uid = user.uid,
                                description = description,
                                debt = if (credit) "F" else "T",
                                paid = "F",
                                date = date,
                                time = "$hour:$minute",
                                amount = amount.toInt(),
                                name = user.name
                            ))
                            if(credit){
                                accamount.value+=amount.toInt()
                            }
                            else{
                                accamount.value-=amount.toInt()
                            }

                            updateamount(User(
                                uid = user.uid,
                                name = user.name,
                                amount = accamount.value
                            ))

                            description=""
                            amount=""
                            Toast.makeText(context, "Pls welcome $date $hour.$minute", Toast.LENGTH_SHORT)
                                .show()
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                            modifier = Modifier.padding(20.dp)) {
                            Text(text = "Add")
                        }
                    }
                }
            }


            Column(
                Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
                ,){
                UserUpperPanel(user,accamount)
                UserLowerPanel(transacts.value,accamount)

            }
        }

    }
}

@Composable
fun UserUpperPanel(user: User,accamount: MutableState<Int>){

    val name=user.name

    var amount:Int by remember {
        mutableStateOf(0)
    }
    amount=user.amount




    Card(modifier = Modifier
        .clip(RoundedCornerShape(30.dp))
        .padding(10.dp)
        .fillMaxWidth()
        .fillMaxHeight(0.2f)
    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()){
                Text(
                    text = name,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = if(accamount.value>0) "You are owed" else "You owe",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(text = "$ ${abs(accamount.value)}",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White)
        }
    }
}

@Composable
fun UserLowerPanel(transactlist:List<Transaction>,accamount:MutableState<Int>){
    LazyColumn{
        itemsIndexed(transactlist){_,element->
            TransactCard(element,accamount)
        }
    }

}

@Composable
fun TransactCard(element: Transaction,accamount:MutableState<Int>) {
    val context = LocalContext.current
    val userRepository by lazy { UserRepository( context.applicationContext) }

    val pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    val onupdate: (transaction:Transaction) -> Unit = { transaction->
        runBlocking(Dispatchers.IO) {
            userRepository.updatetransact(transaction)
        }
    }

    val updateamount:(user:User) -> Unit = { user->
        runBlocking(Dispatchers.IO) {
            userRepository.updateamount(user)
        }
    }

    var alertdialogval by remember {
        mutableStateOf(false)
    }

    when{
        alertdialogval->{
            AlertDialog(
                onDismissRequest = { alertdialogval = false },
                icon = { Icon(painter = painterResource(id = R.drawable.menu), contentDescription ="some" ) },
                confirmButton = {
                    TextButton(onClick = { alertdialogval = false }) {
                        Text("Confirm")
                    }
                },
                title = { Text(text = "Edit") },
                text = {
                    Text(text = "Do you want to edit or delete? ")
                },
                dismissButton = {
                    TextButton(onClick = {
                        Toast.makeText(context, "Using Button", Toast.LENGTH_SHORT).show()
                        alertdialogval = false
                    }) {
                        Text("Dismiss")
                    }
                },
                )
        }
    }

    var paid by remember {
        mutableStateOf("")
    }
    paid = element.paid

    val pay = SwipeAction(
            onSwipe = {
                if(element.debt=="T") {
                    if (paid=="T") {
                        accamount.value -= element.amount
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = element.description,
                                debt = element.debt,
                                paid = "F",
                                date = element.date,
                                time = element.time,
                                amount = element.amount,
                                name = element.name
                            )
                        )
                    } else {
                        accamount.value += element.amount
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = element.description,
                                debt = element.debt,
                                paid = "T",
                                date = element.date,
                                time = element.time,
                                amount = element.amount,
                                name = element.name
                            )
                        )
                    }
                }
                else{
                    if (paid == "T") {
                        accamount.value += element.amount
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = element.description,
                                debt = element.debt,
                                paid = "F",
                                date = element.date,
                                time = element.time,
                                amount = element.amount,
                                name = element.name
                            )
                        )
                    } else {
                        accamount.value -= element.amount
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = element.description,
                                debt = element.debt,
                                paid = "T",
                                date = element.date,
                                time = element.time,
                                amount = element.amount,
                                name = element.name
                            )
                        )
                    }

                }
                paid = if (paid == "T"){
                    "F"
                } else{
                    "T"
                }
                Toast.makeText(context, "Paid", Toast.LENGTH_SHORT).show()
                updateamount(User(
                    uid = element.uid,
                    name = element.name,
                    amount = accamount.value
                ) )

            },
            icon = { Icon(painter = painterResource(id = R.drawable.pay), contentDescription ="Savinggg",
                modifier = Modifier.padding(10.dp))},
            background = MaterialTheme.colorScheme.secondaryContainer
        )

    SwipeableActionsBox(startActions = listOf(pay)) {
            Card(modifier = Modifier.run {
                height(70.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(0.dp))
            }, colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
                onClick = {
                    alertdialogval = true
                }) {
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxHeight(0.95f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    GetLogo(element = element, paid)
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(0.65f)
                    ) {
                        Text(
                            text = element.description,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                        Text(
                            text = "${element.date} ${element.time}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "₹${element.amount}",
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 2.dp
                )
            }
        }

}

@Composable
fun GetLogo(element: Transaction,paid:String) {
    val iconResourceId = when {
        element.debt == "T" && paid == "T" -> R.drawable.up_arrow_filled
        element.debt == "T" && paid == "F" -> R.drawable.up_arrow_outline
        element.debt == "F" && paid == "T" -> R.drawable.down_arrow_filled
        else -> R.drawable.down_arrow_outline
    }

    Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = "Account User",
        modifier = Modifier.size(40.dp)
    )
}
