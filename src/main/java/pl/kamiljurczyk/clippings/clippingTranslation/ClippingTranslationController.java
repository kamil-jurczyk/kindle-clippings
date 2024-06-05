package pl.kamiljurczyk.clippings.clippingTranslation;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kamiljurczyk.clippings.clipping.Clipping;
import pl.kamiljurczyk.clippings.clippingTranslation.dto.ClippingTranslation;

import java.io.*;
import java.util.List;

@RestController
@RequestMapping("/translate")
@RequiredArgsConstructor
class ClippingTranslationController {
    private final ClippingTranslationService clippingService;

    @PostMapping("/{sourceLanguage}/{targetLanguage}")
    ResponseEntity<Object> translateClippings(@RequestBody List<Clipping> clippings,
                                                     @PathVariable("sourceLanguage") String sourceLanguage,
                                                     @PathVariable("targetLanguage") String targetLanguage) {
        return ResponseEntity.ok(clippingService.translateClippings(clippings, sourceLanguage, targetLanguage));
    }

    @PostMapping("/export")
    ResponseEntity<InputStreamResource> exportTxtFileToAnki(@RequestBody List<ClippingTranslation> clippingTranslations) throws IOException {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=anki.txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(clippingService.exportTxtFile(clippingTranslations));
    }
}
