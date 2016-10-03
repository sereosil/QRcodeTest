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
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javax.imageio.ImageIO;
import javax.print.*;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.event.PrintServiceAttributeListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import static com.lowagie.text.Annotation.FILE;

/**
 * Created by sereo_000 on 26.09.2016.
 */
public class Testing {

    public static void main(String[] args) throws WriterException, IOException, DocumentException, PrintException, PrinterException {

        String textInQrCode = "http://crunchify.com/";
        File pdfFileForPrint = new File("C:/Users/sereo_000/Downloads/3.pdf");
        File pdfFileWithWatermarkAndQrCode = new File("C:/Users/sereo_000/Downloads/1.pdf");
        com.lowagie.text.Image qrCode = Image.getInstance("C:/Users/sereo_000/Downloads/123.png");
        com.lowagie.text.Image watermark = Image.getInstance("C:/Users/sereo_000/Downloads/111.png");
        String pathWhereQrCodeWillBeSaved = "C:/Users/sereo_000/Downloads/12321.png";
        int sizeOfQrCode = 250;
        String fileType = "png";
        QrCodeMaking(textInQrCode,fileType,sizeOfQrCode,pathWhereQrCodeWillBeSaved);
        WatermarkAndQrCodeAddingOnFile(pdfFileForPrint,pdfFileWithWatermarkAndQrCode,qrCode,watermark);
        PrintingFile(pdfFileWithWatermarkAndQrCode);
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
    private static void WatermarkAndQrCodeAddingOnFile(File pdfFileForPrint,File pdfFileWithWatermarkAndQrCode, Image qrCode,Image watermark) throws IOException, DocumentException {
        watermark.setAlignment(Element.ALIGN_CENTER);
        boolean a = pdfFileForPrint.canWrite();
        PdfReader reader = new PdfReader(pdfFileForPrint.getPath());
        //PdfWriter writer = new PdfWriter(pdfFileForPrint);
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                pdfFileWithWatermarkAndQrCode));
        for(int i=1; i<= reader.getNumberOfPages(); i++) {
            PdfContentByte content = stamper.getOverContent(i);
            PdfContentByte content1 = stamper.getUnderContent(i);
            qrCode.setAbsolutePosition(530f, 780f);
            qrCode.scaleAbsolute(50f,50f);
            watermark.setAbsolutePosition(140f, 350f);
            watermark.setRotationDegrees(45);
            content.addImage(qrCode);
            content1.addImage(watermark);
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
    private static void PrintingFile(File pdfFileWithWatermarkAndQrCode) throws IOException, PrinterException {
        PrintService myPrintService = findPrintService("NPIF848B5");
        PrinterJob job = PrinterJob.getPrinterJob();
        PDDocument document = PDDocument.load(pdfFileWithWatermarkAndQrCode);
        job.setPageable(new PDFPageable(document));
        job.setPrintService(myPrintService);
        job.print();
    }
}
