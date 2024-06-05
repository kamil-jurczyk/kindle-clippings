package pl.kamiljurczyk.clippings.clippingTranslation;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.kamiljurczyk.clippings.clipping.Clipping;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.ClippingTranslation;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.TranslationRequestBody;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.TranslationResponseBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClippingTranslationService {

    private static final String FILENAME = "anki";
    private static final String FILE_FORMAT = ".txt";

    private final RestClient restClient;

    public ClippingTranslationService() {
        restClient = RestClient.builder()
                .baseUrl("https://libretranslate.com")
                .build();
    }

    List<ClippingTranslation> translateClippings(List<Clipping> clippings, String sourceLanguage, String targetLanguage) {

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

    InputStreamResource exportTxtFile(List<ClippingTranslation> clippingTranslations) throws IOException {
        StringBuilder stringBuilder = appendFileWithClippingTranslations(clippingTranslations);

        File file = File.createTempFile(FILENAME, FILE_FORMAT);
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());

        try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(stringBuilder.toString());
        }
        FileInputStream fileInputStream = new FileInputStream(file);

        return new InputStreamResource(fileInputStream);
    }

    private StringBuilder appendFileWithClippingTranslations(List<ClippingTranslation> clippingTranslations) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("""
                #separator:space
                #html:false\s
                """);
        clippingTranslations.forEach(clippingTranslation ->
                stringBuilder.append(clippingTranslation.getHighlight())
                        .append(" ")
                        .append(clippingTranslation.getTranslation())
                        .append("\n")
        );
        return stringBuilder;
    }
}
