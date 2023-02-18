package com.present.batch;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class InvalidPersonException extends RuntimeException{
    private List<String> violations;

    public InvalidPersonException(String message, List<String> violations) {
        super(message);
        this.violations = violations;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + ": " + this.violations.stream()
              .collect(Collectors.joining(", "));
    }
}
