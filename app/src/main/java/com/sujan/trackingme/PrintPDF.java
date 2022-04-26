package com.sujan.trackingme;

import android.os.Environment;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

public class PrintPDF {
    //declare objects
    int repno,tripno;
    String rTitle,vhinum,vhitype,feultype,drivname,liceno,drivaddress,distance,fromadd,toadd,feulused;
    long repoDate;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public PrintPDF(int repno, int tripno, String rTitle, String vhinum, String vhitype, String feultype, String drivname, String liceno, String drivaddress, String distance, String fromadd, String toadd, String feulused, long repoDate) {
        this.repno = repno;
        this.tripno = tripno;
        this.rTitle = rTitle;
        this.vhinum = vhinum;
        this.vhitype = vhitype;
        this.feultype = feultype;
        this.drivname = drivname;
        this.liceno = liceno;
        this.drivaddress = drivaddress;
        this.distance = distance;
        this.fromadd = fromadd;
        this.toadd = toadd;
        this.feulused = feulused;
        this.repoDate = repoDate;
    }

    //to create report
    public  void getPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath,rTitle+".pdf");
        OutputStream outputStream= new OutputStream() {
            @Override
            public void write(int b) throws IOException {

            }
        };

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document= new Document(pdfDocument);



        Text header = new Text("Mileage Report").setBold().setUnderline();
        Text text7 = new Text("underLine").setUnderline();



        float columnwidth[] = {120,220,120,300};
        Table table3 = new Table(columnwidth);

        float colWidth[] ={280,280};
        Table table = new Table(colWidth);
        table.addCell(new Cell().add(new Paragraph("\tDriver Name     :").setFontSize(14)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(drivname)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph("\tDriver Licence  :").setFontSize(14)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(liceno)).setBorder(Border.NO_BORDER));

        table.addCell(new Cell().add(new Paragraph("\tDriver Address  :").setFontSize(14)).setBorder(Border.NO_BORDER));
        table.addCell(new Cell().add(new Paragraph(drivaddress)).setBorder(Border.NO_BORDER));

        
        float columnwidth2[] = {188,186,186};
        Table table2 = new Table(columnwidth2);

        table2.addCell(new Cell().add(new Paragraph("Vehicle Type")));
        table2.addCell(new Cell().add(new Paragraph("Vehicle Number")));
        table2.addCell(new Cell().add(new Paragraph("Feul Type")));

        table2.addCell(new Cell().add(new Paragraph(vhitype)));
        table2.addCell(new Cell().add(new Paragraph(vhinum)));
        table2.addCell(new Cell().add(new Paragraph(feultype)));
        table2.addCell(new Cell(1,3).add(new Paragraph("")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell(1,2).add(new Paragraph("Feul Consumption")).setBorder(Border.NO_BORDER));
        table2.addCell(new Cell().add(new Paragraph(feulused)));



        try {
            document.add(new Paragraph(header)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(25)
                    .setFontColor(ColorConstants.BLUE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Paragraph last = new Paragraph("AUTOMATIC VEHICLE MANAGEMENT SYSTEM\n" +
                "University of Ruhuna,\nSri Lanka.");
        last.setTextAlignment(TextAlignment.CENTER);

        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Date :"+dateFormat.format(repoDate)+"\tReport no: "+repno+"\n"));
        document.add(new Paragraph("______________________________________________________________________________\n"));
        document.add(new Paragraph("Driver details\n").setUnderline().setBold().setFontSize(16));
        document.add(table);
        document.add(new Paragraph("______________________________________________________________________________\n"));
        document.add(new Paragraph("Vehicle details\n").setUnderline().setBold().setFontSize(16));
        document.add(table2);
        document.add(new Paragraph("______________________________________________________________________________\n"));
        document.add(new Paragraph("Trip details\n").setUnderline().setBold().setFontSize(16));
        document.add(new Paragraph("Trip Number :"+tripno+"\n").setBold().setFontSize(14));
        document.add(new Paragraph("From : "+fromadd+"\n"));
        document.add(new Paragraph("To    : "+toadd+"\n"));
        document.add(new Paragraph("Total Distance   : "+distance+" km\n").setFontSize(17).setBold());
        document.add(new Paragraph("______________________________________________________________________________\n"));
        document.add(last);
        document.add(new Paragraph("______________________________________________________________________________\n"));

        document.close();
    }
}

/*
*       Text text1 = new Text("Normal");
        Text text2 = new Text("Bold").setBold();
        Text text3 = new Text("Italic").setItalic();
        Text text4 = new Text("Bold Italic").setBold().setUnderline();
* */
