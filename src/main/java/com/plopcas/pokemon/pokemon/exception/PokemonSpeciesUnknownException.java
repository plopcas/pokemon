package com.plopcas.pokemon.pokemon.exception;

public class PokemonSpeciesUnknownException extends RuntimeException {

    public PokemonSpeciesUnknownException(String name) {
        super("Unknown error when trying to find " + name);
    }
}
