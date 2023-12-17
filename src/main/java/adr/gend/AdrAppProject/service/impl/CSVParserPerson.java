package adr.gend.AdrAppProject.service.impl;

import adr.gend.AdrAppProject.entity.Person;
import adr.gend.AdrAppProject.service.ParsingUtility;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Primary
@Component("person")
public class CSVParserPerson extends CSVParserAbstract {
    @Override
    public List<Person> getValue() throws CsvValidationException, IOException {
        String[] line;
        List<Person> values = new ArrayList<>();
        while ((line = this.c.readNext()) != null) {
            ParsingUtility parsingUtility = new ParsingUtility(line[4]);
            values.add(new Person(Integer.valueOf(line[0]),line[1],line[2],line[3], parsingUtility.formatPage(), line[5]));
        }
        return values;
    }
}
