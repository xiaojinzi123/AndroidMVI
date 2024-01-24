package com.xiaojinzi.androidmvi.demo.module.login.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojinzi.androidmvi.demo.module.login.domain.LoginIntent
import com.xiaojinzi.androidmvi.demo.ui.theme.AndroidMVITheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: LoginViewModel = viewModel()
            val name by vm.nameStateOb.collectAsState(initial = "")
            AndroidMVITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        OutlinedTextField(
                            value = name,
                            onValueChange = {
                                vm.nameStateOb.value = it
                                /*vm.addIntent(
                                    intent = LoginIntent.NameChanged(
                                        name = it,
                                    )
                                )*/
                            }
                        )

                    }

                }
            }
        }
    }

}

