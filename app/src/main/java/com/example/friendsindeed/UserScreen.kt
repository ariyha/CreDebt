@file:Suppress("NAME_SHADOWING")
@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.friendsindeed

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.rememberUpdatedState
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
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
fun UserScreen(user: String?, navcontroller: NavHostController){
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


    val transacts = user.uid.let { userRepository.gettransact(it).observeAsState(mutableListOf()) }


    CalendarDialog(state = calstate, selection =CalendarSelection.Date(onNegativeClick = {calstate.hide()}){

        dates -> date = dates.toString()
        calstate.hide()},
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )

    ClockDialog(state = timesate, selection =ClockSelection.HoursMinutes(onNegativeClick = {timesate.hide()}){
        hours, minutes ->  hour=hours
                            minute=minutes
        timesate.hide()
    } ,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ))


    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = { MyApp()  },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
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
                                    .height(20.dp)
                            ) {
                                Text(text = hour.toString()+":"+minute.toString())
                            }
                        }

                        OutlinedButton(onClick = {
                            if (amount==""){
                                Toast.makeText(context, "Amount cannot be empty", Toast.LENGTH_SHORT).show()
                                return@OutlinedButton
                            }
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
                Log.d("UserScreen", "UserScreen: ${transacts.value}")
                var transacts1 = transacts.value.toMutableList()
                UserLowerPanel(transacts1,accamount,navcontroller)

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
                Text(text = "₹ ${abs(accamount.value)}",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White)
        }
    }
}

@Composable
fun UserLowerPanel(
    transactlist:MutableList<Transaction>,
    accamount:MutableState<Int>,
    navcontroller: NavHostController
){
    val updatedList = rememberUpdatedState(transactlist)

    LazyColumn{
        itemsIndexed(updatedList.value){_,element->
            TransactCard(element,accamount,navcontroller)
        }
    }

}

