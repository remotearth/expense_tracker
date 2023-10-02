package com.remotearthsolutions.expensetracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.viewmodels.LoginViewModel

@Composable
fun LoginView(viewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painterResource(R.mipmap.ic_logo_layer), "")
        Text(
            stringResource(R.string.welcometitle),
            modifier = Modifier.padding(top = 20.dp),
            style = TextStyle(
                color = colorResource(id = R.color.colorPrimary),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        OutlinedButton(
            onClick = { viewModel.startGoogleLogin() }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 65.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "google login",
            )
            Text(
                stringResource(id = R.string.gmail_signin_text),
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)

            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(id = R.string.noaccounttext),
            color = colorResource(id = R.color.Ashcolor),
        )
        Button(
            onClick = { viewModel.startGuestLogin() }, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 65.dp),
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.colorPrimary))


        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_withoutlogin),
                contentDescription = "guest login",
            )
            Text(
                stringResource(id = R.string.signin_without_login),
                color = colorResource(id = R.color.textcolorwhite),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)

            )
        }
    }

}