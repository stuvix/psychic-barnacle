package CsvReader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {
    // set this to any non-standard separator
    private static final char separator = ',';

    public static List<Compound> readCsv(String filePath) throws IOException, CsvException{
        return readCsv(filePath, false);
    }

    /**
     * reads a csv file and returns a list with compounds of the names.
     * @param filePath path to csv file with firstName, lastName in it.
     * @param csvHasHeader the first line in csv files is often used to store column names. If this is the case, set to true.
     *                     Set to false if first line contains data.
     * @return List with names.
     * @throws IOException mostly FileNotFoundException I suppose
     * @throws CsvException did not read the doc, so idk when this happens
     */
    public static List<Compound> readCsv(String filePath, boolean csvHasHeader) throws IOException, CsvException {
        try (FileReader fileReader = new FileReader(filePath)) {
            // only needed if non-comma separator is used.
            CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            try (CSVReader csvReader = new CSVReaderBuilder(fileReader)
                    .withSkipLines(csvHasHeader ? 1 : 0)
                    .withCSVParser(parser)
                    .build()) {

                return csvReader.readAll()
                        .stream()
                        .map(Compound::new)
                        .toList();
            }
        }
    }
}
