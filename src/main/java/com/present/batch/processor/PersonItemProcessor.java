package com.present.batch.processor;

import com.present.batch.entity.Person;
import com.present.batch.exception.InvalidPersonException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    public static int counter = 0;

    @Override
    public Person process(final Person person) throws Exception {

        List<String> validateResults = this.validatePerson(person);
        if (!validateResults.isEmpty()) {
            throw new InvalidPersonException("Invalid person!!!", validateResults);
        }

        //retry
//        PersonItemProcessor.counter += 1;
//        if (PersonItemProcessor.counter < 3) {
//            throw new TimeoutException("Timeout Exception");
//        }

        //restart
        PersonItemProcessor.counter += 1;
        if (PersonItemProcessor.counter == 3) {
            throw new TimeoutException("Timeout Exception");
        }

        if (PersonItemProcessor.counter == 4) {
            jdbcTemplate.query("SELECT last_name, first_name, birth_year, zodiac_sign FROM people",
                (rs, row) -> new Person(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getString(4))
            ).forEach(p -> log.info("Found <" + p + "> in the database."));
        }

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

    public List<String> validatePerson(Person person) {
        return Stream
            .of(
                Pair.of("FirstName", person.getFirstName()),
                Pair.of("LastName", person.getLastName()))
            .map(value ->
                StringUtils.isNotBlank(value.getRight())
                    ? StringUtils.EMPTY
                    : String.format("%s is empty/blank", value.getLeft()))
            .filter(StringUtils::isNotEmpty)
            .collect(Collectors.toList());
    }
}
