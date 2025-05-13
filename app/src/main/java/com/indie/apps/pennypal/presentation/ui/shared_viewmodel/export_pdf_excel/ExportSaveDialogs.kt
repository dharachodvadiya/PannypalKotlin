package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
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

            ExportDialog(
                onDismissRequest = { onClearResult() },
                dialogTitle = "Pdf export Success",
                dialogText = "pdf saved to ${exportResult.filePath}",
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
    dialogTitle: String,
    dialogText: String,
) {
    androidx.compose.material3.AlertDialog(title = {
        CustomText(
            text = dialogTitle,
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
                CustomText("View")
            }

            TextButton(onClick = {
                onShare()
            }) {
                CustomText("Share")
            }
        }

    })
}
