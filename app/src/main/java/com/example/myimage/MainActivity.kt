package com.example.myimage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.myimage.domain.model.NetworkStatus
import com.example.myimage.domain.repo.NetworkConnectivityObserver
import com.example.myimage.presentation.component.NetworkStatusBar
import com.example.myimage.presentation.homes_screen.HomeVideModel
import com.example.myimage.presentation.navigation.NavGraph
import com.example.myimage.presentation.theme.Green
import com.example.myimage.presentation.theme.MyImageTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var connectivityObserver: NetworkConnectivityObserver

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val status by connectivityObserver.networkStatus.collectAsState()
            var showMessageBar by rememberSaveable { mutableStateOf(false) }
            var message by rememberSaveable { mutableStateOf("") }
            var backgroundColor by remember { mutableStateOf(Color.Red) }


            LaunchedEffect(key1 = status) {
                when (status) {
                    NetworkStatus.CONNECTED -> {
                        message = "Connected to Internet"
                        backgroundColor = Green
                        delay(2000)
                        showMessageBar = false
                    }

                    NetworkStatus.DISCONNECTED -> {
                        showMessageBar = true
                        message = "No Internet Connection"
                        backgroundColor = Color.Red
                    }
                }
            }


            MyImageTheme {
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                val navController = rememberNavController()
                val snackBarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection),

                    snackbarHost = { SnackbarHost(hostState = snackBarHostState) },

                    bottomBar = {
                        NetworkStatusBar(
                            modifier = Modifier,
                            message = message,
                            backgroundColor = backgroundColor,
                            isVisible = showMessageBar
                        )
                    }
                ) { ip ->

                    NavGraph(
                        snackBarHostState = snackBarHostState,
                        navController = navController,
                        scrollBehavior = scrollBehavior,
                        connectivityObserver = connectivityObserver
                    )

                }
            }
        }
    }
}
