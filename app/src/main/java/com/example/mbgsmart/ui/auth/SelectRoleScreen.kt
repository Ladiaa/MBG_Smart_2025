package com.example.mbgsmart.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectRoleScreen(
    onSelectSekolah: () -> Unit,
    onSelectMurid: () -> Unit,
    onSelectAdmin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Masuk sebagai",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSelectSekolah,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Sekolah")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSelectMurid,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Murid")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSelectAdmin,
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Admin")
        }
    }
}
