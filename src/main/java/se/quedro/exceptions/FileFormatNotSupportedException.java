package se.quedro.exceptions;

public class FileFormatNotSupportedException extends RuntimeException {

    public FileFormatNotSupportedException(String errorMessage) {
        super(errorMessage);
    }
}
