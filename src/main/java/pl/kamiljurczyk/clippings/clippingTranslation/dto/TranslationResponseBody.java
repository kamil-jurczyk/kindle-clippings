package pl.kamiljurczyk.clippings.clippingTranslation.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TranslationResponseBody(String translatedText) {}
