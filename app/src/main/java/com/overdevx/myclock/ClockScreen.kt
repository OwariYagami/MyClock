package com.overdevx.myclock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.database.Cursor
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.TextClock
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import java.io.File
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(
        // in scaffold we are specifying top bar.
        topBar = {
            // inside top bar we are specifying background color.
            TopAppBar(
                // along with that we are specifying title for our top bar.
                title = {
                    // in the top bar we are specifying tile as a text
                })
        }) {
        // on below line we are calling
        // display text clock function.
        displayTxtClock()
    }
}
@Composable
fun displayTxtClock() {
    // on below line we are creating
    // a column on below sides.
    Column(
        // on below line we are adding padding
        // padding for our column and filling the max size.
        Modifier
            .fillMaxSize()
            .fillMaxHeight()
            .fillMaxWidth(),
        // on below line we are adding
        // horizontal alignment for our column
        horizontalAlignment = Alignment.CenterHorizontally,
        // on below line we are adding
        // vertical arrangement for our column
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            // on below line we are specifying text
            // to display in top app bar.
            text = "Clock ",

            // on below line we are specifying
            // modifier to fill max width.
            modifier = Modifier.fillMaxWidth(),

            // on below line we are
            // specifying text alignment.
            textAlign = TextAlign.Center,

            // on below line we are
            // specifying color for our text.
            color = MaterialTheme.colorScheme.tertiary,

            // on below line we are
            // updating font size.
            style = MaterialTheme.typography.displayLarge
        )

        // on below line we are creating a spacer.
        Spacer(modifier = Modifier.height(20.dp))

        // on below line we are creating a text clock.
        AndroidView(
            // on below line we are initializing our text clock.
            factory = { context ->
                TextClock(context).apply {
                    // on below line we are setting 12 hour format.
                    format12Hour?.let { this.format12Hour = "hh:mm:ss a" }
                    // on below line we are setting time zone.
                    timeZone?.let { this.timeZone = it }
                    // on below line we are setting text size.
                    textSize.let { this.textSize = 60f }
                    setTextColor(ContextCompat.getColor(context, R.color.white))


                }
            },
            // on below line we are adding padding.
            modifier = Modifier.padding(5.dp)
        )
    }
}