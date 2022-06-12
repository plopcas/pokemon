package com.plopcas.pokemon.pokemon.repository;

import com.plopcas.pokemon.pokemon.exception.PokemonSpeciesUnknownException;
import com.plopcas.pokemon.pokemon.exception.PokemonSpeciesNotFoundException;
import com.plopcas.pokemon.pokemon.model.PokemonSpecies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Repository
public class PokemonRepository {

    private static final Logger log = LoggerFactory.getLogger(PokemonRepository.class);

    @Value("${pokeApi.url}")
    private String url;

    private final RestTemplate restTemplate;

    public PokemonRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PokemonSpecies findByName(String name) {
        try {
            return restTemplate.getForObject(url + "/{name}", PokemonSpecies.class, name);
        } catch (HttpStatusCodeException httpException) {
            // add as many statuses as we care about
            if (httpException.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new PokemonSpeciesNotFoundException(name);
            } else {
                log.error("Unexpected error finding by name %s : %s - %s", name, httpException.getStatusCode(), httpException.getMessage());
                throw new PokemonSpeciesUnknownException(name);
            }
        }
    }
}
