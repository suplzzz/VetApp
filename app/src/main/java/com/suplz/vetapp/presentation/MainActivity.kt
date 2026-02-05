package com.suplz.vetapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.suplz.vetapp.presentation.navigation.NavGraph
import com.suplz.vetapp.presentation.ui.theme.VetAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VetAppTheme {
                NavGraph()
            }
        }
    }
}