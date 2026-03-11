package org.apptolast.menuadmin.presentation.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.domain.model.Restaurant
import org.apptolast.menuadmin.presentation.theme.BgCard
import org.apptolast.menuadmin.presentation.theme.Blue500
import org.apptolast.menuadmin.presentation.theme.BorderLight
import org.apptolast.menuadmin.presentation.theme.Green500
import org.apptolast.menuadmin.presentation.theme.MenuAdminTheme
import org.apptolast.menuadmin.presentation.theme.Red500
import org.apptolast.menuadmin.presentation.theme.TextPrimary
import org.apptolast.menuadmin.presentation.theme.TextSecondary
import org.apptolast.menuadmin.presentation.theme.TextWhite
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    SettingsContent(
        uiState = uiState,
        onStartEditing = viewModel::startEditing,
        onCancelEditing = viewModel::cancelEditing,
        onSave = viewModel::saveRestaurant,
        onNameChange = viewModel::onNameChange,
        onDescriptionChange = viewModel::onDescriptionChange,
        onAddressChange = viewModel::onAddressChange,
        onPhoneChange = viewModel::onPhoneChange,
        onDismissMessage = viewModel::dismissMessage,
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onStartEditing: () -> Unit,
    onCancelEditing: () -> Unit,
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onDismissMessage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column {
            Text(
                text = "Configuracion",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            Text(
                text = "Personaliza tu experiencia",
                fontSize = 14.sp,
                color = TextSecondary,
            )
        }

        // Restaurant info card
        RestaurantCard(
            uiState = uiState,
            onStartEditing = onStartEditing,
            onCancelEditing = onCancelEditing,
            onSave = onSave,
            onNameChange = onNameChange,
            onDescriptionChange = onDescriptionChange,
            onAddressChange = onAddressChange,
            onPhoneChange = onPhoneChange,
        )

        // Messages
        uiState.error?.let { error ->
            Text(text = error, color = Red500, fontSize = 13.sp)
        }
        uiState.successMessage?.let { msg ->
            Text(text = msg, color = Green500, fontSize = 13.sp)
        }

        // App settings card
        AppSettingsCard()
    }
}

@Composable
private fun RestaurantCard(
    uiState: SettingsUiState,
    onStartEditing: () -> Unit,
    onCancelEditing: () -> Unit,
    onSave: () -> Unit,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, BorderLight),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.Business,
                        contentDescription = null,
                        tint = Blue500,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mi Restaurante",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary,
                    )
                }
                if (!uiState.isEditing && uiState.restaurant != null) {
                    OutlinedButton(
                        onClick = onStartEditing,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Editar",
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp).align(Alignment.CenterHorizontally),
                    strokeWidth = 2.dp,
                )
            } else if (uiState.restaurant == null) {
                Text(
                    text = "No se pudo cargar la informacion del restaurante",
                    fontSize = 14.sp,
                    color = TextSecondary,
                )
            } else if (uiState.isEditing) {
                RestaurantEditForm(
                    uiState = uiState,
                    onNameChange = onNameChange,
                    onDescriptionChange = onDescriptionChange,
                    onAddressChange = onAddressChange,
                    onPhoneChange = onPhoneChange,
                    onSave = onSave,
                    onCancel = onCancelEditing,
                )
            } else {
                RestaurantInfo(restaurant = uiState.restaurant)
            }
        }
    }
}

@Composable
private fun RestaurantInfo(restaurant: Restaurant) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow(label = "Nombre", value = restaurant.name)
        InfoRow(label = "Slug", value = restaurant.slug)
        if (restaurant.description.isNotEmpty()) {
            InfoRow(label = "Descripcion", value = restaurant.description)
        }
        if (restaurant.address.isNotEmpty()) {
            InfoRow(label = "Direccion", value = restaurant.address)
        }
        if (restaurant.phone.isNotEmpty()) {
            InfoRow(label = "Telefono", value = restaurant.phone)
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary,
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary,
        )
    }
}

@Composable
private fun RestaurantEditForm(
    uiState: SettingsUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(
            value = uiState.editName,
            onValueChange = onNameChange,
            label = { Text("Nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        )
        OutlinedTextField(
            value = uiState.editDescription,
            onValueChange = onDescriptionChange,
            label = { Text("Descripcion") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            minLines = 2,
        )
        OutlinedTextField(
            value = uiState.editAddress,
            onValueChange = onAddressChange,
            label = { Text("Direccion") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        )
        OutlinedTextField(
            value = uiState.editPhone,
            onValueChange = onPhoneChange,
            label = { Text("Telefono") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onSave,
                enabled = !uiState.isSaving,
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                shape = RoundedCornerShape(8.dp),
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = TextWhite,
                        strokeWidth = 2.dp,
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Guardar", color = TextWhite)
                }
            }
            OutlinedButton(
                onClick = onCancel,
                enabled = !uiState.isSaving,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Cancelar")
            }
        }
    }
}

@Composable
private fun AppSettingsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, BorderLight),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Idioma",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                )
                Text(
                    text = "Espanol",
                    fontSize = 14.sp,
                    color = Blue500,
                    fontWeight = FontWeight.Medium,
                )
            }

            HorizontalDivider(color = BorderLight)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Tema oscuro",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                )
                Switch(
                    checked = false,
                    onCheckedChange = null,
                    enabled = false,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Blue500,
                    ),
                )
            }

            HorizontalDivider(color = BorderLight)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Notificaciones",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                )
                Switch(
                    checked = false,
                    onCheckedChange = null,
                    enabled = false,
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Blue500,
                    ),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsContentPreview() {
    MenuAdminTheme {
        SettingsContent(
            uiState = SettingsUiState(
                isLoading = false,
                restaurant = Restaurant(
                    id = "1",
                    name = "Hotel Palace Barcelona",
                    slug = "hotel-palace-barcelona",
                    description = "Restaurante de cocina mediterranea",
                    address = "Paseo de Gracia 123, Barcelona",
                    phone = "+34 934 567 890",
                    logoUrl = null,
                    active = true,
                ),
            ),
            onStartEditing = {},
            onCancelEditing = {},
            onSave = {},
            onNameChange = {},
            onDescriptionChange = {},
            onAddressChange = {},
            onPhoneChange = {},
            onDismissMessage = {},
        )
    }
}

@Preview
@Composable
private fun SettingsContentEditingPreview() {
    MenuAdminTheme {
        SettingsContent(
            uiState = SettingsUiState(
                isLoading = false,
                isEditing = true,
                restaurant = Restaurant(
                    id = "1",
                    name = "Hotel Palace Barcelona",
                    slug = "hotel-palace-barcelona",
                    description = "Restaurante de cocina mediterranea",
                    address = "Paseo de Gracia 123, Barcelona",
                    phone = "+34 934 567 890",
                    logoUrl = null,
                    active = true,
                ),
                editName = "Hotel Palace Barcelona",
                editDescription = "Restaurante de cocina mediterranea",
                editAddress = "Paseo de Gracia 123, Barcelona",
                editPhone = "+34 934 567 890",
            ),
            onStartEditing = {},
            onCancelEditing = {},
            onSave = {},
            onNameChange = {},
            onDescriptionChange = {},
            onAddressChange = {},
            onPhoneChange = {},
            onDismissMessage = {},
        )
    }
}
