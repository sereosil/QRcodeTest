import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.*;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.omg.IOP.ENCODING_CDR_ENCAPS;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.event.PrintServiceAttributeListener;
import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

import static com.lowagie.text.Annotation.FILE;


/**
 * Created by sereo_000 on 26.09.2016.
 */
public class Testing {

    public static void main(String args) throws Exception {
        File temp = File.createTempFile("temp-file-name", ".tmp");

        System.out.println("Temp file : " + temp.getAbsolutePath());

        //Get tempropary file path
        String absolutePath = temp.getAbsolutePath();
        String tempFilePath = absolutePath.
                substring(0,absolutePath.lastIndexOf(File.separator));
        UUID idForPDF = UUID.randomUUID();
        String filenameOfPDF = idForPDF.toString().replaceAll("-","");
        UUID idForQR = UUID.randomUUID();
        String filenameOfQR = idForQR.toString().replaceAll("-","");
        String textInQrCode = "http://crunchify.com/";
        File pdfFileForPrint = new File("C:/Users/sereo_000/Downloads/3.pdf");
        File pdfFileWithWatermarkAndQrCode = new File(tempFilePath+"/"+filenameOfPDF+".pdf");
        com.lowagie.text.Image qrCode = Image.getInstance("C:/Users/sereo_000/Downloads/123.png");
        com.lowagie.text.Image watermark = Image.getInstance("C:/Users/sereo_000/Downloads/111.png");
        String pathWhereQrCodeWillBeSaved = tempFilePath+"/"+filenameOfQR+".png";
        String collectionNumber = "123";
        Date currentDate;
        currentDate = new Date(System.currentTimeMillis());
        String koi8Data = new String();

        String attributeOfFile = new String("Номер серии: "+collectionNumber+" Дата и время печати: "+currentDate+" Напечатал: ");
        System.out.println(attributeOfFile);
        int width = 650;
        int height = 30;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0, width, height);
        g2.setStroke(new BasicStroke(20.0f));
        g2.setColor(Color.black);
        g2.setFont(new Font("Arial",1,10));
        g2.drawString(attributeOfFile,10,15);
        ImageIO.write(bi, "jpg",new File("C:/Users/sereo_000/Downloads/qq.jpg"));
        com.lowagie.text.Image qeqe = Image.getInstance("C:/Users/sereo_000/Downloads/qq.jpg");
        int sizeOfQrCode = 250;
        String fileType = "png";
        QrCodeMaking(textInQrCode,fileType,sizeOfQrCode,pathWhereQrCodeWillBeSaved);
        WatermarkAndQrCodeAddingOnFile(pdfFileForPrint,pdfFileWithWatermarkAndQrCode,qrCode,watermark,attributeOfFile,qeqe);
        PrintingFile(pdfFileWithWatermarkAndQrCode,args);
        pdfFileWithWatermarkAndQrCode.deleteOnExit();
    }
    private static void QrCodeMaking(String textInQrCode, String fileType, int sizeOfQrCode, String pathWhereQrCodeWillBeSaved){
        File myFile = new File(pathWhereQrCodeWillBeSaved);
        try {

            Map<EncodeHintType, Object> hintMap = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Now with zxing version 3.2.1 you could change border size (white border size to just 1)
            hintMap.put(EncodeHintType.MARGIN, 1); /* default = 4 */
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix byteMatrix = qrCodeWriter.encode(textInQrCode, BarcodeFormat.QR_CODE, sizeOfQrCode,
                    sizeOfQrCode, hintMap);
            int CrunchifyWidth = byteMatrix.getWidth();
            BufferedImage image = new BufferedImage(CrunchifyWidth, CrunchifyWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();

            Graphics2D graphics = (Graphics2D) image.getGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, CrunchifyWidth, CrunchifyWidth);
            graphics.setColor(Color.BLACK);

            for (int i = 0; i < CrunchifyWidth; i++) {
                for (int j = 0; j < CrunchifyWidth; j++) {
                    if (byteMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }
            ImageIO.write(image, fileType, myFile);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\n\nYou have successfully created QR Code.");
    }
    private static void WatermarkAndQrCodeAddingOnFile(File pdfFileForPrint, File pdfFileWithWatermarkAndQrCode, Image qrCode, Image watermark, String attributeOfFile, Image g2) throws IOException, DocumentException {
        watermark.setAlignment(Element.ALIGN_CENTER);
        boolean a = pdfFileForPrint.canWrite();
        PdfReader reader = new PdfReader(pdfFileForPrint.getPath());
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                pdfFileWithWatermarkAndQrCode));
        for(int i=1; i<= reader.getNumberOfPages(); i++) {
            PdfContentByte content = stamper.getOverContent(i);
            PdfContentByte content1 = stamper.getUnderContent(i);
            PdfContentByte content2 = stamper.getOverContent(i);
            qrCode.setAbsolutePosition(530f, 780f);
            qrCode.scaleAbsolute(50f,50f);
            g2.setAbsolutePosition(0f,800f);
            g2.scaleAbsolute(500f,30f);
            watermark.setAbsolutePosition(140f, 350f);
            watermark.setRotationDegrees(45);
            content.addImage(qrCode);
            content1.addImage(watermark);
            ColumnText ct = new ColumnText( content2 );
// this are the coordinates where you want to add text
// if the text does not fit inside it will be cropped
            ct.setSimpleColumn(30,835,750,50);
            String attributeTest = new String(attributeOfFile.getBytes("ISO-8859-5"),"utf8");
            //content2.addImage(g2);
            content2.addImage(g2);
            //ct.setText(new Phrase(String.valueOf(g2)));
            ct.go();
        }
        stamper.getWriter().addDirectImageSimple(qrCode);
        stamper.close();
    }
    private static PrintService findPrintService(String printerName) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printService : printServices) {
            if (printService.getName().trim().contains(printerName)) {
                return printService;
            }
        }
        return null;
    }
    private static void PrintingFile(File pdfFileWithWatermarkAndQrCode, String name) throws IOException, PrinterException {
        PrintService myPrintService = findPrintService(name);
        PrinterJob job = PrinterJob.getPrinterJob();
        PDDocument document = PDDocument.load(pdfFileWithWatermarkAndQrCode);
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);
        job.print();
    }
}
