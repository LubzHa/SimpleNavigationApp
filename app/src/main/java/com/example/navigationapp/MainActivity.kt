package com.example.navigationapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import com.example.navigationapp.ui.theme.NavigationAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    private val viewModel by viewModels<NavigationViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.statusMessage.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
        setContent {
            NavigationAppTheme {
                NavigationScreen(viewModel = viewModel)
            }
        }
    }
}