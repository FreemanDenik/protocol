package com.execute.protocol.core.exeptions;

import java.time.DateTimeException;

public class DateConvertException extends DateTimeException {
    public DateConvertException(String message) {
        super(message);
    }
}
