package com.plopcas.pokemon.translation.service;

import com.plopcas.pokemon.pokemon.dto.PokemonDTO;
import com.plopcas.pokemon.translation.exception.TranslationServiceException;
import com.plopcas.pokemon.translation.repository.TranslationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);

    public static final String CAVE = "cave";
    public static final String YODA = "yoda";
    public static final String SHAKESPEARE = "shakespeare";

    private TranslationRepository translationRepository;

    public TranslationService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
    }

    public PokemonDTO translate(PokemonDTO pokemonDTO) {

        if (pokemonDTO == null) {
            throw new TranslationServiceException("Argument cannot be null");
        }

        log.debug("Habitat is {} and legendary is {}", pokemonDTO.getHabitat(), pokemonDTO.getIsLegendary());

        try {
            if (CAVE.equals(pokemonDTO.getHabitat()) || pokemonDTO.getIsLegendary()) {
                pokemonDTO.setDescription(translationRepository.getTranslation(pokemonDTO.getDescription(), YODA));
            } else {
                pokemonDTO.setDescription(translationRepository.getTranslation(pokemonDTO.getDescription(), SHAKESPEARE));
            }
        } catch (Exception e) {
            log.error("Couldn't translate, using default description '" + pokemonDTO.getDescription() + "'");
        }
        return pokemonDTO;
    }
}
