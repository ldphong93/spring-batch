package com.present.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Person {
    private String lastName;
    private String firstName;
    private Integer birthYear;
    private String zodiacSign;

    public Person() {
        this.zodiacSign = "Unknown";
    }

    public Person(String firstName, String lastName, Integer birthYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthYear = birthYear;
    }

    @Override
    public String toString() {
        return "FirstName: " + firstName + ", LastName: " + lastName + ", ZodiacSign: " + zodiacSign;
    }

}
