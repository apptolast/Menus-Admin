package org.apptolast.menuadmin.presentation.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.apptolast.menuadmin.domain.model.AllergenType
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
fun ProfileScreen(
    onAccountDeleted: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    ProfileContent(
        uiState = uiState,
        onGrantConsent = viewModel::grantConsent,
        onRevokeConsent = viewModel::revokeConsent,
        onStartEditing = viewModel::startEditing,
        onCancelEditing = viewModel::cancelEditing,
        onToggleAllergen = viewModel::toggleAllergen,
        onSeverityNotesChange = viewModel::onSeverityNotesChange,
        onSave = viewModel::saveProfile,
        onExportData = viewModel::exportData,
        onDismissExportData = viewModel::dismissExportData,
        onShowDeleteConfirm = viewModel::showDeleteConfirm,
        onDismissDeleteConfirm = viewModel::dismissDeleteConfirm,
        onDeleteAccount = { viewModel.deleteAccount(onAccountDeleted) },
        onDismissMessage = viewModel::dismissMessage,
    )
}

@Composable
fun ProfileContent(
    uiState: ProfileUiState,
    onGrantConsent: () -> Unit,
    onRevokeConsent: () -> Unit,
    onStartEditing: () -> Unit,
    onCancelEditing: () -> Unit,
    onToggleAllergen: (AllergenType) -> Unit,
    onSeverityNotesChange: (String) -> Unit,
    onSave: () -> Unit,
    onExportData: () -> Unit,
    onDismissExportData: () -> Unit,
    onShowDeleteConfirm: () -> Unit,
    onDismissDeleteConfirm: () -> Unit,
    onDeleteAccount: () -> Unit,
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
        // Header
        Column {
            Text(
                text = "Mi Perfil",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
            )
            Text(
                text = "Gestiona tu perfil de alergenos y datos personales",
                fontSize = 14.sp,
                color = TextSecondary,
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            return@Column
        }

        // Messages
        uiState.error?.let { error ->
            Text(text = error, color = Red500, fontSize = 13.sp)
        }
        uiState.successMessage?.let { msg ->
            Text(text = msg, color = Green500, fontSize = 13.sp)
        }

        // GDPR Consent Card
        ConsentCard(
            hasConsent = uiState.hasConsent,
            isSaving = uiState.isSaving,
            onGrantConsent = onGrantConsent,
            onRevokeConsent = onRevokeConsent,
        )

        // Allergen Profile Card
        if (uiState.hasConsent) {
            AllergenProfileCard(
                uiState = uiState,
                onStartEditing = onStartEditing,
                onCancelEditing = onCancelEditing,
                onToggleAllergen = onToggleAllergen,
                onSeverityNotesChange = onSeverityNotesChange,
                onSave = onSave,
            )
        }

        // GDPR Actions Card
        GdprActionsCard(
            onExportData = onExportData,
            onShowDeleteConfirm = onShowDeleteConfirm,
        )
    }

    // Export data dialog
    if (uiState.showExportData && uiState.exportedData != null) {
        AlertDialog(
            onDismissRequest = onDismissExportData,
            title = { Text("Datos Exportados") },
            text = {
                Text(
                    text = uiState.exportedData,
                    fontSize = 13.sp,
                    color = TextPrimary,
                )
            },
            confirmButton = {
                TextButton(onClick = onDismissExportData) {
                    Text("Cerrar")
                }
            },
        )
    }

    // Delete confirmation dialog
    if (uiState.showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = onDismissDeleteConfirm,
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Warning,
                    contentDescription = null,
                    tint = Red500,
                )
            },
            title = { Text("Eliminar cuenta") },
            text = {
                Text(
                    text = "Esta accion es irreversible. Se eliminaran todos tus datos personales " +
                        "incluyendo tu perfil de alergenos. No podras recuperar esta cuenta.",
                    fontSize = 14.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = onDeleteAccount,
                    colors = ButtonDefaults.buttonColors(containerColor = Red500),
                ) {
                    Text("Eliminar cuenta", color = TextWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDeleteConfirm) {
                    Text("Cancelar")
                }
            },
        )
    }
}

