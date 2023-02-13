package com.present.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import java.util.HashMap;
import java.util.Map;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(final Person person) throws Exception {


        Person transformedPerson = turnUpperCase(person);
        String zodiacSign = findZodiacSign(person.getBirthYear());

        transformedPerson.setZodiacSign(zodiacSign);

        log.info("Converting (" + person + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }

    private String findZodiacSign(Integer yearOfBirth) {
        Map<Integer, String> zodiacMap = new HashMap<>();
        zodiacMap.put(0, "RAT");
        zodiacMap.put(1, "OX");
        zodiacMap.put(2, "TIGER");
        zodiacMap.put(3, "CAT");
        zodiacMap.put(4, "DRAGON");
        zodiacMap.put(5, "SNAKE");
        zodiacMap.put(6, "HORSE");
        zodiacMap.put(7, "GOAT");
        zodiacMap.put(8, "MONKEY");
        zodiacMap.put(9, "ROOSTER");
        zodiacMap.put(10, "DOG");
        zodiacMap.put(11, "PIG");

        int signPosition = (yearOfBirth - 1984) % 12;
        return zodiacMap.get(signPosition);
    }

    private Person turnUpperCase(Person person) {
        String firstName = person.getFirstName().toUpperCase();
        String lastName = person.getLastName().toUpperCase();
        return new Person(firstName, lastName, person.getBirthYear());
    }

}
