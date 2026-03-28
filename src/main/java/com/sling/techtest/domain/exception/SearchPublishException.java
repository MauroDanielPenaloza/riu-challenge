package com.sling.techtest.domain.exception;

public class SearchPublishException extends RuntimeException {

    public SearchPublishException(String searchId, Throwable cause) {
        super(String.format("Error al publicar la búsqueda con identificador: %s", searchId), cause);
    }
}
