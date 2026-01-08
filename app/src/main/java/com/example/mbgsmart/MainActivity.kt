package com.example.mbgsmart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.mbgsmart.ui.navigation.AppNavGraph
import com.example.mbgsmart.ui.theme.MBGSmartTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MBGSmartTheme {
                AppNavGraph()
            }
        }
    }
}
