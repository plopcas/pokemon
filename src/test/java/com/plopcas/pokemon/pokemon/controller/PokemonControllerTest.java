package com.plopcas.pokemon.pokemon.controller;

import com.plopcas.pokemon.pokemon.dto.PokemonDTO;
import com.plopcas.pokemon.pokemon.exception.PokemonSpeciesNotFoundException;
import com.plopcas.pokemon.pokemon.exception.PokemonSpeciesUnknownException;
import com.plopcas.pokemon.pokemon.mapper.PokemonMapper;
import com.plopcas.pokemon.pokemon.model.FlavorText;
import com.plopcas.pokemon.pokemon.model.Language;
import com.plopcas.pokemon.pokemon.model.PokemonHabitat;
import com.plopcas.pokemon.pokemon.model.PokemonSpecies;
import com.plopcas.pokemon.pokemon.service.PokemonService;
import com.plopcas.pokemon.translation.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PokemonControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PokemonService pokemonService;
    @MockBean
    private PokemonMapper pokemonMapper;
    @MockBean
    private TranslationService translationService;

    @Test
    void getBasicPokemonInfoShouldReturn200AndBasicInfo() throws Exception {

        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(false);
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "bar", false);

        when(pokemonService.findByName("foo")).thenReturn(foo);
        when(pokemonMapper.toDto(foo)).thenReturn(fooDto);

        this.mockMvc.perform(get("/pokemon/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'name':'foo','description':'foobar','habitat':'bar','isLegendary':false}"));

        verify(pokemonService, times(1)).findByName("foo");
        verify(pokemonMapper, times(1)).toDto(any());
        verify(translationService, never()).translate(any());
    }

    @Test
    void getBasicPokemonInfoShouldReturn404AWhenNotFound() throws Exception {

        when(pokemonService.findByName("foo")).thenThrow(new PokemonSpeciesNotFoundException("foo"));

        this.mockMvc.perform(get("/pokemon/foo"))
                .andExpect(status().isNotFound());

        verify(pokemonService, times(1)).findByName("foo");
        verify(pokemonMapper, never()).toDto(any());
        verify(translationService, never()).translate(any());
    }

    @Test
    void getBasicPokemonInfoShouldReturn500AWhenUnknownError() throws Exception {

        when(pokemonService.findByName("foo")).thenThrow(new PokemonSpeciesUnknownException("foo"));

        this.mockMvc.perform(get("/pokemon/foo"))
                .andExpect(status().isInternalServerError());

        verify(pokemonService, times(1)).findByName("foo");
        verify(pokemonMapper, never()).toDto(any());
        verify(translationService, never()).translate(any());
    }

    @Test
    void getTranslatedPokemonInfoShouldReturn200AndBasicInfo() throws Exception {

        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(false);
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "bar", false);
        PokemonDTO fooDtoTranslated = new PokemonDTO("foo", "foobar-tr", "bar", false);

        when(pokemonService.findByName("foo")).thenReturn(foo);
        when(pokemonMapper.toDto(foo)).thenReturn(fooDto);
        when(translationService.translate(fooDto)).thenReturn(fooDtoTranslated);

        this.mockMvc.perform(get("/pokemon/translated/foo"))
                .andExpect(status().isOk())
                .andExpect(content().json("{'name':'foo','description':'foobar-tr','habitat':'bar','isLegendary':false}"));

        verify(pokemonService, times(1)).findByName("foo");
        verify(pokemonMapper, times(1)).toDto(any());
        verify(translationService, times(1)).translate(any());
    }

    @Test
    void getTranslatedPokemonInfoShouldReturn404AWhenNotFound() throws Exception {

        when(pokemonService.findByName("foo")).thenThrow(new PokemonSpeciesNotFoundException("foo"));

        this.mockMvc.perform(get("/pokemon/translated/foo"))
                .andExpect(status().isNotFound());

        verify(pokemonService, times(1)).findByName("foo");
        verify(pokemonMapper, never()).toDto(any());
        verify(translationService, never()).translate(any());
    }

    @Test
    void getTranslatedPokemonInfoShouldReturn500AWhenUnknownError() throws Exception {

        when(pokemonService.findByName("foo")).thenThrow(new PokemonSpeciesUnknownException("foo"));

        this.mockMvc.perform(get("/pokemon/translated/foo"))
                .andExpect(status().isInternalServerError());

        verify(pokemonService, times(1)).findByName("foo");
        verify(pokemonMapper, never()).toDto(any());
        verify(translationService, never()).translate(any());
    }
}