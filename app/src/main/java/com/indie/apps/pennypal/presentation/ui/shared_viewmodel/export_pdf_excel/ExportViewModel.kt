package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel

import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.domain.usecase.LoadMerchantDataWithAllDataListUseCase
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val loadMerchantDataWithAllDataListUseCase: LoadMerchantDataWithAllDataListUseCase,
) : ViewModel() {

    private val _exportResult = MutableStateFlow<ExportResult?>(null)
    val exportResult: StateFlow<ExportResult?> = _exportResult

    /* fun exportToExcel(context: Context) {
         viewModelScope.launch {
             _exportResult.value = ExportResult.Loading
             try {
                 val transactions = transactionRepository.getAllTransactions()
                 val workbook = XSSFWorkbook()
                 val sheet = workbook.createSheet("Transactions")

                 // Header row
                 val header = sheet.createRow(0)
                 listOf("ID", "Amount", "Currency", "Category", "Date", "Description").forEachIndexed { index, title ->
                     header.createCell(index).setCellValue(title)
                 }

                 // Data rows
                 transactions.forEachIndexed { index, transaction ->
                     val row = sheet.createRow(index + 1)
                     row.createCell(0).setCellValue(transaction.id.toDouble())
                     row.createCell(1).setCellValue(transaction.amount)
                     row.createCell(2).setCellValue(transaction.currency)
                     row.createCell(3).setCellValue(transaction.category)
                     row.createCell(4).setCellValue(
                         SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(transaction.date)
                     )
                     row.createCell(5).setCellValue(transaction.description)
                 }

                 // Auto-size columns
                 (0..5).forEach { sheet.autoSizeColumn(it) }

                 // Save file
                 val file = File(
                     Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                     "PennyPal_Transactions_${System.currentTimeMillis()}.xlsx"
                 )
                 FileOutputStream(file).use { workbook.write(it) }
                 workbook.close()

                 _exportResult.value = ExportResult.Success(file.absolutePath)
             } catch (e: Exception) {
                 _exportResult.value = ExportResult.Error(e.message ?: "Export failed")
             }
         }
     }*/

    fun exportToPdf(fromDateMilli: Long, toDateMilli: Long) {
        viewModelScope.launch {
            _exportResult.value = ExportResult.Loading
            try {
                val merchantData =
                    loadMerchantDataWithAllDataListUseCase.getLast3DataFromPeriod(0, 0).first()
                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "PennyPal_Last3_MerchantData_${System.currentTimeMillis()}.pdf"
                )
                val writer = PdfWriter(FileOutputStream(file))
                val pdf = PdfDocument(writer)

                // Add header/footer event handler
                val exportDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date())
                val headerFooterPageEvent = HeaderFooterPageEvent("Transactions", exportDate)
                pdf.addEventHandler(
                    PdfDocumentEvent.END_PAGE,
                    headerFooterPageEvent
                )

                val document = Document(pdf)

                // Title
                document.add(Paragraph("PennyPal Last 3 Merchant Data").setBold().setFontSize(16f))

                // Table
                val table = Table(floatArrayOf(50f, 150f, 100f, 100f, 200f))
                listOf("ID", "Details", "Amount", "Category", "Date").forEach {
                    table.addCell(Cell().add(Paragraph(it).setBold()))
                }

                merchantData.forEachIndexed() { index, data ->
                    table.addCell(Cell().add(Paragraph(index.toString())))
                    table.addCell(Cell().add(Paragraph(data.details)))
                    table.addCell(Cell().add(Paragraph("${data.originalAmount} ${data.originalAmountSymbol}")))
                    table.addCell(Cell().add(Paragraph(data.categoryName ?: "")))
                    table.addCell(
                        Cell().add(
                            Paragraph(
                                SimpleDateFormat(
                                    "dd MMM yyyy",
                                    Locale.getDefault()
                                ).format(data.dateInMilli)
                            )
                        )
                    )

                }

                document.add(table)
                document.close()

                _exportResult.value = ExportResult.Success(file.absolutePath)
            } catch (e: Exception) {
                _exportResult.value = ExportResult.Error(e.message ?: "PDF export failed")
            }
        }
    }

    fun clearExportResult() {
        _exportResult.value = null
    }
}

sealed class ExportResult {
    object Loading : ExportResult()
    data class Success(val filePath: String) : ExportResult()
    data class Error(val message: String) : ExportResult()
}