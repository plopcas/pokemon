package com.plopcas.pokemon.translation.service;

import com.plopcas.pokemon.pokemon.dto.PokemonDTO;
import com.plopcas.pokemon.translation.exception.TranslationServiceException;
import com.plopcas.pokemon.translation.exception.TranslationUnknownException;
import com.plopcas.pokemon.translation.repository.TranslationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationServiceTest {

    @Mock
    private TranslationRepository translationRepository;
    @InjectMocks
    private TranslationService translationService;

    @Test
    public void translateShouldReturnYodaTranslationWhenHabitatIsCave() {

        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "cave", false);

        when(translationRepository.getTranslation("foobar", "yoda")).thenReturn("barfoo");

        PokemonDTO result = translationService.translate(fooDto);

        assertThat("barfoo").isEqualTo(result.getDescription());
        assertThat("foo").isEqualTo(result.getName());
        assertThat("cave").isEqualTo(result.getHabitat());
        assertThat(false).isEqualTo(result.getIsLegendary());

        verify(translationRepository, times(1)).getTranslation("foobar", "yoda");
    }

    @Test
    public void translateShouldReturnYodaTranslationWhenIsLegendary() {
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "bar", true);

        when(translationRepository.getTranslation("foobar", "yoda")).thenReturn("barfoo");

        PokemonDTO result = translationService.translate(fooDto);

        assertThat("barfoo").isEqualTo(result.getDescription());
        assertThat("foo").isEqualTo(result.getName());
        assertThat("bar").isEqualTo(result.getHabitat());
        assertThat(true).isEqualTo(result.getIsLegendary());

        verify(translationRepository, times(1)).getTranslation("foobar", "yoda");
    }

    @Test
    public void translateShouldReturnShakespeareTranslationWhenHabitatIsNotCaveAndNotLegendary() {
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "bar", false);

        when(translationRepository.getTranslation("foobar", "shakespeare")).thenReturn("barfoo");

        PokemonDTO result = translationService.translate(fooDto);

        assertThat("barfoo").isEqualTo(result.getDescription());
        assertThat("foo").isEqualTo(result.getName());
        assertThat("bar").isEqualTo(result.getHabitat());
        assertThat(false).isEqualTo(result.getIsLegendary());

        verify(translationRepository, times(1)).getTranslation("foobar", "shakespeare");
    }

    @Test
    public void translateShouldReturnDefaultDescriptionWhenError() {
        PokemonDTO fooDto = new PokemonDTO("foo", "foobar", "bar", false);

        when(translationRepository.getTranslation("foobar", "shakespeare")).thenThrow(new TranslationUnknownException("foobar"));

        PokemonDTO result = translationService.translate(fooDto);

        assertThat("foobar").isEqualTo(result.getDescription());
        assertThat("foo").isEqualTo(result.getName());
        assertThat("bar").isEqualTo(result.getHabitat());
        assertThat(false).isEqualTo(result.getIsLegendary());

        verify(translationRepository, times(1)).getTranslation("foobar", "shakespeare");
    }

    @Test
    public void translateShouldThrowExceptionWhenNullInput() {

        Exception exception = assertThrows(TranslationServiceException.class, () -> {
            translationService.translate(null);
        });

        assertThat(exception.getMessage()).isEqualTo("Argument cannot be null");
    }
}