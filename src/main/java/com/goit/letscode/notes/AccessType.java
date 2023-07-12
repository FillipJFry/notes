package com.goit.letscode.notes;

import lombok.Getter;

public enum AccessType {

    PUBLIC("Публічна"), PRIVATE("Приватна");

    @Getter
    private final String typeValue;

    AccessType(String typeValue) {

        this.typeValue = typeValue;
    }
}
