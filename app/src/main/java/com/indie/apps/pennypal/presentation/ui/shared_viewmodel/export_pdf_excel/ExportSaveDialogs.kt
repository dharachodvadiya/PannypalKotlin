package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel

import android.content.Context
import android.content.Intent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.FileProvider
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomProgressDialog
import com.indie.apps.pennypal.presentation.ui.component.composable.custom.CustomText
import com.indie.apps.pennypal.presentation.ui.theme.MyAppTheme
import com.indie.apps.pennypal.util.app_enum.ExportType
import java.io.File

@Composable
fun ExportSaveDialogs(
    exportType: ExportType?,
    exportResult: ExportResult?,
    onClearResult: () -> Unit
) {
    val context = LocalContext.current
    when (exportResult) {
        is ExportResult.Loading -> {
            CustomProgressDialog(
                message = R.string.loading
            )
        }

        is ExportResult.Success -> {

            val dialogText = when (exportType) {
                ExportType.Pdf -> stringResource(R.string.pdf_save_to)
                ExportType.Excel -> stringResource(R.string.excel_save_to)
                null -> stringResource(R.string.pdf_save_to)
            } + " ${exportResult.filePath}"

            ExportDialog(
                onDismissRequest = { onClearResult() },
                dialogTitle = R.string.pdf_export_success,
                dialogText = dialogText,
                onView = {
                    if (exportType != null) {
                        viewFile(context, exportResult.filePath, exportType)
                    }
                    onClearResult()
                },
                onShare = {
                    if (exportType != null) {
                        sharePdf(context, exportResult.filePath, exportType)
                    }
                    onClearResult()
                },
            )
        }

        is ExportResult.Error -> {
            //retry dialog
        }

        null -> {}
    }
}

private fun viewFile(context: Context, filePath: String, exportType: ExportType) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val type = when (exportType) {
        ExportType.Pdf -> "application/pdf"
        ExportType.Excel -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    }

    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, type)
        flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(intent, "Open file"))


}

private fun sharePdf(context: Context, filePath: String, exportType: ExportType) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    val type = when (exportType) {
        ExportType.Pdf -> "application/pdf"
        ExportType.Excel -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        this.type = type
        putExtra(Intent.EXTRA_STREAM, uri)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(intent, "Share File"))
}

@Composable
private fun ExportDialog(
    onDismissRequest: () -> Unit,
    onShare: () -> Unit,
    onView: () -> Unit,
    @StringRes dialogTitle: Int,
    dialogText: String,
) {
    androidx.compose.material3.AlertDialog(title = {
        CustomText(
            text = stringResource(dialogTitle),
            style = MyAppTheme.typography.Semibold57,
            color = MyAppTheme.colors.black
        )
    }, text = {
        CustomText(
            text = dialogText,
            style = MyAppTheme.typography.Regular46,
            color = MyAppTheme.colors.gray2
        )
    }, onDismissRequest = {
        onDismissRequest()
    }, confirmButton = {

        Row {
            TextButton(onClick = {
                onView()
            }) {
                CustomText(stringResource(R.string.view))
            }

            TextButton(onClick = {
                onShare()
            }) {
                CustomText(stringResource(R.string.share))
            }
        }

    })
}
