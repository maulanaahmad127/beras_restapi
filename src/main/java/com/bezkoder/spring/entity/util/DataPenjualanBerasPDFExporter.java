package com.bezkoder.spring.entity.util;

import java.awt.Color;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
 
import javax.servlet.http.HttpServletResponse;


import com.bezkoder.spring.entity.model.DataPenjualanBeras;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
 
 
public class DataPenjualanBerasPDFExporter {
    private List<DataPenjualanBeras> listBeras;
     
    public DataPenjualanBerasPDFExporter(List<DataPenjualanBeras> listBeras) {
        this.listBeras = listBeras;
    }
 
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(2);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
         
        cell.setPhrase(new Phrase("Jenis Beras", font));
        table.addCell(cell);
         

        cell.setPhrase(new Phrase("Beras Terjual", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Total Berat", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Bulan", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tahun", font));
        table.addCell(cell);
         
    }
     
    private void writeTableData(PdfPTable table) {
        for (DataPenjualanBeras beras : listBeras) {
            table.addCell(beras.getJenisBeras());
            table.addCell(String.valueOf(beras.getBerasTerjual()));
            table.addCell(String.valueOf(beras.getTotalBerat()));
            table.addCell(String.valueOf(beras.getBulan()));
            table.addCell(String.valueOf(beras.getTahun()));
        }
    }
     
    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        
        final HeaderFooter header = new HeaderFooter(new Phrase("SPPB", fontHeader), false);
        header.setAlignment(Element.ALIGN_LEFT);
        header.setBorder(Rectangle.BOTTOM);
        
        final HeaderFooter footer = new HeaderFooter(new Phrase("Last Updated at: " + dtf.format(now) ,fontHeader), false);
        footer.setAlignment(Element.ALIGN_RIGHT);
        footer.setBorder(Rectangle.TOP);

        document.setHeader(header);
        document.setFooter(footer);
         
        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(15);
        font.setColor(Color.BLUE);
         
        Paragraph p = new Paragraph("List of Data Penjualan Produksi Beras", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);
         
        document.add(p);
         
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {2.5f, 1.5f, 1.5f, 1.5f, 1.5f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);
        writeTableData(table);
         
        document.add(table);
         
        document.close();
         
    }
}