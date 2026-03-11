package org.apptolast.menuadmin.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.presentation.theme.BgPrimary
import org.apptolast.menuadmin.presentation.theme.BgSecondary
import org.apptolast.menuadmin.presentation.theme.Blue500
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.Red500
import org.apptolast.menuadmin.presentation.theme.TextMuted
import org.apptolast.menuadmin.presentation.theme.TextPrimary
import org.apptolast.menuadmin.presentation.theme.TextSecondary
import org.apptolast.menuadmin.presentation.theme.TextWhite
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isAuthenticated) {
        onAuthenticated()
        return
    }

    AuthContent(
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onAcceptTermsChange = viewModel::onAcceptTermsChange,
        onToggleMode = viewModel::onToggleMode,
        onLogin = viewModel::onLogin,
        onRegister = viewModel::onRegister,
    )
}

@Composable
fun AuthContent(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onAcceptTermsChange: (Boolean) -> Unit,
    onToggleMode: () -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BgSecondary),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 420.dp)
                .padding(24.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                .background(BgPrimary)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header
            Text(
                text = "AllergenGuard",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Blue500,
            )
            Text(
                text = "Gestión de alérgenos para restaurantes",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tab-like toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(onClick = { if (!uiState.isLoginMode) onToggleMode() }) {
                    Text(
                        text = "Iniciar sesión",
                        fontWeight = if (uiState.isLoginMode) FontWeight.Bold else FontWeight.Normal,
                        color = if (uiState.isLoginMode) Blue500 else TextMuted,
                    )
                }
                TextButton(onClick = { if (uiState.isLoginMode) onToggleMode() }) {
                    Text(
                        text = "Crear cuenta",
                        fontWeight = if (!uiState.isLoginMode) FontWeight.Bold else FontWeight.Normal,
                        color = if (!uiState.isLoginMode) Blue500 else TextMuted,
                    )
                }
            }

            // Email field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            // Password field
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña") },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (passwordVisible) "Ocultar" else "Mostrar",
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
            )

            // Accept terms (register only)
            if (!uiState.isLoginMode) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Checkbox(
                        checked = uiState.acceptTerms,
                        onCheckedChange = onAcceptTermsChange,
                    )
                    Text(
                        text = "Acepto los términos y condiciones",
                        fontSize = 13.sp,
                        color = TextSecondary,
                    )
                }
            }

            // Error message
            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = Red500,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Submit button
            Button(
                onClick = { if (uiState.isLoginMode) onLogin() else onRegister() },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                shape = RoundedCornerShape(12.dp),
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = TextWhite,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(
                        text = if (uiState.isLoginMode) "Iniciar sesión" else "Crear cuenta",
                        fontWeight = FontWeight.SemiBold,
                        color = TextWhite,
                    )
                }
            }

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
                Text(
                    text = "  o  ",
                    fontSize = 12.sp,
                    color = TextMuted,
                )
                HorizontalDivider(modifier = Modifier.weight(1f), color = BorderLight)
            }

            // Google button (placeholder - needs platform-specific Google Sign-In)
            OutlinedButton(
                onClick = { /* Google Sign-In will be wired per-platform */ },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "Continuar con Google",
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewAuthScreenLogin() {
    MenuAdminTheme {
        AuthContent(
            uiState = AuthUiState(isLoginMode = true),
            onEmailChange = {},
            onPasswordChange = {},
            onAcceptTermsChange = {},
            onToggleMode = {},
            onLogin = {},
            onRegister = {},
        )
    }
}

@Preview
@Composable
private fun PreviewAuthScreenRegister() {
    MenuAdminTheme {
        AuthContent(
            uiState = AuthUiState(isLoginMode = false, email = "test@example.com"),
            onEmailChange = {},
            onPasswordChange = {},
            onAcceptTermsChange = {},
            onToggleMode = {},
            onLogin = {},
            onRegister = {},
        )
    }
}

@Preview
@Composable
private fun PreviewAuthScreenError() {
    MenuAdminTheme {
        AuthContent(
            uiState = AuthUiState(
                isLoginMode = true,
                email = "test@example.com",
                error = "Credenciales inválidas",
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onAcceptTermsChange = {},
            onToggleMode = {},
            onLogin = {},
            onRegister = {},
        )
    }
}
