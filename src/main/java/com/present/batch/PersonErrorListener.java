package com.present.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.SkipListenerSupport;

@Slf4j
public class PersonErrorListener extends SkipListenerSupport<Person, Person> {

    @Override
    public void onSkipInProcess(Person person, Throwable throwable) {
        log.error("Invalid person data -> {}", person.toString());
    }
}
