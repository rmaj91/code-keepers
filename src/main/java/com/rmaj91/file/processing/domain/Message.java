package com.rmaj91.file.processing.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

    private String message;

    public static Message of(String message) {
        return new Message(message);
    }
}
