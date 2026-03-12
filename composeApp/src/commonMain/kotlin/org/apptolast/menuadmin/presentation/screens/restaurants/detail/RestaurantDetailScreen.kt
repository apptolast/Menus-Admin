package org.apptolast.menuadmin.presentation.screens.restaurants.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Publish
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Smartphone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import org.apptolast.menuadmin.presentation.components.StatCard
import org.apptolast.menuadmin.presentation.theme.Amber500
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
fun RestaurantDetailScreen(
    onBack: () -> Unit,
    onNavigateToRecipes: (String) -> Unit,
    onNavigateToMenus: (String) -> Unit,
    onNavigateToCarta: (String) -> Unit,
    viewModel: RestaurantDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    RestaurantDetailContent(
        uiState = uiState,
        onBack = onBack,
        onNavigateToRecipes = onNavigateToRecipes,
        onNavigateToMenus = onNavigateToMenus,
        onNavigateToCarta = onNavigateToCarta,
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RestaurantDetailContent(
    uiState: RestaurantDetailUiState,
    onBack: () -> Unit,
    onNavigateToRecipes: (String) -> Unit,
    onNavigateToMenus: (String) -> Unit,
    onNavigateToCarta: (String) -> Unit,
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
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = Blue500,
                strokeWidth = 3.dp,
            )
        }
        return
    }

    val restaurant = uiState.restaurant
    if (restaurant == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = uiState.error ?: "Restaurante no encontrado",
                fontSize = 16.sp,
                color = TextSecondary,
            )
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Header with back button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextPrimary,
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = restaurant.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                )
                Text(
                    text = restaurant.slug,
                    fontSize = 14.sp,
                    color = TextSecondary,
                )
            }
            if (!uiState.isEditing) {
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

        // Messages
        uiState.error?.let { error ->
            Text(text = error, color = Red500, fontSize = 13.sp)
        }
        uiState.successMessage?.let { msg ->
            Text(text = msg, color = Green500, fontSize = 13.sp)
        }

        // Stats row
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            StatCard(
                label = "Recetas",
                value = uiState.recipesCount.toString(),
                icon = Icons.Outlined.Fastfood,
                iconTint = Green500,
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = "Menus",
                value = uiState.menusCount.toString(),
                icon = Icons.AutoMirrored.Outlined.MenuBook,
                iconTint = Amber500,
                modifier = Modifier.weight(1f),
            )
            StatCard(
                label = "Publicados",
                value = uiState.publishedMenusCount.toString(),
                icon = Icons.Outlined.Publish,
                iconTint = Blue500,
                modifier = Modifier.weight(1f),
            )
        }

        // Restaurant info / edit card
        if (uiState.isEditing) {
            RestaurantEditCard(
                uiState = uiState,
                onNameChange = onNameChange,
                onDescriptionChange = onDescriptionChange,
                onAddressChange = onAddressChange,
                onPhoneChange = onPhoneChange,
                onSave = onSave,
                onCancel = onCancelEditing,
            )
        } else {
            RestaurantInfoCard(restaurant = restaurant)
        }

        // Quick navigation buttons
        Text(
            text = "Acceso rapido",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextPrimary,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(
                onClick = { onNavigateToRecipes(restaurant.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Green500),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Restaurant,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Recetas", color = TextWhite, fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = { onNavigateToMenus(restaurant.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Amber500),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.MenuBook,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Menus", color = TextWhite, fontWeight = FontWeight.SemiBold)
            }
            Button(
                onClick = { onNavigateToCarta(restaurant.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                shape = RoundedCornerShape(8.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.Smartphone,
                    contentDescription = null,
                    tint = TextWhite,
                    modifier = Modifier.size(18.dp),
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Carta Digital", color = TextWhite, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun RestaurantInfoCard(
    restaurant: Restaurant,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, BorderLight),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Informacion del restaurante",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (restaurant.description.isNotEmpty()) {
                Text(
                    text = restaurant.description,
                    fontSize = 14.sp,
                    color = TextPrimary,
                )
            }
            if (restaurant.address.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.LocationOn,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = restaurant.address,
                        fontSize = 14.sp,
                        color = TextSecondary,
                    )
                }
            }
            if (restaurant.phone.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(
                        text = restaurant.phone,
                        fontSize = 14.sp,
                        color = TextSecondary,
                    )
                }
            }
            Text(
                text = if (restaurant.active) "Activo" else "Inactivo",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (restaurant.active) Green500 else Red500,
            )
        }
    }
}

@Composable
private fun RestaurantEditCard(
    uiState: RestaurantDetailUiState,
    onNameChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, BorderLight),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Editar restaurante",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )
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
}

@Preview
@Composable
private fun PreviewRestaurantDetailContent() {
    MenuAdminTheme {
        RestaurantDetailContent(
            uiState = RestaurantDetailUiState(
                isLoading = false,
                restaurant = Restaurant(
                    id = "1",
                    name = "Hotel Palace Barcelona",
                    slug = "hotel-palace-barcelona",
                    description = "Restaurante de cocina mediterranea con vistas al mar",
                    address = "Paseo de Gracia 123, Barcelona",
                    phone = "+34 934 567 890",
                    active = true,
                ),
                recipesCount = 12,
                menusCount = 3,
                publishedMenusCount = 2,
            ),
            onBack = {},
            onNavigateToRecipes = {},
            onNavigateToMenus = {},
            onNavigateToCarta = {},
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
private fun PreviewRestaurantDetailContentEditing() {
    MenuAdminTheme {
        RestaurantDetailContent(
            uiState = RestaurantDetailUiState(
                isLoading = false,
                isEditing = true,
                restaurant = Restaurant(
                    id = "1",
                    name = "Hotel Palace Barcelona",
                    slug = "hotel-palace-barcelona",
                    description = "Restaurante de cocina mediterranea",
                    address = "Paseo de Gracia 123, Barcelona",
                    phone = "+34 934 567 890",
                    active = true,
                ),
                editName = "Hotel Palace Barcelona",
                editDescription = "Restaurante de cocina mediterranea",
                editAddress = "Paseo de Gracia 123, Barcelona",
                editPhone = "+34 934 567 890",
                recipesCount = 12,
                menusCount = 3,
                publishedMenusCount = 2,
            ),
            onBack = {},
            onNavigateToRecipes = {},
            onNavigateToMenus = {},
            onNavigateToCarta = {},
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
