package pl.kamiljurczyk.clippings.clippingTranslation.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ClippingTranslation {
    private String highlight;
    private String translation;
    private String sourceLanguage;
    private String targetLanguage;
}
