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
import java.io.File

@Composable
fun ExportSaveDialogs(
    pdfExportResult: ExportResult?,
    onClearResult: () -> Unit
) {
    val context = LocalContext.current
    when (pdfExportResult) {
        is ExportResult.Loading -> {
            CustomProgressDialog(
                message = R.string.loading
            )
        }

        is ExportResult.Success -> {

            ExportDialog(
                onDismissRequest = { onClearResult() },
                dialogTitle = "Pdf export Success",
                dialogText = "pdf saved to ${pdfExportResult.filePath}",
                onView = {
                    viewPdf(context, pdfExportResult.filePath)
                    onClearResult()
                },
                onShare = {
                    sharePdf(context, pdfExportResult.filePath)
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

private fun viewPdf(context: Context, filePath: String) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(intent, "Open PDF"))
}

private fun sharePdf(context: Context, filePath: String) {
    val file = File(filePath)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(intent, "Share PDF"))
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
