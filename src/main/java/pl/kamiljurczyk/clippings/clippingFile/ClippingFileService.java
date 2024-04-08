package pl.kamiljurczyk.clippings.clippingFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.kamiljurczyk.clippings.user.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
class ClippingFileService {

    private final ClippingFileRepository clippingFileRepository;
    private final UserRepository userRepository;

    public void uploadClipping(MultipartFile clipping, String userName) {
        List<ArrayList<String>> listOfClippings = ClippingExtractData.extractClippingsFromTxtFile(clipping);

        ClippingFile clippingFile = new ClippingFile();
        clippingFile.setFileName(clipping.getOriginalFilename());
        clippingFile.setUserId(userRepository.findByEmail(userName).orElseThrow(
                () -> new UsernameNotFoundException("User not found")).getId());
        ClippingExtractData.toClippingEntitiesList(listOfClippings).forEach(clippingFile::addClipping);

        clippingFileRepository.save(clippingFile);
    }

    public ClippingFile getClippingsByFileId(long id) {
        return clippingFileRepository.getClippingFileById(id).orElseThrow(ClippingFileNotFoundException::new);
    }

    public boolean isAllowedToGetClippings(String userName, Long id) {
        Long userId = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException("User not found")).getId();

        return userId.equals(clippingFileRepository.getClippingFileById(id).orElseThrow(ClippingFileNotFoundException::new).getUserId());
    }
}