@ExperimentalMaterial3Api
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactCard(
    element: Transaction,
    accamount: MutableState<Int>,
    navcontroller: NavHostController
) {

    var credit by remember {
        mutableStateOf(element.debt=="F")
    }

    var editedcredit by remember {
        mutableStateOf(credit)
    }

    var description by remember {
        mutableStateOf(element.description)
    }

    var editeddescription by remember {
        mutableStateOf(description)
    }

    var amount by remember {
        mutableStateOf(element.amount.toString())
    }

    var editedamountn by remember {
        mutableStateOf(amount)
    }

    var time by remember {
        mutableStateOf(element.time)
    }


    var date by remember {
        mutableStateOf(element.date)
    }

    var editeddate by remember {
        mutableStateOf(date)
    }

    var editedtime by remember {
        mutableStateOf(time)
    }


    val calstate  = UseCaseState()
    val timesate = UseCaseState()

    CalendarDialog(state = calstate, selection =CalendarSelection.Date(onNegativeClick ={calstate.hide()} ){
            dates -> editeddate = dates.toString()
            calstate.hide()}
    )

    ClockDialog(state = timesate, selection =ClockSelection.HoursMinutes(onNegativeClick = {timesate.hide()}){
            hours, minutes ->  editedtime = "$hours:$minutes"
            timesate.hide()
    } )



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

    var paid by remember {
        mutableStateOf(element.paid)
    }

    val deletetransact:(transaction:Transaction)->Unit={
        transaction ->
        runBlocking ( Dispatchers.IO ){
            userRepository.deletetransact(transaction)
        }
    }

    var alertdialogval by remember {
        mutableStateOf(false)
    }

    var editdialogval by remember {
        mutableStateOf(false)
    }

    val updatetransact:(transaction:Transaction)->Unit={
            transaction ->
        runBlocking ( Dispatchers.IO ){
            userRepository.updatetransact(transaction)
        }
    }

    when {
        editdialogval->{
            Dialog(onDismissRequest = { editdialogval=false }) {
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)){
                    Column(modifier= Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    ){
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
                                Switch(checked = editedcredit,
                                    onCheckedChange = { editedcredit = it},
                                    thumbContent = {
                                        if (editedcredit) {
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

                        OutlinedTextField(
                            value = editeddescription,
                            onValueChange = { editeddescription = it },
                            label = { Text("Description") },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                        )

                        OutlinedTextField(
                            value = editedamountn,
                            onValueChange = { editedamountn = it },
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
                                Text(text = editeddate)
                            }

                            OutlinedButton(
                                onClick = { timesate.show() },
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(text = editedtime)
                            }
                        }

                        OutlinedButton(onClick = {
                            if(paid=="F"){
                                if(!credit){
                                    if(editedcredit){
                                        accamount.value+=amount.toInt()
                                        accamount.value+=editedamountn.toInt()

                                    }
                                    else{
                                        accamount.value+=amount.toInt()
                                        accamount.value-=editedamountn.toInt()
                                    }
                                }
                                else{
                                    if(editedcredit){
                                        accamount.value-=amount.toInt()
                                        accamount.value+=editedamountn.toInt()
                                    }
                                    else{
                                        accamount.value-=amount.toInt()
                                        accamount.value-=editedamountn.toInt()
                                    }
                                }
                            }
                            updateamount(User(
                                uid = element.uid,
                                name = element.name,
                                amount = accamount.value
                            ))

                            updatetransact(
                                Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = editeddescription,
                                debt = if (editedcredit) "F" else "T",
                                paid = paid,
                                date = editeddate,
                                time = editedtime,
                                amount = editedamountn.toInt(),
                                name = element.name
                            )
                            )

                            description = editeddescription
                            date = editeddate
                            time = editedtime
                            amount = editedamountn
                            credit = editedcredit

                            editdialogval = false
                        },
                            modifier = Modifier.padding(20.dp)) {
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
                    TextButton(onClick = {
                        editdialogval = true
                        alertdialogval = false }) {
                        Text("Edit")
                    }
                },
                title = { Text(text = "Edit") },
                text = {
                    Text(text = "Do you want to edit or delete? ")
                },
                dismissButton = {
                    TextButton(onClick = {
                        if(!credit){
                            if(paid=="F"){
                                accamount.value+=element.amount
                            }
                        }
                        else{
                            if(paid=="F"){
                                accamount.value-=element.amount
                            }
                        }

                        updateamount(User(
                            uid = element.uid,
                            name = element.name,
                            amount = accamount.value
                        ))

                        deletetransact(Transaction(
                            tid = element.tid,
                            uid = element.uid,
                            description = description,
                            debt = if(credit)"F" else "T",
                            paid = "F",
                            date = date,
                            time = time,
                            amount = amount.toInt(),
                            name = element.name
                        )
                        )

                        alertdialogval = false

                        navcontroller.popBackStack()

                        navcontroller.navigate("UserScreen/${Gson().toJson(User(
                            uid = element.uid,
                            name = element.name,
                            amount = accamount.value
                        ))}")

                    }) {
                        Text("Delete")
                    }
                },
                )
        }
    }

    val pay = SwipeAction(
            onSwipe = {
                if(!credit) {
                    if (paid=="T") {
                        accamount.value -= amount.toInt()
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = description,
                                debt = if(credit)"F" else "T",
                                paid = "F",
                                date = date,
                                time = time,
                                amount = amount.toInt(),
                                name = element.name
                            )
                        )
                    } else {
                        accamount.value += amount.toInt()
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = description,
                                debt = if(credit)"F" else "T",
                                paid = "T",
                                date = date,
                                time = time,
                                amount = amount.toInt(),
                                name = element.name
                            )
                        )
                    }
                }
                else{
                    if (paid == "T") {
                        accamount.value += amount.toInt()
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = description,
                                debt = if(credit)"F" else "T",
                                paid = "F",
                                date = date,
                                time = time,
                                amount = amount.toInt(),
                                name = element.name
                            )
                        )
                    } else {
                        accamount.value -= amount.toInt()
                        onupdate(
                            Transaction(
                                tid = element.tid,
                                uid = element.uid,
                                description = description,
                                debt = if(credit)"F" else "T",
                                paid = "T",
                                date = date,
                                time = time,
                                amount = amount.toInt(),
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
                Toast.makeText(context, "Paid ", Toast.LENGTH_SHORT).show()
                updateamount(User(
                    uid = element.uid,
                    name = element.name,
                    amount = accamount.value
                ) )

            },
            icon = { Icon(painter = painterResource(id = R.drawable.pay), contentDescription ="Savinggg",
                modifier = Modifier.padding(10.dp))},
            background = MaterialTheme.colorScheme.primaryContainer
        )

    SwipeableActionsBox(swipeThreshold = 200.dp, startActions = listOf(pay)) {
            Card(modifier = Modifier
                .height(70.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(0.dp))
                .combinedClickable(
                    onClick = {

                    },
                    onLongClick = {
                        alertdialogval = true
                    }
                ), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
                ) {
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxHeight(0.95f)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var debt = { if(credit) "F" else "T" }
                    GetLogo(debt(), paid)
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth(0.65f)
                    ) {
                        Text(
                            text = description,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 5.dp)
                        )
                        Text(
                            text = "${date} ${time}",
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
                            text = "₹${amount}",
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
fun GetLogo(debt: String,paid:String) {
    val iconResourceId = when {
        debt == "T" && paid == "T" -> R.drawable.up_arrow_filled
        debt == "T" && paid == "F" -> R.drawable.up_arrow_outline
        debt == "F" && paid == "T" -> R.drawable.down_arrow_filled
        else -> R.drawable.down_arrow_outline
    }

    Icon(
        painter = painterResource(id = iconResourceId),
        contentDescription = "Account User",
        modifier = Modifier.size(40.dp)
    )
}
