package CodeStore;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CodeStorage {
    private static String codeFile = "";

    public static void appendCodes(Iterable<String> codes) throws IOException {
        codeFile =System.getProperty("user.home") + File.separator + "Documents" + File.separator +"TicketGenerator" ;
        System.out.println(codeFile);

        File file = new File(codeFile);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileWriter fw = new FileWriter(codeFile + File.separator + "GeneratedTickets.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        for (String code : codes) {
            bw.write(code + "\n");
        }
        bw.close();
        fw.close();
    }
}
