package ru.tennis.validation;

import java.util.List;

public record ValidationResult(List<String> errors, String normalName1, String normalName2) {

    public ValidationResult(List<String> errors, String normalName1, String normalName2) {
        this.errors = List.copyOf(errors);
        this.normalName1 = normalName1;
        this.normalName2 = normalName2;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
