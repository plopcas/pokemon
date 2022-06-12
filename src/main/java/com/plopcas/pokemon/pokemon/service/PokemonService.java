package com.plopcas.pokemon.pokemon.service;

import com.plopcas.pokemon.pokemon.repository.PokemonRepository;
import com.plopcas.pokemon.pokemon.model.PokemonSpecies;
import org.springframework.stereotype.Service;

@Service
public class PokemonService {

    private PokemonRepository pokemonRepository;

    public PokemonService(PokemonRepository pokemonRepository) {
        this.pokemonRepository = pokemonRepository;
    }

    public PokemonSpecies findByName(String name) {
        return pokemonRepository.findByName(name);
    }
}
