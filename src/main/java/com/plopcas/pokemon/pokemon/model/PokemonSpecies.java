package com.plopcas.pokemon.pokemon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class PokemonSpecies {

    private Integer id;
    private String name;
    @JsonProperty("is_legendary")
    private Boolean isLegendary;
    @JsonProperty("flavor_text_entries")
    private List<FlavorText> flavorTextEntries;
    @JsonProperty("habitat")
    private PokemonHabitat pokemonHabitat;
}
