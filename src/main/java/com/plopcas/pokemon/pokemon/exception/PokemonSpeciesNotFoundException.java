package com.plopcas.pokemon.pokemon.exception;

public class PokemonSpeciesNotFoundException extends RuntimeException {

    public PokemonSpeciesNotFoundException(String name) {
        super(name + " not found");
    }
}
