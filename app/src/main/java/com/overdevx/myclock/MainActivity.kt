package com.overdevx.myclock


import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.AlarmOn
import androidx.compose.material.icons.filled.PunchClock
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.PunchClock
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.overdevx.myclock.ui.theme.MyClockTheme
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.overdevx.myclock.data.AlarmViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val alarmViewModel = AlarmViewModel(application)
            MyClockTheme {

                // A surface container using the 'background' color from the theme
                val items = listOf(
                    BottomNavigationItem(
                        route = "alarm",
                        title = "Alarm",
                        selectedIcon = Icons.Filled.AlarmOn,
                        unselectedIcon = Icons.Outlined.Alarm,
                        hasNews = false,
                        badgeCount = 50
                    ),
                    BottomNavigationItem(
                        route = "timer",
                        title = "Timer",
                        selectedIcon = Icons.Filled.Timer,
                        unselectedIcon = Icons.Outlined.Timer,
                        hasNews = true,
                    ),
                    BottomNavigationItem(
                        route = "clock",
                        title = "Clock",
                        selectedIcon = Icons.Filled.PunchClock,
                        unselectedIcon = Icons.Outlined.PunchClock,
                        hasNews = false,
                    )
                )
                // A surface container using the 'background' color from the theme
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                items.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            navController.navigate(item.title)
                                        },
                                        label = {
                                            Text(text = item.title)
                                        },
                                        icon = {
                                            BadgedBox(badge = {
                                                if (item.badgeCount != null) {
                                                    Badge {
                                                        Text(text = item.badgeCount.toString())
                                                    }
                                                } else if (item.hasNews) {
                                                    Badge()
                                                }
                                            }) {
                                                Icon(
                                                    imageVector = if (index == selectedItemIndex) {
                                                        item.selectedIcon
                                                    } else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            }
                                        })
                                }
                            }
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            NavHost(navController = navController, startDestination = "alarm") {
                                composable("alarm") {
                                    AlarmScreen(
                                        navController = navController,
                                        alarmViewModel = alarmViewModel
                                    )
                                }
                                composable("timer") {
                                    TimerScreen(navController)
                                }
                                composable("clock") {
                                    ClockScreen(navController)
                                }
                                composable("addAlarmScreen") {
                                    AddAlarmScreen(navController, alarmViewModel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}


@Composable
fun AlarmApp(alarmViewModel: AlarmViewModel) {
    MaterialTheme {

    }
}

@Composable
fun App(alarmViewModel: AlarmViewModel) {
    AlarmApp(alarmViewModel)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyClockTheme {

    }
}