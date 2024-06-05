package pl.kamiljurczyk.clippings.clippingFile;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.kamiljurczyk.clippings.security.jwt.JwtService;

@RequestMapping("/clipping")
@RestController
@RequiredArgsConstructor
class ClippingFileController {

    private final ClippingFileService clippingFileService;
    private final JwtService jwtService;

    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<Object> uploadFileWithClippings(@RequestParam("file") MultipartFile clipping, @RequestHeader("Authorization") String jwt) {
        clippingFileService.uploadClipping(clipping, jwtService.extractUsernameFromBearerToken(jwt));

        return ResponseEntity.accepted().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@clippingFileService.isAllowedToGetClippings(principal, #id)")
    ResponseEntity<ClippingFile> getClippingsByFileId(@PathVariable("id") long id) {
        return ResponseEntity.ok(clippingFileService.getClippingsByFileId(id));
    }
}
