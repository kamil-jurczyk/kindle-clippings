package pl.kamiljurczyk.clippings.clippingTranslation.dto;

import lombok.Builder;

@Builder
public class ClippingTranslation {
    private String highlight;
    private String translation;
    private String sourceLanguage;
    private String targetLanguage;
}
