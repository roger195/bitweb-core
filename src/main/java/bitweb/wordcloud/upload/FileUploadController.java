package bitweb.wordcloud.upload;

import bitweb.wordcloud.textfileword.TextWordsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/upload")
public class FileUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
            }
            UUID identifier = fileUploadService.uploadFile(file);
            LOGGER.info(String.format("File(%s) upload initiated!", identifier));
            return new ResponseEntity<>(identifier.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public TextWordsDTO getUploadedWords(@RequestParam("identifier") UUID uuid){
        return fileUploadService.getUploadedWords(uuid);
    }

    @GetMapping("/status")
    public UploadStatus getFileUploadStatus(@RequestParam("identifier") UUID uuid){
        return fileUploadService.getUploadStatus(uuid);
    }
}

