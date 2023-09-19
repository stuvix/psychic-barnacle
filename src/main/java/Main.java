import CsvReader.CsvReader;
import PdfCreation.PdfBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CsvException {
        //testCSV();
        testPDF();
    }

    private static void testCSV() throws IOException, CsvException{
        CsvReader.readCsv("src/main/resources/example.csv").forEach(System.out::println);
        System.out.println("\n----------------------- \n");
        CsvReader.readCsv("src/main/resources/exampleWithHeader.csv", true).forEach(System.out::println);
    }

    private static void testPDF() throws IOException {
        PdfBuilder.createTicket("Michel", "Max", "out/out.pdf");
    }
}
