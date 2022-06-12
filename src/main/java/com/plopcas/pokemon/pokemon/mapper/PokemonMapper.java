package com.plopcas.pokemon.pokemon.mapper;

import com.plopcas.pokemon.pokemon.dto.PokemonDTO;
import com.plopcas.pokemon.pokemon.exception.PokemonMapperException;
import com.plopcas.pokemon.pokemon.model.FlavorText;
import com.plopcas.pokemon.pokemon.model.PokemonSpecies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PokemonMapper {

    private static final Logger log = LoggerFactory.getLogger(PokemonMapper.class);

    public PokemonDTO toDto(PokemonSpecies pokemonSpecies) {

        if (pokemonSpecies == null) {
            String errorMessage = "Argument cannot be null";
            log.error(errorMessage);
            throw new PokemonMapperException(errorMessage);
        }

        PokemonDTO pokemonDTO = new PokemonDTO();
        pokemonDTO.setName(pokemonSpecies.getName());
        if (pokemonSpecies.getFlavorTextEntries() != null) {
            Optional<FlavorText> flavorText = pokemonSpecies
                    .getFlavorTextEntries()
                    .stream()
                    .filter(entry -> "en".equals(entry.getLanguage().getName()))
                    .findAny();
            if (flavorText.isPresent()) {
                pokemonDTO.setDescription(flavorText
                        .get()
                        .getFlavorText()
                        .replace("\n", " ")
                        .replace("\f", " "));
            }
        }
        if (pokemonSpecies.getPokemonHabitat() != null) {
            pokemonDTO.setHabitat(pokemonSpecies.getPokemonHabitat().getName());
        }
        pokemonDTO.setIsLegendary(pokemonSpecies.getIsLegendary());

        return pokemonDTO;
    }
}
