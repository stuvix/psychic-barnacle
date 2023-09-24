import CodeStore.CodeStorage;
import CsvReader.*;
import PDFCode417Creation.PDF147Generator;
import PdfCreation.PdfBuilder;
import com.opencsv.exceptions.CsvException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, CsvException {
//        testPDF();
        try {
            JFileChooser openJFileChooser = new JFileChooser("/home/michel/IdeaProjects/psychic-barnacle/src/main/resources");
            int openJFileChooserResult = openJFileChooser.showOpenDialog(null);
            if (openJFileChooserResult == JFileChooser.APPROVE_OPTION) {

                JFileChooser saveJFileChooser = new JFileChooser();
                int saveJFileChooserResult = saveJFileChooser.showSaveDialog(null);
                String savePath = saveJFileChooser.getCurrentDirectory().getAbsolutePath();

                if(saveJFileChooserResult != JFileChooser.APPROVE_OPTION){
                    String userHome = System.getProperty("user.home");

                    savePath = userHome + File.separator +"Downloads" + File.separator + "Tickets";

                    File folder = new File(savePath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }
                }

                List<Compound> names = CsvReader.readCsv(openJFileChooser.getSelectedFile().getAbsolutePath(),
                        "Numm a Virnumm",
                        "Wéi vill Ticketen wëlls du?",
                        "Ticket erstallt"
                );

                ArrayList<String> codes = new ArrayList<>();
                for (Compound compound : names) {
                    for (int i = 0; i < compound.amount; i++) {
                        String code = PDF147Generator.generateCode();
                        codes.add(code);
                        PdfBuilder.createTicket(
                                compound.name,
                                savePath + "/"+compound.name+ (compound.amount>1 ? i+1 : "") +".pdf",
                                PDF147Generator.generatePDF147(code),
                                code
                        );
                    }
                }
                CodeStorage.appendCodes(codes);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

//    private static void testPDF() throws IOException {
//        BufferedImage qrCode = ImageIO.read(new File("src/main/resources/barCode.png"));
//        PdfBuilder.createTicket("Michel Max", "out/out.pdf", PDF147Generator.generatePDF147(PDF147Generator.generateCode()));
//    }

}
