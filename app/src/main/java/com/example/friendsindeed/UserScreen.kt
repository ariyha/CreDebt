package com.example.friendsindeed

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(username: String?){
    var newname by remember {
        mutableStateOf("")
    }
    var credit by remember {
        mutableStateOf(false)
    }

    var date by remember {
        mutableStateOf(LocalDate.now())
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
    
    CalendarDialog(state = calstate, selection =CalendarSelection.Date{
        dates -> date = dates}
    )

    ClockDialog(state = timesate, selection =ClockSelection.HoursMinutes{
        hours, minutes ->  hour=hours
                            minute=minutes
    } )

    val context = LocalContext.current
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
                            value = newname,
                            onValueChange = { newname = it },
                            label = { Text("Amount") },
                            maxLines = 1,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            value = newname,
                            onValueChange = { newname = it },
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
                                Text(text = "$date")
                            }

                            OutlinedButton(
                                onClick = { timesate.show() },
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(text = "$hour/$minute")
                            }
                        }

                        OutlinedButton(onClick = {
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
                UserUpperPanel(username)
                UserLowerPanel()
            }
        }

    }
}

@Composable
fun UserUpperPanel(username:String?){
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
                text = "$username",
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
            Text(text = "$ 1.00",
                fontSize = 20.sp,
                fontFamily = FontFamily.Monospace,
                color = Color.White)
        }    }
}

@Composable
fun UserLowerPanel(){
    LazyColumn{
        items(100){
            TransactCard()
        }
    }

}

@Preview
@Composable
fun TransactCard(){
    val context = LocalContext.current
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


    val pay = SwipeAction(
            onSwipe = {
                Toast.makeText(context, "Paid", Toast.LENGTH_SHORT).show()
            },
            icon = { Icon(painter = painterResource(id = R.drawable.pay), contentDescription ="Savinggg",
                modifier = Modifier.padding(10.dp))},
            background = MaterialTheme.colorScheme.secondaryContainer
        )

    SwipeableActionsBox(startActions = listOf(pay)) {
        Card(modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp))
            , colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            onClick = {
                alertdialogval=true
            }){
            Row(modifier= Modifier
                .padding(5.dp)
                .fillMaxHeight(0.95f)
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.up_arrow_outline),
                    contentDescription = "Account User",
                    modifier = Modifier
                        .size(40.dp)
                )
                Column(modifier= Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(0.65f)) {
                    Text(text = "Breakfast",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier= Modifier.padding(top = 5.dp))
                    Text(text = "Hello",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier= Modifier.padding(5.dp))
                }
                Column(verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = "₹${100}",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onBackground )
                }

            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onBackground,
                thickness = 2.dp
            )
        }
    }
}

