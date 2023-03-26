package com.present.batch.reader;

import com.present.batch.entity.Person;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class ItemReader {

    @StepScope
    @Bean(name = "personItemReader")
    public FlatFileItemReader<Person> read() throws Exception {
        return new FlatFileItemReaderBuilder<Person>()
            .name("personFlatFileReader")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited()
            .names(new String[]{"firstName", "lastName", "birthYear"})
            .fieldSetMapper(fieldSet -> {
                Person ps = new Person();
                ps.setFirstName(fieldSet.readString("firstName"));
                ps.setLastName(fieldSet.readString("lastName"));
                ps.setBirthYear(fieldSet.readInt("birthYear"));
                return ps;
            })
            .build();
    }

}
