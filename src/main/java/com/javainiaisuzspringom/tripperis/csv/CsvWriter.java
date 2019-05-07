package com.javainiaisuzspringom.tripperis.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CsvWriter implements Writer {

    public void writeDataToFile(String[] headers, List<String[]> csvLines, PrintWriter writer) throws IOException {

            CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(headers));
            printer.printRecord("test1", "test2");

            csvLines.forEach(line -> {
                try {
                    printer.printRecord(line);
                } catch (IOException e) {
                    throw new RuntimeException("err.csv.creation", e);
                }
            });

            printer.flush();
    }
}
