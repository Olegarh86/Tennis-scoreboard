package ru.tennis.validation;

import java.util.List;

public record ValidationResult(List<String> errors, String normalizedName1, String normalizedName2) {

    public ValidationResult(List<String> errors, String normalizedName1, String normalizedName2) {
        this.errors = List.copyOf(errors);
        this.normalizedName1 = normalizedName1;
        this.normalizedName2 = normalizedName2;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}
