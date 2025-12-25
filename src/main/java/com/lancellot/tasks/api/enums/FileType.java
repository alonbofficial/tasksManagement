package com.lancellot.tasks.api.enums;

import lombok.Getter;

@Getter
public enum FileType {

    IMG("img"),
    TXT("txt"),
    CSV("csv");

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    public static FileType fromString(String s) {
        if (s == null) {
            throw new IllegalArgumentException("FileType cannot be null");
        }
        switch (s.trim().toLowerCase()) {
            case "img":
                return IMG;
            case "txt":
                return TXT;
            case "csv":
                return CSV;
            default:
                throw new IllegalArgumentException("Unknown FileType: " + s);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
