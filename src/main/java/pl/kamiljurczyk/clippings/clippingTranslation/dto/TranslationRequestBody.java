package pl.kamiljurczyk.clippings.clippingTranslation.dto;

import lombok.Builder;

@Builder
public class TranslationRequestBody {
    private String q;
    private String source;
    private String target;
    private String format;
    private String apiKey;
}
