package com.present.batch.config;

import com.present.batch.entity.Person;
import com.present.batch.listener.JobCompletionListener;
import com.present.batch.listener.PersonSkipListener;
import com.present.batch.processor.PersonItemProcessor;
import java.util.concurrent.TimeoutException;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("personItemReader")
    public FlatFileItemReader<Person> reader;

    @Bean
    public Job importUserJob(JobCompletionListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
            .<Person, Person>chunk(1)
            .reader(reader)
            .processor(processor())
            .writer(writer)

            //skip
            .faultTolerant()
            .skip(TimeoutException.class).skipLimit(5)
            .listener(new PersonSkipListener())

            //re-try
            //re-try limit can be interpreted as maximum number of job run
//            .faultTolerant()
//            .retryLimit(3)
//            .retry(TimeoutException.class)
//            .listener(new PersonRetryListener())

            //restart
//            .startLimit(5)

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
            .sql(
                "INSERT INTO people (first_name, last_name, birth_year, zodiac_sign) VALUES (:firstName, :lastName, :birthYear, :zodiacSign)")
            .dataSource(dataSource)
            .build();
    }
}

