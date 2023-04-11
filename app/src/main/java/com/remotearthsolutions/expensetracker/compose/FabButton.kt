package com.remotearthsolutions.expensetracker.compose

import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults.elevation
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.remotearthsolutions.expensetracker.compose.theme.colorAccent

@Composable
fun FabButton(onClick: () -> Unit, modifier: Modifier, contentDescription: String = "") {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        elevation = elevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        contentColor = Color.White,
        backgroundColor = colorAccent,
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = contentDescription)
    }
}
