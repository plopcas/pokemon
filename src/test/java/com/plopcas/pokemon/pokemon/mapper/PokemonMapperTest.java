package com.plopcas.pokemon.pokemon.mapper;

import com.plopcas.pokemon.pokemon.dto.PokemonDTO;
import com.plopcas.pokemon.pokemon.exception.PokemonMapperException;
import com.plopcas.pokemon.pokemon.model.FlavorText;
import com.plopcas.pokemon.pokemon.model.Language;
import com.plopcas.pokemon.pokemon.model.PokemonHabitat;
import com.plopcas.pokemon.pokemon.model.PokemonSpecies;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PokemonMapperTest {

    @Test
    void toDtoShouldReturnDTO() {

        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(false);
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "bar", false);

        PokemonDTO pokemonDTO = new PokemonMapper().toDto(foo);

        assertThat(pokemonDTO).usingRecursiveComparison().isEqualTo(fooDto);
    }

    @Test
    void toDtoShouldThrowExceptionWhenNullInput() {

        Exception exception = assertThrows(PokemonMapperException.class, () -> {
            new PokemonMapper().toDto(null);
        });

        assertThat(exception.getMessage()).isEqualTo("Argument cannot be null");
    }

    @Test
    void toDtoShouldReturnIncompleteDTOIfNameIsMissing() {
        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName(null);
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(false);
        PokemonDTO fooDto = new PokemonDTO(null, "foobar", "bar", false);

        PokemonDTO pokemonDTO = new PokemonMapper().toDto(foo);

        assertThat(pokemonDTO).usingRecursiveComparison().isEqualTo(fooDto);
    }

    @Test
    void toDtoShouldReturnIncompleteDTOIfDescriptionIsMissing() {
        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(null);
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(false);
        PokemonDTO fooDto = new PokemonDTO("foo", null, "bar", false);

        PokemonDTO pokemonDTO = new PokemonMapper().toDto(foo);

        assertThat(pokemonDTO).usingRecursiveComparison().isEqualTo(fooDto);
    }

    @Test
    void toDtoShouldReturnIncompleteDTOIfHabitatIsMissing() {
        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(null);
        foo.setIsLegendary(false);
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", null, false);

        PokemonDTO pokemonDTO = new PokemonMapper().toDto(foo);

        assertThat(pokemonDTO).usingRecursiveComparison().isEqualTo(fooDto);
    }

    @Test
    void toDtoShouldReturnIncompleteDTOIfIsLegendaryIsMissing() {
        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(null);
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "bar", null);

        PokemonDTO pokemonDTO = new PokemonMapper().toDto(foo);

        assertThat(pokemonDTO).usingRecursiveComparison().isEqualTo(fooDto);
    }
}