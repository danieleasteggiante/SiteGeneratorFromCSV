package adr.gend.AdrAppProject.service.impl;

import adr.gend.AdrAppProject.entity.Home;
import adr.gend.AdrAppProject.entity.Page;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("home")

public class CSVParserHome extends CSVParserAbstract{
    @Override
    public List<Home> getValue() throws CsvValidationException, IOException {
        String[] line;
        List<Home> values = new ArrayList<>();
        while ((line = this.c.readNext()) != null) {
            values.add(new Home(Integer.valueOf(line[0]),line[1],line[2],Boolean.valueOf(line[3])));
        }
        return values;
    }
}
