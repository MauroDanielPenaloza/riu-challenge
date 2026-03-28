package com.sling.techtest.domain.exception;

public class SearchNotFoundException extends RuntimeException {

    public SearchNotFoundException(String searchId) {
        super(String.format("No se encontró ninguna búsqueda con el identificador: %s", searchId));
    }
}
