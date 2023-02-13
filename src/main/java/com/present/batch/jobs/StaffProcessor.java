package com.present.batch.jobs;

import com.present.batch.Person;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Scope("step")
public class StaffProcessor implements ItemProcessor<Person, ValidEntity<Person>> {

    @Value("#{jobParameters['currentMonth']}")
    private Integer month;

    @Override
    public ValidEntity<Person> process(Person person) throws Exception {
        String fullBirthDay = person.getBirthDay() + "-" + person.getBirthYear();
        LocalDate birthDay = LocalDate.parse(fullBirthDay, DateTimeFormatter.ofPattern("dd-mm-yyyy"));

        return this.month.equals(birthDay.getMonthValue())
              ? new ValidEntity<>(person, Boolean.TRUE)
              : new ValidEntity<>(person, Boolean.FALSE);
    }
}
