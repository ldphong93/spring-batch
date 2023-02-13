package com.present.batch.jobs;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.present.batch.Person;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CSVStaffWriter implements ItemWriter<ValidEntity<Person>> {

    private StatefulBeanToCsv csvWriter;

    @PostConstruct
    void init() throws IOException {
        Writer writer = Files.newBufferedWriter(Paths.get("birthDayStaff.csv"));

        this.csvWriter = new StatefulBeanToCsvBuilder(writer)
              .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
              .withSeparator(',')
              .build();
    }

    @Override
    public void write(List<? extends ValidEntity<Person>> list) throws Exception {

        List<Person> staffToWrite = list.stream()
              .filter(validEntity -> validEntity.isValid())
              .map(ValidEntity::getEntity)
              .collect(Collectors.toList());

        csvWriter.write(staffToWrite);
    }
}