@Composable
private fun ConsentCard(
    hasConsent: Boolean,
    isSaving: Boolean,
    onGrantConsent: () -> Unit,
    onRevokeConsent: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, BorderLight),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = Blue500,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Consentimiento RGPD",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (hasConsent) {
                    "Has otorgado consentimiento para el tratamiento de tus datos de salud " +
                        "(alergias alimentarias) segun el Art. 9 del RGPD."
                } else {
                    "Para utilizar el perfil de alergenos personalizado, necesitas otorgar " +
                        "consentimiento explicito para el tratamiento de datos de salud " +
                        "(Art. 9 RGPD). Tus datos de alergia son datos de categoria especial."
                },
                fontSize = 14.sp,
                color = TextSecondary,
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (hasConsent) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CheckCircle,
                        contentDescription = null,
                        tint = Green500,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Consentimiento activo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Green500,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    OutlinedButton(
                        onClick = onRevokeConsent,
                        enabled = !isSaving,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Red500.copy(alpha = 0.5f)),
                    ) {
                        Text("Revocar", color = Red500, fontSize = 13.sp)
                    }
                }
            } else {
                Button(
                    onClick = onGrantConsent,
                    enabled = !isSaving,
                    colors = ButtonDefaults.buttonColors(containerColor = Blue500),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = TextWhite,
                            strokeWidth = 2.dp,
                        )
                    } else {
                        Text("Dar consentimiento", color = TextWhite)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AllergenProfileCard(
    uiState: ProfileUiState,
    onStartEditing: () -> Unit,
    onCancelEditing: () -> Unit,
    onToggleAllergen: (AllergenType) -> Unit,
    onSeverityNotesChange: (String) -> Unit,
    onSave: () -> Unit,
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
                Text(
                    text = "Mis Alergenos",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
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

            Spacer(modifier = Modifier.height(12.dp))

            val allergens = if (uiState.isEditing) uiState.editAllergens else uiState.allergenProfile

            if (allergens.isEmpty() && !uiState.isEditing) {
                Text(
                    text = "No has configurado ningun alergeno todavia",
                    fontSize = 14.sp,
                    color = TextSecondary,
                )
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                if (uiState.isEditing) {
                    AllergenType.entries.forEach { allergen ->
                        AllergenChip(
                            allergen = allergen,
                            isSelected = allergen in uiState.editAllergens,
                            onClick = { onToggleAllergen(allergen) },
                        )
                    }
                } else {
                    allergens.forEach { allergen ->
                        AllergenChip(
                            allergen = allergen,
                            isSelected = true,
                            onClick = null,
                        )
                    }
                }
            }

            if (uiState.isEditing) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = uiState.editSeverityNotes,
                    onValueChange = onSeverityNotesChange,
                    label = { Text("Notas sobre severidad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    minLines = 2,
                )
                Spacer(modifier = Modifier.height(12.dp))
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
                        onClick = onCancelEditing,
                        enabled = !uiState.isSaving,
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Cancelar")
                    }
                }
            } else if (uiState.severityNotes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notas: ${uiState.severityNotes}",
                    fontSize = 13.sp,
                    color = TextSecondary,
                )
            }
        }
    }
}

@Composable
private fun AllergenChip(
    allergen: AllergenType,
    isSelected: Boolean,
    onClick: (() -> Unit)?,
) {
    val bgColor = if (isSelected) allergen.color.copy(alpha = 0.15f) else BgCard
    val borderColor = if (isSelected) allergen.color else BorderLight

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .background(bgColor)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = allergen.nameEs,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color = if (isSelected) allergen.color else TextSecondary,
        )
    }
}

@Composable
private fun GdprActionsCard(
    onExportData: () -> Unit,
    onShowDeleteConfirm: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BgCard),
        border = BorderStroke(1.dp, BorderLight),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Derechos RGPD",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tienes derecho a exportar y eliminar tus datos personales " +
                    "segun los Art. 17 y 20 del Reglamento General de Proteccion de Datos.",
                fontSize = 14.sp,
                color = TextSecondary,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onExportData,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Download,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Exportar mis datos")
                }
                OutlinedButton(
                    onClick = onShowDeleteConfirm,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Red500.copy(alpha = 0.5f)),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = Red500,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Eliminar mi cuenta", color = Red500)
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProfileContentPreview() {
    MenuAdminTheme {
        ProfileContent(
            uiState = ProfileUiState(
                isLoading = false,
                hasConsent = true,
                allergenProfile = setOf(
                    AllergenType.GLUTEN,
                    AllergenType.DAIRY,
                    AllergenType.EGGS,
                ),
                severityNotes = "Intolerancia severa al gluten",
            ),
            onGrantConsent = {},
            onRevokeConsent = {},
            onStartEditing = {},
            onCancelEditing = {},
            onToggleAllergen = {},
            onSeverityNotesChange = {},
            onSave = {},
            onExportData = {},
            onDismissExportData = {},
            onShowDeleteConfirm = {},
            onDismissDeleteConfirm = {},
            onDeleteAccount = {},
            onDismissMessage = {},
        )
    }
}

@Preview
@Composable
private fun ProfileContentNoConsentPreview() {
    MenuAdminTheme {
        ProfileContent(
            uiState = ProfileUiState(
                isLoading = false,
                hasConsent = false,
            ),
            onGrantConsent = {},
            onRevokeConsent = {},
            onStartEditing = {},
            onCancelEditing = {},
            onToggleAllergen = {},
            onSeverityNotesChange = {},
            onSave = {},
            onExportData = {},
            onDismissExportData = {},
            onShowDeleteConfirm = {},
            onDismissDeleteConfirm = {},
            onDeleteAccount = {},
            onDismissMessage = {},
        )
    }
}
