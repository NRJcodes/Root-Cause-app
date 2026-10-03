package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.EnterpriseAccent
import com.example.ui.theme.EnterpriseBorder
import com.example.ui.theme.EnterpriseNavy
import com.example.ui.theme.EnterpriseNavyDark
import com.example.ui.theme.EnterpriseSurface
import com.example.ui.theme.EnterpriseTextSecondary

@Composable
fun AuthDialog(
    currentUser: UserProfile?,
    onLogin: (String, String, String) -> Unit,
    onLogout: () -> Unit,
    onDismiss: () -> Unit
) {
    var email by remember { mutableStateOf(currentUser?.email ?: "lead.analyst@enterprise.com") }
    var password by remember { mutableStateOf("••••••••") }
    var name by remember { mutableStateOf(currentUser?.fullName ?: "Eleanor Vance") }
    var org by remember { mutableStateOf(currentUser?.organization ?: "Global Strategy & Operations") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = EnterpriseSurface,
        shape = RoundedCornerShape(12.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(EnterpriseNavy),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Workspace Authentication",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = EnterpriseNavy
                    )
                    Text(
                        text = "Store & synchronize sessions to your profile",
                        fontSize = 11.sp,
                        color = EnterpriseTextSecondary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Work Email") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = EnterpriseTextSecondary, modifier = Modifier.size(18.dp))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EnterpriseAccent,
                        unfocusedBorderColor = EnterpriseBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("auth_email_input")
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = EnterpriseTextSecondary, modifier = Modifier.size(18.dp))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EnterpriseAccent,
                        unfocusedBorderColor = EnterpriseBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_input")
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Analyst Full Name") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = EnterpriseTextSecondary, modifier = Modifier.size(18.dp))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EnterpriseAccent,
                        unfocusedBorderColor = EnterpriseBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("auth_name_input")
                )

                OutlinedTextField(
                    value = org,
                    onValueChange = { org = it },
                    label = { Text("Business Unit / Organization") },
                    leadingIcon = {
                        Icon(Icons.Default.Business, contentDescription = null, tint = EnterpriseTextSecondary, modifier = Modifier.size(18.dp))
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EnterpriseAccent,
                        unfocusedBorderColor = EnterpriseBorder
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("auth_org_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onLogin(email, name, org)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnterpriseNavy,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("auth_submit_button")
            ) {
                Text("Save & Connect", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Cancel", color = EnterpriseNavy)
            }
        }
    )
}
