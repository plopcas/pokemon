package com.plopcas.pokemon.pokemon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PokemonDTO {

    private String name;
    private String description;
    private String habitat;
    private Boolean isLegendary;
}
