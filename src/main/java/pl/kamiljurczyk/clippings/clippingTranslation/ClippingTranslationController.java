package pl.kamiljurczyk.clippings.clippingTranslation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.kamiljurczyk.clippings.clipping.Clipping;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ClippingTranslationController {

    private final ClippingTranslationService clippingService;

    @PostMapping("/translate/{sourceLanguage}/{targetLanguage}")
    public ResponseEntity<Object> translateClippings(@RequestBody List<Clipping> clippings,
                                                     @PathVariable("sourceLanguage") String sourceLanguage,
                                                     @PathVariable("targetLanguage") String targetLanguage) {
        return ResponseEntity.ok(clippingService.translateClippings(clippings, sourceLanguage, targetLanguage));
    }
}
