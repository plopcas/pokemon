package com.plopcas.pokemon.translation.repository;

import com.plopcas.pokemon.translation.dto.TranslationRequestDTO;
import com.plopcas.pokemon.translation.exception.TranslationEmptyException;
import com.plopcas.pokemon.translation.exception.TranslationUnknownException;
import com.plopcas.pokemon.translation.model.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Repository
public class TranslationRepository {

    private static final Logger log = LoggerFactory.getLogger(TranslationRepository.class);

    private final RestTemplate restTemplate;

    @Value("${funTranslationsApi.url}")
    private String url;

    public TranslationRepository(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getTranslation(String description, String translationType) {
        try {
            HttpEntity<TranslationRequestDTO> request = new HttpEntity<>(new TranslationRequestDTO(description));
            Translation translation = restTemplate.postForObject(url + "/{type}", request, Translation.class, translationType);
            if (translation != null
                    && translation.getContents() != null
                    && translation.getContents().getTranslated() != null
                    && !translation.getContents().getTranslated().isBlank()) {
                return translation.getContents().getTranslated();
            } else {
                throw new TranslationEmptyException(description);
            }
        } catch (HttpStatusCodeException httpException) {
            log.error("Unexpected error trying to translate '%s' : %s - %s", description, httpException.getStatusCode(), httpException.getMessage());
            throw new TranslationUnknownException(description);
        }
    }
}
