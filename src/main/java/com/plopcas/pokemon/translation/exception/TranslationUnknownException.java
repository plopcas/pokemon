package com.plopcas.pokemon.translation.exception;

public class TranslationUnknownException extends RuntimeException {
    public TranslationUnknownException(String text) {
        super("Unknown error trying to translate '" + text + "'");
    }
}
