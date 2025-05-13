package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.indie.apps.pennypal.R
import com.indie.apps.pennypal.data.module.merchant_data.MerchantDataWithAllData
import com.indie.apps.pennypal.domain.usecase.LoadMerchantDataWithAllDataListUseCase
import com.indie.apps.pennypal.util.Util
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    private val loadMerchantDataWithAllDataListUseCase: LoadMerchantDataWithAllDataListUseCase,
) : ViewModel() {

    private val greenColorPdf = DeviceRgb(0, 128, 0) // Dark green
    private val redColorPdf = DeviceRgb(220, 20, 60) // Crimson red
    private val lightBluePdf = DeviceRgb(191, 243, 255)

    private val headerBackgroundPdf = DeviceRgb(0, 102, 204) // Dark blue
    private val headerTextColorPdf = DeviceRgb(255, 255, 255) // White
    private val lightRowPdf = DeviceRgb(235, 245, 255) // Light blue
    private val whiteRowPdf = DeviceRgb(255, 255, 255) // White

    val greenColor = XSSFColor(byteArrayOf(0, 128.toByte(), 0), null) // Dark green
    val redColor = XSSFColor(byteArrayOf(220.toByte(), 20, 60), null) // Crimson red
    private val lightBlueRow =
        XSSFColor(byteArrayOf(235.toByte(), 245.toByte(), 255.toByte()), null)
    private val whiteRow = XSSFColor(byteArrayOf(255.toByte(), 255.toByte(), 255.toByte()), null)
    private val headerBgColor =
        XSSFColor(byteArrayOf(0, 102.toByte(), 204.toByte()), null) // Dark blue
    private val headerTextColor =
        IndexedColors.WHITE.index // Excel doesn't support RGB for font color directly in all cases

    private val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    // val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())

    private val _exportResult = MutableStateFlow<ExportResult?>(null)
    val exportResult: StateFlow<ExportResult?> = _exportResult

    fun exportToExcel(fromDateMilli: Long, toDateMilli: Long) {
        viewModelScope.launch {
            _exportResult.value = ExportResult.Loading
            try {
                val merchantData =
                    loadMerchantDataWithAllDataListUseCase.getLast3DataFromPeriod(0, 0).first()
                val workbook = XSSFWorkbook()
                val sheet = workbook.createSheet("Transaction Summary")

                // Header style
                val headerStyle = workbook.createCellStyle().apply {
                    fillPattern = FillPatternType.SOLID_FOREGROUND
                    alignment = HorizontalAlignment.CENTER
                    setFont(workbook.createFont().apply {
                        bold = true
                        color = headerTextColor
                    })
                    setFillForegroundColor(headerBgColor)
                }

                // White row styles
                val whiteRowStyle = workbook.createCellStyle().apply {
                    setFillForegroundColor(whiteRow)
                    fillPattern = FillPatternType.SOLID_FOREGROUND
                    alignment = HorizontalAlignment.LEFT
                }

                // Light blue row styles
                val lightBlueRowStyle = workbook.createCellStyle().apply {
                    setFillForegroundColor(lightBlueRow)
                    fillPattern = FillPatternType.SOLID_FOREGROUND
                    alignment = HorizontalAlignment.LEFT
                }


                // Currency symbol
                val currencySymbol = merchantData.firstOrNull()?.originalAmountSymbol ?: "₹"

                // ✅ Added summary data function
                val totalSummary = calculateSummary(merchantData)

                val summary = listOf(
                    "Total Income: $currencySymbol${Util.getFormattedString(totalSummary.income)}",
                    "Total Expense: $currencySymbol${Util.getFormattedString(totalSummary.expense)}",
                    "Total Balance: $currencySymbol${Util.getFormattedString(totalSummary.balance)}"
                )

                /*summary.forEachIndexed { i, line ->
                    val row = sheet.createRow(i)
                    val cell = row.createCell(0)
                    cell.setCellValue(line)
                    sheet.addMergedRegion(org.apache.poi.ss.util.CellRangeAddress(i, i, 0, 4))
                    cell.cellStyle = workbook.createCellStyle().apply {
                        setFont(workbook.createFont().apply {
                            bold = true
                            IndexedColors.GREEN.index
                        })
                    }
                }*/

                summary.forEachIndexed { i, line ->
                    val row = sheet.createRow(i)
                    val cell = row.createCell(0)
                    cell.setCellValue(line)
                    sheet.addMergedRegion(org.apache.poi.ss.util.CellRangeAddress(i, i, 0, 4))
                    val font = workbook.createFont().apply {
                        bold = true
                        color = when (i) {
                            0 -> IndexedColors.GREEN.index
                            1 -> IndexedColors.RED.index
                            else -> IndexedColors.BLACK.index
                        }
                    }
                    val style = workbook.createCellStyle().apply {
                        setFont(font)
                    }
                    cell.cellStyle = style
                }

                val startDataRow = summary.size + 1

                // Header row
                val headerRow = sheet.createRow(startDataRow)
                val columnHeaders = listOf("No", "Title", "Amount", "Category", "Date", "Note")
                columnHeaders.forEachIndexed { index, title ->
                    val cell = headerRow.createCell(index)
                    cell.setCellValue(title)
                    cell.cellStyle = headerStyle
                }

                // Data rows
                merchantData.forEachIndexed { index, data ->
                    val rowIndex = startDataRow + 1 + index
                    val row = sheet.createRow(rowIndex)

                    val isEven = index % 2 == 0
                    val rowStyle = if (isEven) whiteRowStyle else lightBlueRowStyle

                    val incomeStr =
                        if (data.type > 0) "${data.originalAmount} ${data.originalAmountSymbol}" else "-"
                    val expenseStr =
                        if (data.type < 0) "${data.originalAmount} ${data.originalAmountSymbol}" else "-"

                    val values = listOf(
                        index.toString(),
                        data.details,
                        incomeStr,
                        expenseStr,
                        data.paymentName,
                        dateTimeFormat.format(data.dateInMilli)
                    )

                    values.forEachIndexed { i, value ->
                        val cell = row.createCell(i)
                        cell.setCellValue(value)
                        cell.cellStyle = rowStyle
                    }
                }

                // Adjust column widths
                listOf(5, 25, 15, 15, 20, 25).forEachIndexed { i, width ->
                    sheet.setColumnWidth(i, width * 256)
                }
                val firstRow = startDataRow      // Header row
                val lastRow = startDataRow + merchantData.size // Last data row
                val firstCol = 0
                val lastCol = 5

                applyTableBorder(workbook, sheet, firstRow, lastRow, firstCol, lastCol)


                /*val dateRangeStr =
                    "${dateFormat.format(fromDateMilli)}_${dateFormat.format(toDateMilli)}"*/
                val dateRangeStr = System.currentTimeMillis()
                val fileName = "PennyPal_Transactions_$dateRangeStr.xlsx"
                // fileName = "PennyPal_Transactions_1.xlsx"

                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
                )
                FileOutputStream(file).use { workbook.write(it) }
                workbook.close()

                _exportResult.value = ExportResult.Success(file.absolutePath)
            } catch (e: Exception) {
                _exportResult.value = ExportResult.Error(e.message ?: "Export failed")
            }
        }
    }

    fun exportToPdf(context: Context, fromDateMilli: Long, toDateMilli: Long) {
        viewModelScope.launch {
            _exportResult.value = ExportResult.Loading
            try {


                val merchantData =
                    loadMerchantDataWithAllDataListUseCase.getLast3DataFromPeriod(0, 0).first()

                /*val dateRangeStr =
                    "${dateFormat.format(fromDateMilli)}_${dateFormat.format(toDateMilli)}"*/
                val dateRangeStr = System.currentTimeMillis()
                val fileName = "PennyPal_Transactions_$dateRangeStr.pdf"

                val file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
                )
                val writer = PdfWriter(FileOutputStream(file))
                val pdf = PdfDocument(writer)

                // Load icon from assets
                // ✅ Load drawable icon and convert to ImageData
                val drawable = ContextCompat.getDrawable(context, R.drawable.icon_loading)
                val bitmap = createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight)
                val canvas = android.graphics.Canvas(bitmap)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)

                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val iconBytes = stream.toByteArray()
                val appIcon = ImageDataFactory.create(iconBytes)

                pdf.addEventHandler(
                    PdfDocumentEvent.END_PAGE,
                    HeaderFooterPageEvent(fromDateMilli, toDateMilli, appIcon)
                )

                val document = Document(pdf)

                // Title
                document.add(
                    Paragraph("Transaction Summary")
                        .setBold()
                        .setFontSize(16f)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginTop(40f)
                )

                // Summary Section
                //val decimalFormat = DecimalFormat("#,##0.00")
                val currencySymbol = merchantData.firstOrNull()?.originalAmountSymbol ?: "₹"
                val summary = calculateSummary(merchantData)
                document.add(
                    Paragraph("Total Income: $currencySymbol${Util.getFormattedString(summary.income)}")
                        .setFontSize(12f)
                        .setFontColor(greenColorPdf)
                )
                document.add(
                    Paragraph("Total Expense: $currencySymbol${Util.getFormattedString(summary.expense)}")
                        .setFontSize(12f)
                        .setFontColor(redColorPdf)
                )
                document.add(
                    Paragraph("Total Balance: $currencySymbol${Util.getFormattedString(summary.balance)}")
                        .setFontSize(12f)
                        .setMarginBottom(20f)
                )


                // Table
                val table = Table(floatArrayOf(50f, 130f, 90f, 90f, 90f, 150f))
                table.setBorder(Border.NO_BORDER)
                listOf("ID", "Details", "Income", "Expense", "Payment Method", "Date").forEach {
                    val cell = Cell().add(
                        Paragraph(it)
                            .setBold()
                            .setFontColor(headerTextColorPdf)
                    )
                        .setBackgroundColor(headerBackgroundPdf)
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER)
                    table.addCell(cell)
                }

                merchantData.forEachIndexed { index, data ->
                    val bgColor = if (index % 2 == 0) whiteRowPdf else lightRowPdf

                    val incomeStr =
                        if (data.type < 0) "-" else "${data.originalAmount} ${data.originalAmountSymbol}"
                    val expenseStr =
                        if (data.type > 0) "-" else "${data.originalAmount} ${data.originalAmountSymbol}"

                    listOf(
                        index.toString(),
                        data.details,
                        incomeStr,
                        expenseStr,
                        data.paymentName,
                        dateTimeFormat.format(data.dateInMilli)
                    ).forEachIndexed() { ind, value ->
                        val p = Paragraph(value)

                        if (ind == 2)
                            p.setFontColor(greenColorPdf)
                        else if (ind == 3)
                            p.setFontColor(redColorPdf)

                        val cell = Cell().add(p)
                            .setBackgroundColor(bgColor)
                            .setBorder(Border.NO_BORDER)

                        if (ind != 1)
                            cell.setTextAlignment(TextAlignment.CENTER)

                        table.addCell(cell)
                    }
                }

                // Wrap in a border (simulate outer border)
                val wrapperTable = Table(1)
                wrapperTable.setWidth(UnitValue.createPercentValue(100f))
                val wrapperCell = Cell().add(table)
                    .setBorder(SolidBorder(1f)) // Outer border
                    .setPadding(0f)
                wrapperTable.addCell(wrapperCell)

                document.add(wrapperTable)
                document.close()

                _exportResult.value = ExportResult.Success(file.absolutePath)
            } catch (e: Exception) {
                println("aaaa ${e.message}")
                _exportResult.value = ExportResult.Error(e.message ?: "PDF export failed")
            }
        }
    }

    fun clearExportResult() {
        _exportResult.value = null
    }

    private fun applyTableBorder(
        workbook: XSSFWorkbook,
        sheet: Sheet,
        startRow: Int,
        endRow: Int,
        startCol: Int,
        endCol: Int
    ) {
        val borderStyle = BorderStyle.MEDIUM

        // Apply top border to header row
        for (col in startCol..endCol) {
            val cell = sheet.getRow(startRow)?.getCell(col) ?: continue
            val originalStyle = cell.cellStyle
            val newStyle = workbook.createCellStyle().apply {
                cloneStyleFrom(originalStyle)
                borderLeft = BorderStyle.NONE
                borderRight = BorderStyle.NONE
                borderBottom = BorderStyle.NONE
                borderTop = borderStyle
            }
            cell.cellStyle = newStyle
        }

        // Apply bottom border to last row
        for (col in startCol..endCol) {
            val cell = sheet.getRow(endRow)?.getCell(col) ?: continue
            val originalStyle = cell.cellStyle
            val newStyle = workbook.createCellStyle().apply {
                cloneStyleFrom(originalStyle)
                borderLeft = BorderStyle.NONE
                borderRight = BorderStyle.NONE
                borderTop = BorderStyle.NONE
                borderBottom = borderStyle
            }
            cell.cellStyle = newStyle
        }

        // Apply left and right borders to all rows
        for (rowIndex in startRow..endRow) {
            val row = sheet.getRow(rowIndex) ?: continue

            // Left border
            val leftCell = row.getCell(startCol)
            if (leftCell != null) {
                val originalStyle = leftCell.cellStyle
                val newStyle = workbook.createCellStyle().apply {
                    cloneStyleFrom(originalStyle)
                    if (originalStyle.borderTop != borderStyle)
                        borderTop = BorderStyle.NONE

                    if (originalStyle.borderBottom != borderStyle)
                        borderBottom = BorderStyle.NONE

                    borderRight = BorderStyle.NONE
                    borderLeft = borderStyle
                }
                leftCell.cellStyle = newStyle
            }

            // Right border
            val rightCell = row.getCell(endCol)
            if (rightCell != null) {
                val originalStyle = rightCell.cellStyle
                val newStyle = workbook.createCellStyle().apply {
                    cloneStyleFrom(originalStyle)
                    if (originalStyle.borderTop != borderStyle)
                        borderTop = BorderStyle.NONE

                    if (originalStyle.borderBottom != borderStyle)
                        borderBottom = BorderStyle.NONE
                    borderLeft = BorderStyle.NONE
                    borderRight = borderStyle
                }
                rightCell.cellStyle = newStyle
            }
        }
    }

    private fun calculateSummary(data: List<MerchantDataWithAllData>): SummaryData {
        val income = data.filter { it.type > 0 }.sumOf { it.amount }
        val expense = data.filter { it.type < 0 }.sumOf { it.amount }
        return SummaryData(income, expense, income - expense)
    }

    data class SummaryData(val income: Double, val expense: Double, val balance: Double)
}

sealed class ExportResult {
    object Loading : ExportResult()
    data class Success(val filePath: String) : ExportResult()
    data class Error(val message: String) : ExportResult()
}