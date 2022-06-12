package com.plopcas.pokemon.pokemon.repository;

import com.plopcas.pokemon.pokemon.exception.PokemonSpeciesNotFoundException;
import com.plopcas.pokemon.pokemon.exception.PokemonSpeciesUnknownException;
import com.plopcas.pokemon.pokemon.model.FlavorText;
import com.plopcas.pokemon.pokemon.model.Language;
import com.plopcas.pokemon.pokemon.model.PokemonHabitat;
import com.plopcas.pokemon.pokemon.model.PokemonSpecies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PokemonRepositoryTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private PokemonRepository pokemonRepository;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(pokemonRepository, "url", "http://foo.bar");
    }

    @Test
    public void findByNameShouldReturnMockedObject() {

        PokemonSpecies foo = new PokemonSpecies();
        foo.setId(1);
        foo.setName("foo");
        foo.setFlavorTextEntries(newArrayList(new FlavorText("foobar", new Language("en"))));
        foo.setPokemonHabitat(new PokemonHabitat("bar"));
        foo.setIsLegendary(false);

        when(restTemplate.getForObject("http://foo.bar/{name}", PokemonSpecies.class, "foo")).thenReturn(foo);

        PokemonSpecies result = pokemonRepository.findByName("foo");

        assertEquals(foo, result);
    }

    @Test
    public void findByNameShouldThrowExceptionIfNotFound() {

        when(restTemplate.getForObject("http://foo.bar/{name}", PokemonSpecies.class, "bar"))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        Exception exception = assertThrows(PokemonSpeciesNotFoundException.class, () -> {
            pokemonRepository.findByName("bar");
        });

        assertThat(exception.getMessage()).isEqualTo("bar not found");
    }

    @Test
    public void findByNameShouldThrowExceptionIfUnhandledClientError() {

        when(restTemplate.getForObject("http://foo.bar/{name}", PokemonSpecies.class, "foo"))
                .thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN));

        Exception exception = assertThrows(PokemonSpeciesUnknownException.class, () -> {
            pokemonRepository.findByName("foo");
        });

        assertThat(exception.getMessage()).isEqualTo("Unknown error when trying to find foo");
    }

    @Test
    public void findByNameShouldThrowExceptionIfUnhandledServerError() {

        when(restTemplate.getForObject("http://foo.bar/{name}", PokemonSpecies.class, "foo"))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Exception exception = assertThrows(PokemonSpeciesUnknownException.class, () -> {
            pokemonRepository.findByName("foo");
        });

        assertThat(exception.getMessage()).isEqualTo("Unknown error when trying to find foo");
    }
}