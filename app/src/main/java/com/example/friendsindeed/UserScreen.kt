package com.example.friendsindeed

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(username: String?){
    var newname by remember {
        mutableStateOf("")
    }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
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
        Surface(modifier =Modifier
            .padding(padding)
            .fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ){
            if (showBottomSheet){
                ModalBottomSheet(onDismissRequest = { showBottomSheet=false }, sheetState = sheetState) {
                    Column(modifier= Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f),
                    ){
                        Text(
                            text = "Add Transaction",
                            fontSize = 30.sp,
                            modifier = Modifier.padding(20.dp)
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
                        OutlinedButton(onClick = {
                            Toast.makeText(context, "Pls welcome $newname", Toast.LENGTH_SHORT)
                                .show()
                            scope.launch { sheetState.hide() }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                        },
                            modifier = Modifier.padding(20.dp)) {
                            Text(text = "Add User")
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
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)){
        items(100){
            TransactCard()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactCard(){
    val context = LocalContext.current
    val pay = SwipeAction(
            onSwipe = {
                Toast.makeText(context, "Paid", Toast.LENGTH_SHORT).show()
            },
            icon = { Icon(painter = painterResource(id = R.drawable.plus_new), contentDescription ="Savinggg" )},
            background = Color.Green
        )

    SwipeableActionsBox(startActions = listOf(pay)) {
        Card(modifier = Modifier
            .height(70.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth(),
            onClick = {
                Toast.makeText(context, "Hello Motherfuckersssss",
                    Toast.LENGTH_SHORT).show()
            }){
            Row(modifier= Modifier
                .padding(5.dp)
                .fillMaxHeight()
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.up_arrow_outline),
                    contentDescription = "Account User",
                    modifier = Modifier
                        .size(50.dp) // Aligning the Image to the center
                )
                Column(modifier= Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(0.65f)) {
                    Text(text = "Breakfast",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier= Modifier.padding(top = 5.dp))
                    Text(text = "Hello",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White,
                        modifier= Modifier.padding(5.dp))
                }
                Column(verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally){
                    Text(text = "₹${100}",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color.White )
                }

            }
        }
    }


}

