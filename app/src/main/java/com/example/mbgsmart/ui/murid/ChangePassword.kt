package com.example.mbgsmart.ui.murid


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mbgsmart.ui.components.BaseScreenMurid
import com.example.mbgsmart.ui.components.MuridBottomNavBar
import com.example.mbgsmart.ui.theme.*

/* ================= SCREEN ================= */

@Composable
fun ChangePasswordScreen(
    currentScreen: String = "murid_profile",
    onNavigate: (String) -> Unit,
    onSubmit: (String, String) -> Unit = { _, _ -> }
) {

    var oldPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var showOld by remember { mutableStateOf(false) }
    var showNew by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }

    val isValid =
        oldPassword.isNotBlank() &&
                newPassword.length >= 6 &&
                newPassword == confirmPassword

    Scaffold(
        bottomBar = {
            MuridBottomNavBar(
                currentScreen = currentScreen,
                onNavigate = onNavigate
            )
        }
    ) { padding ->

        BaseScreenMurid(
            title = "Ubah Password",
            subtitle = "Pastikan password baru aman dan mudah diingat",
            modifier = Modifier.padding(padding)
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {

                /* PASSWORD LAMA */
                PasswordField(
                    label = "Password Lama",
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    isVisible = showOld,
                    onToggle = { showOld = !showOld }
                )

                /* PASSWORD BARU */
                PasswordField(
                    label = "Password Baru",
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    isVisible = showNew,
                    onToggle = { showNew = !showNew }
                )

                /* KONFIRMASI PASSWORD */
                PasswordField(
                    label = "Konfirmasi Password Baru",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    isVisible = showConfirm,
                    onToggle = { showConfirm = !showConfirm }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        onSubmit(oldPassword, newPassword)
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Simpan Password",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

/* ================= REUSABLE COMPONENT ================= */

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = if (isVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggle) {
                Icon(
                    imageVector = if (isVisible)
                        Icons.Default.Visibility
                    else
                        Icons.Default.VisibilityOff,
                    contentDescription = null
                )
            }
        }
    )
}

/* ================= PREVIEW ================= */

@Preview(showBackground = true)
@Composable
fun PreviewChangePasswordScreen() {
    MBGSmartTheme {
        ChangePasswordScreen(onNavigate = {})
    }
}
