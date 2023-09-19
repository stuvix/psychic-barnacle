package PdfCreation;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.*;
import java.io.IOException;

import static org.apache.pdfbox.pdmodel.font.PDType1Font.TIMES_ROMAN;

public class PdfBuilder {
    private static final float pageWidth = 1800;
    private static final float pageHeight = 600;

    private static final String templatePath = "src/main/resources/ticket_template.pdf";
    private static final String resPrefix = "src/main/resources/";
    private static final String bgPath = resPrefix + "bg.jpg";
    private static final String acelPath = resPrefix + "acel.png";
    private static final String logoPath = resPrefix + "logo.jpg";

    private static final String qrPath = resPrefix + "qr.png";


    private static final String location = "Grizzly @ Clausen";
    private static final String time = "21:00";

    public static void createTicket(String firstName, String lastName, String saveTo) throws IOException {
        try (PDDocument ticket = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
            ticket.addPage(page);


            addImageToBG(ticket, bgPath, new Rectangle(0,0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight()));

            addImageAsOverlay(ticket, acelPath, 50, 50, 100);

            float logoWidth = 1000;
            addImageAsOverlay(ticket, logoPath, (pageWidth - logoWidth) / 2, pageHeight * 0.35f, logoWidth);



            addTextCentered(ticket, "Michel Max", pageHeight * 0.1f);
            addText(ticket, "Location:" ,pageWidth * 0.3f, pageHeight * 0.27f);
            addText(ticket, location, pageWidth * 0.3f, pageHeight * 0.2f);

            addText(ticket, "Time:", pageWidth * 0.6f, pageHeight * 0.27f);
            addText(ticket, time, pageWidth * 0.6f, pageHeight * 0.2f);


            addImageAsOverlay(ticket, qrPath, pageWidth * 0.8f, pageHeight * 0.1f, 250f);

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
     * @param ticket
     * @param path
     * @param x
     * @param y
     * @param width
     * @throws IOException
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

    private static void addText(PDDocument ticket, String text, float x, float y) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(ticket, ticket.getPage(0),
                PDPageContentStream.AppendMode.APPEND,
                true);
        contentStream.beginText();
        contentStream.newLineAtOffset(x,y);
        contentStream.setNonStrokingColor(Color.WHITE);
        contentStream.setFont(TIMES_ROMAN, 40);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();
    }

    private static void addTextCentered(PDDocument ticket, String text, float y) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(ticket, ticket.getPage(0),
                PDPageContentStream.AppendMode.APPEND,
                true);
        contentStream.beginText();
        float stringWidth = getStringWidth(text, TIMES_ROMAN, 40);
        contentStream.newLineAtOffset((pageWidth - stringWidth) / 2, y);
        contentStream.setNonStrokingColor(Color.WHITE);
        contentStream.setFont(TIMES_ROMAN, 40);
        contentStream.showText(text);
        contentStream.endText();
        contentStream.close();
    }


    private record Rectangle(float x, float y, float width, float height) {

    }

    private static float getStringWidth(String text, PDFont font, int fontSize) throws IOException {
        return font.getStringWidth(text) * fontSize / 1000F;
    }
}