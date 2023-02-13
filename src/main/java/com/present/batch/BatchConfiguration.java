package com.present.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
              .name("personItemReader")
              .resource(new ClassPathResource("sample-data.csv"))
              .delimited()
              .names(new String[]{"firstName", "lastName", "birthDay", "birthYear"})
              .fieldSetMapper(fieldSet -> Person.builder()
                    .firstName(fieldSet.readString("firstName"))
                    .lastName(fieldSet.readString("lastName"))
                    .birthDay(fieldSet.readString("birthDay"))
                    .birthYear(fieldSet.readInt("birthYear"))
                    .build())
              .build();
    }

    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Person> personWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
              .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
              .sql("INSERT INTO people (first_name, last_name, birth_day, birth_year, zodiac_sign) VALUES (:firstName, :lastName, :birthDay, :birthYear, :zodiacSign)")
              .dataSource(dataSource)
              .build();
    }

    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, @Qualifier("attachZodiacSign") Step step1, @Qualifier("getStaffBDInMonth") Step getBDInMonthStep) {
        return jobBuilderFactory.get("importUserJob")
              .incrementer(new RunIdIncrementer())
              .listener(listener)
              .start(step1).next(getBDInMonthStep)
              .build();
    }

    @Bean(name = "attachZodiacSign")
    public Step attachZodiacSignStep(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
              .<Person, Person>chunk(10)
              .reader(reader())
              .processor(processor())
              .writer(writer)
              .build();
    }

}