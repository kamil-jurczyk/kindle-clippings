package pl.kamiljurczyk.clippings.clippingFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class ClippingFileService {

    private final ClippingFileRepository clippingFileRepository;

    public void uploadClipping(MultipartFile clipping) {
        List<ArrayList<String>> listOfClippings = ClippingExtractData.extractClippingsFromTxtFile(clipping);

        ClippingFile clippingFile = ClippingFile.builder()
                .fileName(clipping.getOriginalFilename())
                .clippingList(ClippingExtractData.toClippingEntitiesList(listOfClippings))
                .build();

        clippingFileRepository.save(clippingFile);
    }

    public ClippingFile getClippingsByFileId(long id) {
        return clippingFileRepository.getClippingFileById(id).orElseThrow(ClippingFileNotFoundException::new);
    }
}
