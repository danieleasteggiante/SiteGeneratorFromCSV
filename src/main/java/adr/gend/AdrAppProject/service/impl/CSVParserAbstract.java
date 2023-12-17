package adr.gend.AdrAppProject.service.impl;

import adr.gend.AdrAppProject.service.CSVParserInterface;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class CSVParserAbstract implements CSVParserInterface {
    public CSVReader c;

    @Override
    public CSVParserInterface parse(File f) throws IOException, CsvValidationException {
        if (f != null) {
            this.c =
                    new CSVReaderBuilder(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8))
                            .withSkipLines(1)
                            .withCSVParser(new CSVParserBuilder()
                                    .withIgnoreQuotations(true)
                                    .withSeparator(';')
                                    .build())
                            .build();
        }
        return this;
    }

    @Override
    abstract public <T> List<T> getValue() throws CsvValidationException, IOException;
}

