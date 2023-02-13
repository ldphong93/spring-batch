package com.present.batch.jobs;

import com.present.batch.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class GetStaffHavingBDInMonth {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean(name = "getStaffBDInMonth")
    public Step getStaffBDInMonth() {
        return this.stepBuilderFactory.get("queryStep")
              .<Person, ValidEntity<Person>>chunk(10)
              .reader(this.personReader())
              .processor(new StaffProcessor())
              .writer(new CSVStaffWriter())
              .build();
    }

    private JdbcCursorItemReader<Person> personReader() {
        return new JdbcCursorItemReaderBuilder<Person>()
              .name("personReader")
              .dataSource(this.dataSource)
              .sql("select * from people")
              .rowMapper((rs, rowNum) -> Person.builder()
                    .firstName(rs.getString("last_name"))
                    .lastName(rs.getString("first_name"))
                    .zodiacSign(rs.getString("zodiac_sign"))
                    .birthDay(rs.getString("birth_day"))
                    .build())
              .build();
    }
}
