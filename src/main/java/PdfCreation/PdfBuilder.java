package PdfCreation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.TIMES_ROMAN;

public class PdfBuilder {
    private static final float pageWidth = 1800;
    private static final float pageHeight = 600;

    private static final String resPrefix = "src/main/resources/";
    private static final String bgPath = resPrefix + "img.png";
    private static final String acelPath = resPrefix + "acel.png";
    private static final String logoPath = resPrefix + "logo.jpg";


    private static final String location = "Grizzly @ Clausen";
    private static final String time = "20:00";

    private static final PDType1Font font = PDType1Font.TIMES_BOLD;

    public static void createTicket(String name, String saveTo, BufferedImage qrCode, String code) throws IOException {
        try (PDDocument ticket = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
            ticket.addPage(page);


            addImageToBG(ticket, bgPath, new Rectangle(0,0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight()));

            //addImageAsOverlay(ticket, acelPath, 50, 50, 100);

            /*float logoWidth = 1000;
            addImageAsOverlay(ticket, logoPath, (pageWidth - logoWidth) / 2, pageHeight * 0.35f, logoWidth);*/



            addRotatedText(ticket, name, pageWidth * 0.92f, pageHeight * 0.1f, 40);
            addRotatedText(ticket, "Ticket Number: " + code, pageWidth * 0.96f, pageHeight * 0.05f, 25);
            /* addTextAtRatio(ticket, "Location:" ,pageHeight * 0.27f, 1f/3f);
            addTextAtRatio(ticket, location, pageHeight * 0.2f, 1f/3f);

            addTextAtRatio(ticket, "Time:", pageHeight * 0.27f, 2f/3f);
            addTextAtRatio(ticket, time, pageHeight * 0.2f, 2f/3f);*/


            addImageAsOverlayToRightBorder(ticket, qrCode);

            ticket.save(saveTo);
        }
    }

    private static void addImageToBG(PDDocument ticket, String path, Rectangle location) throws IOException {
        PDPage page = ticket.getPage(0);
        PDImageXObject pdImageXObject = PDImageXObject.createFromFile(path, ticket);

        PDPageContentStream cos = new PDPageContentStream(ticket, page,
                PDPageContentStream.AppendMode.PREPEND,
                true);

        cos.drawImage(pdImageXObject, location.x, location.y, location.width, location.height);
        cos.close();
    }


    /**
     * respects the aspect ratio of the original image
     * @throws IOException if there is nothing at path
     */
    private static void addImageAsOverlay(PDDocument ticket, String path, float x, float y, float width) throws IOException {
        PDPage page = ticket.getPage(0);
        PDImageXObject pdImageXObject = PDImageXObject.createFromFile(path, ticket);

        PDPageContentStream cos = new PDPageContentStream(ticket, page,
                PDPageContentStream.AppendMode.APPEND,
                true);

        float height = pdImageXObject.getHeight() * (width / pdImageXObject.getWidth());

        cos.drawImage(pdImageXObject, x, y, width, height);
        cos.close();
    }

    /**
     * puts an image along the right edge of the ticket while occupying the full height of the page, and calculating the width accordingly.
     * @param image the actual qr code
     * @throws IOException if there is nothing at path
     */
    private static void addImageAsOverlayToRightBorder(PDDocument ticket, BufferedImage image) throws IOException {
        PDPage page = ticket.getPage(0);
        PDImageXObject pdImageXObject = JPEGFactory.createFromImage(ticket, image);

        PDPageContentStream cos = new PDPageContentStream(ticket, page,
                PDPageContentStream.AppendMode.APPEND,
                true);

        //position is calculated from the right edge
        float width = pdImageXObject.getWidth() / (pdImageXObject.getHeight() / pageHeight);

        cos.drawImage(pdImageXObject, pageWidth - width, 0, width, pageHeight);
        cos.close();
    }

    private static void addText(PDDocument ticket, String text, float x, float y) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(ticket, ticket.getPage(0),
                PDPageContentStream.AppendMode.APPEND,
                true);
        contentStream.beginText();
        contentStream.newLineAtOffset(x,y);
        contentStream.setNonStrokingColor(Color.WHITE);
        contentStream.setFont(font, 40);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();
    }

    private static void addRotatedText(PDDocument ticket, String text, float x, float y, int fontSize) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(ticket, ticket.getPage(0),
                PDPageContentStream.AppendMode.APPEND,
                true);
        PDPage page = ticket.getPage(0);

        contentStream.beginText();


        float width = getStringWidth(text, fontSize);
        if (width > pageHeight) {
            System.out.println("Warning: name of person "+text+" too long");
        }
        //float centeredY = y - width/2;

        Matrix matrix = Matrix.getRotateInstance(Math.toRadians(90), x, y);
        contentStream.setTextMatrix(matrix);

        contentStream.setNonStrokingColor(Color.WHITE);
        contentStream.setFont(font, fontSize);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();
    }

    private static void addTextAtRatio(PDDocument ticket, String text, float y, float ratio) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(ticket, ticket.getPage(0),
                PDPageContentStream.AppendMode.APPEND,
                true);
        contentStream.beginText();
        float stringWidth = getStringWidth(text, 40);
        contentStream.newLineAtOffset(pageWidth * ratio - stringWidth * (1f/2f), y);
        contentStream.setNonStrokingColor(Color.WHITE);
        contentStream.setFont(font, 40);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();
    }


    private record Rectangle(float x, float y, float width, float height) {

    }

    private static float getStringWidth(String text, int fontSize) throws IOException {
        return PdfBuilder.font.getStringWidth(text) * fontSize / 1000F;
    }
}
