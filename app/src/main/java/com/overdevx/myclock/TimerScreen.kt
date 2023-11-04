package com.overdevx.myclock

import android.content.ContentValues.TAG
import android.nfc.Tag
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
@Composable
fun TimerScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        // call the function Timer
        // and pass the values
        // it is defined below.
        Timer(
            totalTime = 100L * 1000L,
            handleColor = MaterialTheme.colorScheme.primaryContainer,
            inactiveBarColor = Color.DarkGray,
            activeBarColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(200.dp)
        )
    }
}

// create a composable to
// Draw arc and handle
@Composable
fun Timer(

    // total time of the timer
    totalTime: Long,

    // circular handle color
    handleColor: Color,

    // color of inactive bar / progress bar
    inactiveBarColor: Color,

    // color of active bar
    activeBarColor: Color,
    modifier: Modifier = Modifier,

    // set initial value to 1
    initialValue: Float = 1f,
    strokeWidth: Dp = 5.dp
) {
    // create variable for
    // size of the composable
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    // create variable for value
    var value by remember {
        mutableStateOf(initialValue)
    }
    // create variable for current time
    var currentTime by remember {
        mutableStateOf(totalTime)
    }
    // create variable for isTimerRunning
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if(currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .onSizeChanged {
                size = it
            }
    ) {
        // draw the timer
        Canvas(modifier = modifier) {
            // draw the inactive arc with following parameters
            drawArc(
                color = inactiveBarColor, // assign the color
                startAngle = -215f, // assign the start angle
                sweepAngle = 250f, // arc angles
                useCenter = false, // prevents our arc to connect at te ends
                size = Size(size.width.toFloat(), size.height.toFloat()),

                // to make ends of arc round
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // draw the active arc with following parameters
            drawArc(
                color = activeBarColor, // assign the color
                startAngle = -215f,  // assign the start angle
                sweepAngle = 250f * value, // reduce the sweep angle
                // with the current value
                useCenter = false, // prevents our arc to connect at te ends
                size = Size(size.width.toFloat(), size.height.toFloat()),

                // to make ends of arc round
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // calculate the value from arc pointer position
            val center = Offset(size.width / 2f, size.height / 2f)
            val beta = (250f * value + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2f
            val a = cos(beta) * r
            val b = sin(beta) * r
            // draw the circular pointer/ cap
            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round  // make the pointer round
            )
        }
        // add value of the timer
        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        // create button to start or stop the timer
        Button(
            onClick = {
                if(currentTime <= 0L) {
                    currentTime = totalTime
                    isTimerRunning = true
                } else {
                    isTimerRunning = !isTimerRunning
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
            // change button color
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isTimerRunning || currentTime <= 0L) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    Color.Red
                }
            )
        ) {
            Text(
                // change the text of button based on values
                text = if (isTimerRunning && currentTime >= 0L) "Stop"
                else if (!isTimerRunning && currentTime >= 0L) "Start"
                else "Restart"
            )
        }
    }
}
@Composable
fun TimerApp() {
    var timerRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(0L) }
    var countdownTimer: CountDownTimer? by remember { mutableStateOf(null) }

    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatTime(hours, minutes, seconds),
            fontSize = 48.sp
        )

        if (!timerRunning) {
            TimerControls(
                onStartClick = {
                    timerRunning = true
                    timeLeft = (hours * 3600 + minutes * 60 + seconds).toLong()
                    countdownTimer = object : CountDownTimer(timeLeft * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            timeLeft = millisUntilFinished / 1000
                            hours = (timeLeft / 3600).toInt()
                            minutes = ((timeLeft % 3600) / 60).toInt()
                            seconds = (timeLeft % 60).toInt()
                        }

                        override fun onFinish() {
                            timerRunning = false
                            countdownTimer = null
                        }
                    }.start()
                },
                onPauseClick = {
                    countdownTimer?.cancel()
                    timerRunning = false
                }
            )
        } else {
            TimerControls(
                onStopClick = {
                    countdownTimer?.cancel()
                    timerRunning = false
                }
            )
        }
    }
}

@Composable
fun TimerControls(
    onStartClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onStopClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = onStartClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Log.d(TAG,"Tombol Start")
            Text("Start")
        }
        Button(
            onClick = onPauseClick,
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Pause")
        }
        Button(
            onClick = onStopClick,
            modifier = Modifier.padding(16.dp),
        ) {
            Text("Stop")
        }
    }
}

fun formatTime(hours: Int, minutes: Int, seconds: Int): String {
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

