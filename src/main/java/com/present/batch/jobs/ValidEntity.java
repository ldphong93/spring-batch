package com.present.batch.jobs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidEntity<T> {
    private T entity;
    private boolean isValid;
}
