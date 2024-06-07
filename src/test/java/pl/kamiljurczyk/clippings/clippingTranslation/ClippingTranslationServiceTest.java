package pl.kamiljurczyk.clippings.clippingTranslation;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.maciejwalkowiak.wiremock.spring.ConfigureWireMock;
import com.maciejwalkowiak.wiremock.spring.EnableWireMock;
import com.maciejwalkowiak.wiremock.spring.InjectWireMock;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.ResourceAccessException;
import pl.kamiljurczyk.clippings.clipping.Clipping;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.ClippingTranslation;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@EnableWireMock({
        @ConfigureWireMock(name = "translation-service", property = "translate.url")
})
class ClippingTranslationServiceTest {

    private static final String HIGHLIGHT = "hello";
    private static final String TRANSLATION = "halo";
    private static final String SOURCE_LANGUAGE = "en";
    private static final String TARGET_LANGUAGE = "pl";

    private static final String CORRECT_RESPONSE_JSON_PATH = "/__files/correct-response.json";
    private static final String BAD_REQUEST_JSON_PATH = "/__files/bad-request.json";
    private static final List<Clipping> TEST_CLIPPING_LIST = List.of(Clipping.builder()
            .highlight(HIGHLIGHT)
            .build());

    @InjectWireMock("translation-service")
    private WireMockServer wiremock;

    @Autowired
    private ClippingTranslationService clippingTranslationService;

    @Test
    void shouldReturnCorrectTranslation() throws IOException {
        // given
        String responseBody = IOUtils.resourceToString(CORRECT_RESPONSE_JSON_PATH, StandardCharsets.UTF_8);

        wiremock.stubFor(post(urlEqualTo("/"))
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                ));

        // when
        List<ClippingTranslation> clippingTranslations = clippingTranslationService.translateClippings(TEST_CLIPPING_LIST, SOURCE_LANGUAGE, TARGET_LANGUAGE);

        ClippingTranslation clippingTranslation = clippingTranslations.getFirst();
        // then
        assertThat(clippingTranslation.getHighlight()).isEqualTo(HIGHLIGHT);
        assertThat(clippingTranslation.getTranslation()).isEqualTo(TRANSLATION);
        assertThat(clippingTranslation.getSourceLanguage()).isEqualTo(SOURCE_LANGUAGE);
        assertThat(clippingTranslation.getTargetLanguage()).isEqualTo(TARGET_LANGUAGE);
    }

    @Test
    void shouldThrowExceptionWhenStatus400() throws IOException {
        // given
        String responseBody = IOUtils.resourceToString(BAD_REQUEST_JSON_PATH, StandardCharsets.UTF_8);

        wiremock.stubFor(post(urlEqualTo("/"))
                .willReturn(
                        aResponse()
                                .withStatus(400)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                ));
        // then
        assertThatThrownBy(() -> clippingTranslationService.translateClippings(TEST_CLIPPING_LIST, SOURCE_LANGUAGE, TARGET_LANGUAGE))
                .isInstanceOf(ResourceAccessException.class)
                .hasMessageContaining("Translation body is invalid");
    }

    @Test
    void shouldThrowExceptionWhenStatus500() {
        // given
        wiremock.stubFor(post(urlEqualTo("/"))
                .willReturn(
                        aResponse()
                                .withStatus(500)
                                .withHeader("Content-Type", "application/json")
                ));
        // then
        assertThatThrownBy(() -> clippingTranslationService.translateClippings(TEST_CLIPPING_LIST, SOURCE_LANGUAGE, TARGET_LANGUAGE))
                .isInstanceOf(ResourceAccessException.class)
                .hasMessageContaining("Server is unreachable");
    }
}