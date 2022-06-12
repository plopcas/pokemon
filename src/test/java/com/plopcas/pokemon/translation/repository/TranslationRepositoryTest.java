package com.plopcas.pokemon.translation.repository;

import com.plopcas.pokemon.translation.dto.TranslationRequestDTO;
import com.plopcas.pokemon.translation.exception.TranslationEmptyException;
import com.plopcas.pokemon.translation.exception.TranslationUnknownException;
import com.plopcas.pokemon.translation.model.Translation;
import com.plopcas.pokemon.translation.model.TranslationContents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TranslationRepositoryTest {

    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private TranslationRepository translationRepository;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(translationRepository, "url", "http://foo.bar");
    }

    @Test
    public void getTranslationShouldReturnTranslatedText() {

        Translation translation = new Translation(new TranslationContents("barfoo", "foobar", "baz"));
        HttpEntity<TranslationRequestDTO> request = new HttpEntity<>(new TranslationRequestDTO("foobar"));

        when(restTemplate.postForObject(eq("http://foo.bar/{type}"), any(HttpEntity.class), eq(Translation.class), eq("baz")))
                .thenReturn(translation);

        String result = translationRepository.getTranslation("foobar", "baz");

        assertThat("barfoo").isEqualTo(result);
    }

    @Test
    public void getTranslationShouldThrowExceptionIfUnhandledClientError() {

        when(restTemplate.postForObject(eq("http://foo.bar/{type}"), any(HttpEntity.class), eq(Translation.class), eq("baz")))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        Exception exception = assertThrows(TranslationUnknownException.class, () -> {
            translationRepository.getTranslation("foobar", "baz");
        });

        assertThat(exception.getMessage()).isEqualTo("Unknown error trying to translate 'foobar'");
    }

    @Test
    public void getTranslationShouldThrowExceptionIfUnhandledServerError() {

        when(restTemplate.postForObject(eq("http://foo.bar/{type}"), any(HttpEntity.class), eq(Translation.class), eq("baz")))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Exception exception = assertThrows(TranslationUnknownException.class, () -> {
            translationRepository.getTranslation("foobar", "baz");
        });

        assertThat(exception.getMessage()).isEqualTo("Unknown error trying to translate 'foobar'");
    }

    @Test
    public void getTranslationShouldThrowExceptionIfTranslationIsNull() {

        HttpEntity<TranslationRequestDTO> request = new HttpEntity<>(new TranslationRequestDTO("foobar"));

        when(restTemplate.postForObject(eq("http://foo.bar/{type}"), any(HttpEntity.class), eq(Translation.class), eq("baz")))
                .thenReturn(null);

        Exception exception = assertThrows(TranslationEmptyException.class, () -> {
            translationRepository.getTranslation("foobar", "baz");
        });

        assertThat(exception.getMessage()).isEqualTo("Translation for 'foobar' is empty");
    }

    @Test
    public void getTranslationShouldThrowExceptionIfTranslationContentsIsNull() {

        Translation translation = new Translation(null);
        HttpEntity<TranslationRequestDTO> request = new HttpEntity<>(new TranslationRequestDTO("foobar"));

        when(restTemplate.postForObject(eq("http://foo.bar/{type}"), any(HttpEntity.class), eq(Translation.class), eq("baz")))
                .thenReturn(translation);

        Exception exception = assertThrows(TranslationEmptyException.class, () -> {
            translationRepository.getTranslation("foobar", "baz");
        });

        assertThat(exception.getMessage()).isEqualTo("Translation for 'foobar' is empty");
    }

    @Test
    public void getTranslationShouldThrowExceptionIfTranslationTextIsNull() {

        Translation translation = new Translation(new TranslationContents(null, "foobar", "baz"));
        HttpEntity<TranslationRequestDTO> request = new HttpEntity<>(new TranslationRequestDTO("foobar"));

        when(restTemplate.postForObject(eq("http://foo.bar/{type}"), any(HttpEntity.class), eq(Translation.class), eq("baz")))
                .thenReturn(translation);

        Exception exception = assertThrows(TranslationEmptyException.class, () -> {
            translationRepository.getTranslation("foobar", "baz");
        });

        assertThat(exception.getMessage()).isEqualTo("Translation for 'foobar' is empty");
    }

    @Test
    public void getTranslationShouldThrowExceptionIfTranslationTextIsEmpty() {

        Translation translation = new Translation(new TranslationContents("", "foobar", "baz"));
        HttpEntity<TranslationRequestDTO> request = new HttpEntity<>(new TranslationRequestDTO("foobar"));

        when(restTemplate.postForObject(eq("http://foo.bar/{type}"), any(HttpEntity.class), eq(Translation.class), eq("baz")))
                .thenReturn(translation);

        Exception exception = assertThrows(TranslationEmptyException.class, () -> {
            translationRepository.getTranslation("foobar", "baz");
        });

        assertThat(exception.getMessage()).isEqualTo("Translation for 'foobar' is empty");
    }
}