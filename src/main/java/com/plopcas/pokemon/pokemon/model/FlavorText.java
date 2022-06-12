package com.plopcas.pokemon.pokemon.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FlavorText {

    @JsonProperty("flavor_text")
    private String flavorText;
    private Language language;
}
