package com.orca.attack.orca.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrcaException extends RuntimeException {
    private String message;

    public OrcaException(String message) {
        super(message);
        this.message = message;
    }

}
