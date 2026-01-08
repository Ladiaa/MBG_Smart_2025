package com.example.mbgsmart.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbgsmart.ui.theme.BrandDarkBlue

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected)
                BrandDarkBlue
            else
                MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (selected)
                Color.White
            else
                Color.DarkGray
        ),
        modifier = Modifier.height(36.dp)
    ) {
        Text(text, fontSize = 12.sp)
    }
}
