package PDFCode417Creation;

import org.krysalis.barcode4j.impl.pdf417.PDF417Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import java.awt.image.BufferedImage;
import java.io.*;

public class PDF147Generator {
    public static String generateCode(){
        String code = Long.toString(System.currentTimeMillis()*27);
        System.out.println(code);
        return code;
    }

    public static BufferedImage generatePDF147(String code) {
        try {
            PDF417Bean pdf417Bean = new PDF417Bean();
            final int dpi = 300;
            pdf417Bean.setModuleWidth(0.7);
            pdf417Bean.doQuietZone(false);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi, BufferedImage.TYPE_BYTE_BINARY, false, 90);
            pdf417Bean.generateBarcode(canvas, code);
            canvas.finish();
            System.out.println("PDF417-Barcode wurde erfolgreich erstellt.");
            return canvas.getBufferedImage();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
