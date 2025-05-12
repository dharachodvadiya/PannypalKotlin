package com.indie.apps.pennypal.presentation.ui.shared_viewmodel.export_pdf_excel

import com.itextpdf.kernel.events.Event
import com.itextpdf.kernel.events.IEventHandler
import com.itextpdf.kernel.events.PdfDocumentEvent
import com.itextpdf.kernel.pdf.canvas.PdfCanvas
import com.itextpdf.layout.Canvas
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment

class HeaderFooterPageEvent(
    private val exportType: String,
    private val exportDate: String
) : IEventHandler {

    override fun handleEvent(event: Event?) {
        val docEvent = event as PdfDocumentEvent
        val pdf = docEvent.document
        val page = docEvent.page
        val pageSize = page.pageSize
        val pdfCanvas = PdfCanvas(page)
        val canvas = Canvas(pdfCanvas, pageSize)

        // Header
        val headerY = pageSize.top - 36
        canvas.showTextAligned(
            Paragraph("PennyPal").setFontSize(10f),
            36f,
            headerY,
            TextAlignment.LEFT
        )
        canvas.showTextAligned(
            Paragraph(exportType).setFontSize(10f),
            pageSize.right - 36,
            headerY,
            TextAlignment.RIGHT
        )

        // Footer
        val footerY = 36f
        canvas.showTextAligned(
            Paragraph("Exported on $exportDate").setFontSize(10f),
            36f,
            footerY,
            TextAlignment.LEFT
        )
        val pageNumber = "Page ${pdf.getPageNumber(page)} of ${pdf.numberOfPages}"
        canvas.showTextAligned(
            Paragraph(pageNumber).setFontSize(10f),
            pageSize.right - 36,
            footerY,
            TextAlignment.RIGHT
        )

        canvas.close()
    }
}