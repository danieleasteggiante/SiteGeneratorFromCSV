package adr.gend.AdrAppProject.service.impl;

import adr.gend.AdrAppProject.entity.Page;
import adr.gend.AdrAppProject.service.ParsingUtility;
import com.opencsv.exceptions.CsvValidationException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Component("page")
public class CSVParserPage extends CSVParserAbstract {
    @Override
    public List<Page> getValue() throws CsvValidationException, IOException {
        String[] line;
        List<Page> values = new ArrayList<>();
        while ((line = this.c.readNext()) != null) {
            ParsingUtility parsingUtility = new ParsingUtility(line[4]);
            values.add(new Page(Integer.valueOf(line[0]),line[1],line[2],line[3],parsingUtility.formatPage()));
        }
        return values;
    }
}
