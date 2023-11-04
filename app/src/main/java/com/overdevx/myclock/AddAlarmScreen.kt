package com.overdevx.myclock

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.overdevx.myclock.data.AlarmViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmScreen(navController: NavHostController, alarmViewModel: AlarmViewModel,modifier: Modifier= Modifier) {
    var newAlarmTitle by remember { mutableStateOf("") }
    var newAlarmTime by remember { mutableStateOf(System.currentTimeMillis()) }

    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Add New Alarm",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            TextField(
                value = newAlarmTitle,
                onValueChange = { newAlarmTitle = it },
                label = { Text("Alarm Title") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
            TimePickerExample(
                paddingValues = PaddingValues(
                    start = 16.dp,
                    top = 8.dp,
                    end = 16.dp,
                    bottom = 8.dp
                ),
                time = newAlarmTime,
                ontimeSelected = { newTime -> newAlarmTime = newTime },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (newAlarmTitle.isNotEmpty()) {
                        alarmViewModel.addAlarm(newAlarmTitle, newAlarmTime)
                        navController.navigateUp()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Add Alarm")
            }
        }
    }

}