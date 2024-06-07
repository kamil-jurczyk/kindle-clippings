package pl.kamiljurczyk.clippings.clippingTranslation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import pl.kamiljurczyk.clippings.clipping.Clipping;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.ClippingTranslation;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.TranslationRequestBody;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.TranslationResponseBody;

import java.io.*;
import java.net.http.HttpClient;
import java.rmi.ServerException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClippingTranslationService {

    private static final String FILENAME = "anki";
    private static final String FILE_FORMAT = ".txt";

    @Value("${translate.url}")
    private String translateUrl;

    private final RestClient restClient;

    public ClippingTranslationService() {
        var client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
        var requestFactory = new JdkClientHttpRequestFactory(client);
        restClient = RestClient.builder()
                .requestFactory(requestFactory)
                .baseUrl(translateUrl)
                .build();
    }

    List<ClippingTranslation> translateClippings(List<Clipping> clippings, String sourceLanguage, String targetLanguage) {

        ArrayList<ClippingTranslation> clippingTranslations = new ArrayList<>();

        clippings.forEach(clipping -> {

            String highlight = clipping.getHighlight();

            TranslationResponseBody translation = restClient.post()
                    .uri(translateUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(TranslationRequestBody.builder()
                            .q(highlight)
                            .source(sourceLanguage)
                            .target(targetLanguage)
                            .format("text")
                            .alternatives(3)
                            .apiKey("")
                            .build())
                    .exchange(((request, response) -> {
                        if (response.getStatusCode().is4xxClientError()) {
                            throw new InvalidObjectException("Translation body is invalid");
                        } else if (response.getStatusCode().is5xxServerError()) {
                            throw new ServerException("Server is unreachable");
                        } else {
                            ObjectMapper objectMapper = new ObjectMapper();
                            return objectMapper.readValue(response.getBody(), TranslationResponseBody.class);
                        }
                    }));

            clippingTranslations.add(ClippingTranslation.builder()
                    .highlight(highlight)
                    .translation(translation.translatedText())
                    .sourceLanguage(sourceLanguage)
                    .targetLanguage(targetLanguage)
                    .build());
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
