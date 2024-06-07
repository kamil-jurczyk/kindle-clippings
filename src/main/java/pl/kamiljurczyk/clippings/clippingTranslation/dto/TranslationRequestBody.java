package pl.kamiljurczyk.clippings.clippingTranslation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class TranslationRequestBody {
    private String q;
    private String source;
    private String target;
    private String format;
    private int alternatives;
    @JsonProperty("api_key")
    private String apiKey;
}
