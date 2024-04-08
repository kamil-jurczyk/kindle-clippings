package pl.kamiljurczyk.clippings.clippingTranslation;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.kamiljurczyk.clippings.clipping.Clipping;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.ClippingTranslation;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.TranslationRequestBody;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.TranslationResponseBody;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClippingTranslationService {

    private final RestClient restClient;

    public ClippingTranslationService() {
        restClient = RestClient.builder()
                .baseUrl("https://libretranslate.com")
                .build();
    }

    public List<ClippingTranslation> translateClippings(List<Clipping> clippings, String sourceLanguage, String targetLanguage) {

        ArrayList<ClippingTranslation> clippingTranslations = new ArrayList<>();

        clippings.forEach(clipping -> {

            String highlight = clipping.getHighlight();

            TranslationResponseBody translation = restClient.post()
                    .uri("/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(TranslationRequestBody.builder()
                            .q(highlight)
                            .source(sourceLanguage)
                            .target(targetLanguage)
                            .format("text")
                            .apiKey("")
                            .build())
                    .retrieve()
                    .body(TranslationResponseBody.class);

            if(translation != null) {
                clippingTranslations.add(ClippingTranslation.builder()
                        .highlight(highlight)
                        .translation(translation.getTranslatedText())
                        .sourceLanguage(sourceLanguage)
                        .targetLanguage(targetLanguage)
                        .build());
            }
        });

        return clippingTranslations;
    }
}
