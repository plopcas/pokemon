package com.plopcas.pokemon.pokemon.service;

import com.plopcas.pokemon.pokemon.model.FlavorText;
import com.plopcas.pokemon.pokemon.model.Language;
import com.plopcas.pokemon.pokemon.model.PokemonHabitat;
import com.plopcas.pokemon.pokemon.model.PokemonSpecies;
import com.plopcas.pokemon.pokemon.repository.PokemonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokemonRepository pokemonRepository;
    @InjectMocks
    private PokemonService pokemonService;

    @Test
    public void findByNameShouldReturnMockedObject(){

        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(false);

        when(pokemonRepository.findByName("foo")).thenReturn(foo);

        PokemonSpecies result = pokemonService.findByName("foo");

        assertEquals(foo, result);
        verify(pokemonRepository, times(1)).findByName("foo");
    }
}