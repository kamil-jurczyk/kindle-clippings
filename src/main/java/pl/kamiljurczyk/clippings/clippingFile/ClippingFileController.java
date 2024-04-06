package pl.kamiljurczyk.clippings.clippingFile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/clipping")
@RestController
@RequiredArgsConstructor
public class ClippingFileController {

    private final ClippingFileService clippingFileService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Object> uploadFileWithClippings(@RequestParam("file") MultipartFile clipping) {
        clippingFileService.uploadClipping(clipping);

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClippingFile> getClippingsByFileId(@PathVariable("id") long id) {
        return ResponseEntity.ok(clippingFileService.getClippingsByFileId(id));
    }
}
