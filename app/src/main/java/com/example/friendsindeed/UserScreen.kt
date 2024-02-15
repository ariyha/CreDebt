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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(uid: String?){
    val context = LocalContext.current
    val userRepository by lazy { UserRepository( context.applicationContext) }


    var accamount:Int by remember {
        mutableStateOf(0)
    }

    val getAmount :(uid:String)->Int={uid->
        runBlocking(Dispatchers.IO) {
            userRepository.getamount(uid)
        }
    }

    accamount =  getAmount(uid.toString())


    var pref = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


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

    val updateamount:(uid:String, amount:Int) -> Unit = { uid,amount->
        runBlocking(Dispatchers.IO) {
            userRepository.updateamount(uid,amount)
        }
    }


    val transacts = uid?.let { userRepository.gettransact(it).observeAsState(emptyList()) }


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
                                Text(text = date.toString())
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
                                uid = uid!!,
                                description = description,
                                debt = if (credit) "F" else "T",
                                paid = "F",
                                date = date,
                                time = "$hour:$minute",
                                amount = amount.toInt()
                            ))
                            if(credit){
                                accamount+=amount.toInt()
                            }
                            else{
                                accamount-=amount.toInt()
                            }

                            updateamount(uid,accamount)

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
                UserUpperPanel(uid)
                if (transacts != null) {
                    UserLowerPanel(transacts.value)
                }
            }
        }

    }
}

@Composable
fun UserUpperPanel(uid:String?){
    val context = LocalContext.current
    val userRepository by lazy { UserRepository( context.applicationContext) }
    val getAmount :(uid:String)->Int={uid->
        runBlocking(Dispatchers.IO) {
            userRepository.getamount(uid)
        }
    }

    var name=""
    val users = userRepository.getall().observeAsState(emptyList()).value
    for(element in users){
        if (element.uid.equals(uid)){
            name=element.name
            break
        }
    }

    var amount:Int by remember {
        mutableStateOf(0)
    }
    amount=getAmount(uid.toString())





    Card(modifier = Modifier
        .clip(RoundedCornerShape(30.dp))
        .padding(10.dp)
        .fillMaxWidth()
        .fillMaxHeight(0.2f),

    )
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()){
                Text(
                    text = name,
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White,
                    modifier = Modifier.padding(10.dp)
                )
                Text(
                    text = "Owes You",
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White
                )
                Text(text = "$ ${amount}",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color.White)
        }
    }
}

@Composable
fun UserLowerPanel(transactlist:List<Transaction>){
    LazyColumn{
        itemsIndexed(transactlist){_,element->
            TransactCard(element)
        }
    }

}

@Composable
fun TransactCard(element: Transaction) {
    val context = LocalContext.current
    val userRepository by lazy { UserRepository( context.applicationContext) }

    val getAmount :(uid:String)->Int={uid->
        runBlocking(Dispatchers.IO) {
            userRepository.getamount(uid)
        }
    }

    var amount by remember { mutableStateOf(0) }

    amount = getAmount(element.uid)

    val onupdate: (uid: String,valu:String) -> Unit = { uid,valu ->
        runBlocking(Dispatchers.IO) {
            userRepository.updatepaid(uid,valu)
        }
    }

    val updateamount:(uid:String, amount:Int) -> Unit = { uid,amount->
        runBlocking(Dispatchers.IO) {
            userRepository.updateamount(uid,amount)
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
                Toast.makeText(context, "Paid", Toast.LENGTH_SHORT).show()

                if(element.paid.equals("T")){
                    amount+=element.amount
                    onupdate(element.tid,"F")
                }
                else{
                    amount-=element.amount
                    onupdate(element.tid,"T")
                }
                if (paid.equals("T")){
                    paid="F"
                }
                else{
                    paid="T"
                }
                updateamount(element.uid,amount)
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
                    getLogo(element = element, paid)
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
fun getLogo(element: Transaction,paid:String) {
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
