package com.overdevx.myclock

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring.DampingRatioLowBouncy
import androidx.compose.animation.core.Spring.StiffnessVeryLow
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.overdevx.myclock.ui.theme.MyClockTheme

import androidx.compose.ui.graphics.vector.ImageVector

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.runtime.setValue

import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

import com.overdevx.myclock.data.AlarmViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.Dialog
import com.overdevx.myclock.data.AlarmEntity

import java.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.leanback.widget.picker.TimePicker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

import java.util.Locale


data class BottomNavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null,
)


class AlarmScreen : ComponentActivity() {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyClockTheme {

            }
        }
    }


}


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    alarmViewModel: AlarmViewModel
) {
    val alarms by alarmViewModel.alarms.observeAsState(listOf())
    var newAlarmTitle by remember {
        mutableStateOf("")
    }

    var currentTime = System.currentTimeMillis()
    var newAlarmTime by remember {
        mutableStateOf(System.currentTimeMillis())
    }

    Column(
        modifier = Modifier
            .padding(top = 10.dp)
    ) {


        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Alarm", style = MaterialTheme.typography.displayMedium,modifier = Modifier.align(Alignment.CenterVertically)
                .padding(start = 10.dp))
            IconButton(
                onClick = { navController.navigate("addAlarmScreen") },
                content = {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            )
        }



        val visibleState = remember {
            MutableTransitionState(false).apply {
                // Start the animation immediately.
                targetState = true
            }
        }

        // Fade in entry animation for the entire list
        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(
                animationSpec = spring(dampingRatio = DampingRatioLowBouncy)
            ),
            exit = fadeOut()
        ) {
            LazyColumn {
                items(alarms) { alarm ->
                    var show by remember { mutableStateOf(true) }
                    val dismissState = rememberDismissState(
                        confirmValueChange = {
                            if (it == DismissValue.DismissedToStart || it == DismissValue.DismissedToEnd) {
                                show = false
                                alarmViewModel.deleteAlarm(alarm)
                                false
                            } else true
                        }, positionalThreshold = { 150.dp.toPx() }
                    )
                    val maxSwipeWidth = with(LocalDensity.current) { 80.dp.toPx() }
                    SwipeToDismiss(state = dismissState,
                        background = {
                            // Background content when swiping to dismiss
                            DismissBackground(dismissState)
                        },
                        directions = setOf(DismissDirection.StartToEnd),
                        dismissContent = {
                            AlarmItem(
                                alarmViewModel = alarmViewModel,
                                alarm = alarm, modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .animateEnterExit(
                                        enter = slideInVertically(
                                            animationSpec = spring(
                                                stiffness = StiffnessVeryLow,
                                                dampingRatio = DampingRatioLowBouncy
                                            ),
                                        )
                                    ), onToggleAlarm = {toggledAlarm ->
                                    alarmViewModel.toggleAlarm(toggledAlarm)
                                }
                            )
                        })


                }
            }
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissBackground(dismissState: DismissState) {
    val color =  Color(0xFFFF1744)
    val direction = dismissState.dismissDirection
    if (direction == DismissDirection.StartToEnd) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(color)
                .padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = "delete"
            )
            Spacer(modifier = Modifier)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmItem(alarmViewModel: AlarmViewModel, alarm: AlarmEntity, modifier: Modifier = Modifier,onToggleAlarm: (AlarmEntity) -> Unit) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .sizeIn(minHeight = 72.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "${formatTime(alarm.time)}",
                    style = MaterialTheme.typography.displayLarge
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(MaterialTheme.shapes.small)
            ) {
                val isChecked = remember { mutableStateOf(true) }
                Switch(
                    modifier = Modifier.padding(16.dp),
                    checked = alarm.isEnabled,
                    onCheckedChange = {
                        onToggleAlarm(alarm)
                    },
                    colors = SwitchDefaults.colors(
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }
        }

    }
}

@Composable
fun TimePickerDialogButton(
    modifier: Modifier = Modifier,
    time: Long,
    onTimeChange: (Long) -> Unit
) {
    var isDialogVisible by remember { mutableStateOf(false) }

    // Format the selected time for display
    val selectedTime = remember { mutableStateOf(time) }

    // Create a Text composable to display the selected time
    Text(
        text = "Selected Time: ${formatTime(selectedTime.value)}",
        modifier = modifier.clickable {
            isDialogVisible = true
        }
    )

    // Create a Dialog composable to display the TimePickerDialog
    if (isDialogVisible) {
        Dialog(
            onDismissRequest = {
                isDialogVisible = false
            }
        ) {
            // Initialize a Calendar instance
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = selectedTime.value

            // Create a TimePickerDialog and show it
            TimePickerDialog(
                LocalContext.current,
                { _, hour, minute ->
                    // Update the selected time when the TimePickerDialog is dismissed
                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    val newTime = calendar.timeInMillis
                    onTimeChange(newTime)
                    selectedTime.value = newTime
                    isDialogVisible = false
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }
    }
}

//private fun formatTime(timeInMillis: Long): String {
//    val calendar = Calendar.getInstance()
//    calendar.timeInMillis = timeInMillis
//    val hour = calendar.get(Calendar.HOUR_OF_DAY)
//    val minute = calendar.get(Calendar.MINUTE)
//    return String.format("%02d:%02d", hour, minute)
//}
fun formatTime(time: Long): String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time
    val formatter = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return formatter.format(cal.time)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerExample(paddingValues: PaddingValues, time: Long,modifier: Modifier=Modifier, ontimeSelected: (Long) -> Unit) {
    var showTimePicker by remember { mutableStateOf(false) }
    val state = rememberTimePickerState()
    val formatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    var selectedTime by remember { mutableStateOf(time) } // Menambahkan selectedTime

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues = paddingValues),
        propagateMinConstraints = false,
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = { showTimePicker = true }
        ) {
            Text("Set Time")
        }

        SnackbarHost(hostState = snackState)
    }
    // Menampilkan waktu yang dipilih dalam Text
    Text(
        text = "Selected Time: ${formatter.format(selectedTime)}",
        modifier = Modifier.padding(top = 16.dp),
        fontWeight = FontWeight.Bold
    )
    if (showTimePicker) {
        val content = LocalContext.current
        TimePickerDialog(
            onCancel = { showTimePicker = false },
            onConfirm = {
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, state.hour)
                cal.set(Calendar.MINUTE, state.minute)
                cal.isLenient = false
                snackScope.launch {
                    snackState.showSnackbar(
                        "Entered time: ${formatter.format(cal.time)}"
                    )
                }
                ontimeSelected(cal.time.time) // Panggil callback untuk menyimpan waktu yang dipilih
                selectedTime = cal.time.time
                showTimePicker = false
            },

            ) {
            TimePicker(state = state)
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Set Alarm",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    toggle()
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MyClockTheme {

    }
}

