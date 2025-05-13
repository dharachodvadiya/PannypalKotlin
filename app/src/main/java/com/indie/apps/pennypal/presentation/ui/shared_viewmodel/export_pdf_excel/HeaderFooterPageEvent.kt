package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel

import android.annotation.SuppressLint
import com.itextpdf.io.image.ImageData
import com.itextpdf.kernel.events.Event
import com.itextpdf.kernel.events.IEventHandler
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.pdf.action.PdfAction
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Link
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import java.text.SimpleDateFormat
import java.util.Locale

class HeaderFooterPageEvent(
    private val fromDateMilli: Long,
    private val toDateMilli: Long,
    private val appIconImageData: ImageData
) : IEventHandler {

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun handleEvent(event: Event?) {
        val docEvent = event as PdfDocumentEvent
        val pdf = docEvent.document
        val page = docEvent.page
        val pageSize = page.pageSize
        val pdfCanvas = PdfCanvas(page)
        val canvas = Canvas(pdfCanvas, pageSize)

        val playStoreUrl = "https://play.google.com/store/apps/details?id=com.indie.apps.pennypal"
        val link = Link("PennyPal", PdfAction.createURI(playStoreUrl))


        // 🔝 Header
        // Header
        val headerTopY = pageSize.top - 36

        val image = Image(appIconImageData)
            .scaleToFit(20f, 20f)
            .setFixedPosition(36f, headerTopY - 5)
        canvas.add(image)

        canvas.showTextAligned(
            Paragraph().add(link).setFontSize(10f),
            60f, // Offset to right of icon
            headerTopY,
            TextAlignment.LEFT
        )

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val exportRange = "${dateFormat.format(fromDateMilli)} - ${dateFormat.format(toDateMilli)}"

        // Top Right: Created Date
        canvas.showTextAligned(
            Paragraph(exportRange).setFontSize(10f),
            pageSize.right - 36,
            headerTopY,
            TextAlignment.RIGHT
        )

        // Bottom Right: Export Range

        // Divider Line
        pdfCanvas.setLineWidth(1f)
        pdfCanvas.moveTo(36.0, (pageSize.top - 60).toDouble())
        pdfCanvas.lineTo((pageSize.right - 36).toDouble(), (pageSize.top - 60).toDouble())
        pdfCanvas.stroke()


        // Footer
        val footerY = 36f
        // Page Number
        canvas.showTextAligned(
            Paragraph("Page ${pdf.getPageNumber(page)} of ${pdf.numberOfPages}").setFontSize(10f),
            36f,
            footerY,
            TextAlignment.LEFT
        )
        // App Name with Play Store Link

        // Right: Clickable App Icon + App Name
        val footerIcon = Image(appIconImageData)
            .scaleToFit(20f, 20f)
            .setFixedPosition(pageSize.right - 60, footerY - 5)
        canvas.add(footerIcon)

        canvas.showTextAligned(
            Paragraph().add(link).setFontSize(10f),
            pageSize.right - 60,
            footerY,
            TextAlignment.RIGHT
        )

        canvas.close()
    }
}