package CsvReader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvReader {
    // set this to any non-standard separator
    private static final char separator = ',';



    /**
     * reads a csv file and returns a list with compounds of the names.
     * @param filePath path to csv file with firstName, lastName in it.
     * @return List with names.
     * @throws IOException mostly FileNotFoundException I suppose
     * @throws CsvException did not read the doc, so idk when this happens
     */
    public static List<Compound> readCsv(String filePath, String headerName, String headerAmount, String headerDoNotGenerate) throws IOException, CsvException {
        try (FileReader fileReader = new FileReader(filePath)) {
            // only needed if non-comma separator is used.
            CSVParser parser = new CSVParserBuilder().withSeparator(separator).build();
            try (CSVReader csvReader = new CSVReaderBuilder(fileReader)
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build()) {

                var allLines = csvReader.readAll();
                int headerIndexName = getHeaderLocation(allLines.get(0), headerName);
                int headerIndexAmount = getHeaderLocation(allLines.get(0), headerAmount);
                int headerIndexDoNotGenerate = getHeaderLocation(allLines.get(0), headerDoNotGenerate);
                allLines.remove(0);

                return allLines
                        .stream()
                        .filter(line -> line[headerIndexDoNotGenerate].strip().isEmpty())
                        .map(line -> new Compound(line[headerIndexName].strip(), Integer.parseInt(line[headerIndexAmount].strip())))
                        .toList();
            }
        }
    }

    private static int getHeaderLocation(String[] headers, String columnName) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equals(columnName)) {
                return i;
            }
        }
        return -1;
    }


}
