package com.goit.letscode.notes.data;

import lombok.Getter;

public enum AccessType {

    PUBLIC("Публічна"), PRIVATE("Приватна");

    @Getter
    private final String typeValue;

    AccessType(String typeValue) {

        this.typeValue = typeValue;
    }
}
