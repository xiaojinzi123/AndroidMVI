package com.xiaojinzi.androidmvi.demo.module.login.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojinzi.androidmvi.demo.module.login.domain.LoginIntent
import com.xiaojinzi.androidmvi.demo.ui.theme.AndroidMVITheme
import com.xiaojinzi.mvi.template.domain.BusinessContentView

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val vm: LoginViewModel = viewModel()
            val name by vm.nameStateOb.collectAsState(initial = "")
            val password by vm.passwordStateOb.collectAsState(initial = "")
            val canSubmit by vm.canSubmitStateOb.collectAsState(initial = false)
            AndroidMVITheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    BusinessContentView<LoginViewModel> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .padding(horizontal = 38.dp, vertical = 0.dp)
                        ) {

                            Spacer(
                                modifier = Modifier
                                    .height(height = 24.dp)
                            )

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                value = name,
                                onValueChange = {
                                    vm.nameStateOb.value = it
                                }
                            )

                            Spacer(
                                modifier = Modifier
                                    .height(height = 24.dp)
                            )

                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                value = password,
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Password,
                                ),
                                onValueChange = {
                                    vm.passwordStateOb.value = it
                                }
                            )

                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(horizontal = 0.dp, vertical = 26.dp),
                                enabled = canSubmit,
                                onClick = {
                                    vm.addIntent(
                                        intent = LoginIntent.Login(
                                            context = context,
                                        )
                                    )
                                }) {
                                Text(text = "登录")
                            }

                        }
                    }
                }
            }
        }
    }

}

