package com.present.batch;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class Person {

    @CsvBindByPosition(position = 0)
    private String lastName;

    @CsvBindByPosition(position = 1)
    private String firstName;

    @CsvBindByPosition(position = 2)
    private String birthDay;

    @CsvBindByPosition(position = 3)
    private Integer birthYear;

    @CsvBindByPosition(position = 4)
    private String zodiacSign;

    public Person() {
        this.zodiacSign = "Unknown";
    }

    public Person(String firstName, String lastName, Integer birthYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }
}
