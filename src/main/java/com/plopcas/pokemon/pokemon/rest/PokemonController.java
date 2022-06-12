package com.plopcas.pokemon.pokemon.rest;

import com.plopcas.pokemon.pokemon.dto.PokemonDTO;
import com.plopcas.pokemon.pokemon.exception.PokemonSpeciesNotFoundException;
import com.plopcas.pokemon.pokemon.mapper.PokemonMapper;
import com.plopcas.pokemon.pokemon.service.PokemonService;
import com.plopcas.pokemon.translation.service.TranslationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class PokemonController {

    private PokemonService pokemonService;
    private PokemonMapper pokemonMapper;
    private TranslationService translationService;

    public PokemonController(PokemonService pokemonService, PokemonMapper pokemonMapper, TranslationService translationService) {
        this.pokemonService = pokemonService;
        this.pokemonMapper = pokemonMapper;
        this.translationService = translationService;
    }

    @GetMapping("/pokemon/{name}")
    @ResponseBody
    public PokemonDTO getBasicPokemonInfo(@PathVariable String name) {
        try {
            return pokemonMapper.toDto(pokemonService.findByName(name));
        } catch (PokemonSpeciesNotFoundException httpException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpException.getMessage());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pokemon/translated/{name}")
    @ResponseBody
    public PokemonDTO getTranslatedPokemonInfo(@PathVariable String name) {
        try {
            return translationService.translate(pokemonMapper.toDto(pokemonService.findByName(name)));
        } catch (PokemonSpeciesNotFoundException httpException) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, httpException.getMessage());
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}