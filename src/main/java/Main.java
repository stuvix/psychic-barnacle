import CsvReader.CsvReader;
import com.opencsv.exceptions.CsvException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CsvException {
        testCSV();
    }

    private static void testCSV() throws IOException, CsvException{
        CsvReader.readCsv("src/main/resources/example.csv").forEach(System.out::println);
        System.out.println("\n----------------------- \n");
        CsvReader.readCsv("src/main/resources/exampleWithHeader.csv", true).forEach(System.out::println);
    }
}
