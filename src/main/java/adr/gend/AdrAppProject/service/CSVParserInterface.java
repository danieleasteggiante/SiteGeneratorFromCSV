package adr.gend.AdrAppProject.service;

import adr.gend.AdrAppProject.entity.Post;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Component
public interface CSVParserInterface {
    CSVParserInterface parse(File f) throws IOException, CsvValidationException;
    <T> List<T> getValue() throws CsvValidationException, IOException;
}
