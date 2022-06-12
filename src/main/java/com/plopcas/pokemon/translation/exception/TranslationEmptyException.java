package com.plopcas.pokemon.translation.exception;

public class TranslationEmptyException extends RuntimeException {
    public TranslationEmptyException(String text) {
        super("Translation for '" + text + "' is empty");
    }
}
