package adr.gend.AdrAppProject.service.impl;

import adr.gend.AdrAppProject.entity.Page;
import adr.gend.AdrAppProject.entity.Post;
import adr.gend.AdrAppProject.service.ParsingUtility;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component("blog")
public class CSVParserBlog extends CSVParserAbstract{

    @Override
    public List<Post> getValue() throws CsvValidationException, IOException {
        String[] line;
        List<Post> values = new ArrayList<>();
        while ((line = this.c.readNext()) != null) {
            ParsingUtility parsingUtility = new ParsingUtility(line[3]);
            values.add(new Post(Integer.valueOf(line[0]),LocalDate.now(),line[1],line[2],parsingUtility.formatPage()));
        }
        return values;
    }
}
