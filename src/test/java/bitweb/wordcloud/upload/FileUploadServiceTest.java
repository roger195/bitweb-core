package bitweb.wordcloud.upload;

import bitweb.wordcloud.textfileword.TextFileWordMapper;
import bitweb.wordcloud.textfileword.TextFileWordRepository;
import bitweb.wordcloud.textfileword.TextWordsDTO;
import bitweb.wordcloud.rabbitmq.RabbitMQProducer;
import bitweb.wordcloud.textfileinfo.TextFileInfo;
import bitweb.wordcloud.textfileinfo.TextFileInfoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class FileUploadServiceTest {

    @Mock
    private TextFileInfoRepository textFileInfoRepository;

    @Mock
    private TextFileWordRepository textFileWordRepository;

    @Mock
    private TextFileWordMapper textFileWordMapper;
    @Mock
    private RabbitMQProducer rabbitMQProducer;

    @InjectMocks
    private FileUploadService fileUploadService;

    @Test
    void testGetTextFromMultipartFile() throws IOException {
        String content = "Line 1\nLine 2\nLine 3";
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                content.getBytes()
        );

        ArrayList<String> result = fileUploadService.getTextFromMultipartFile(file);
        System.out.println(result);
        assertEquals(1, result.size());
        assertEquals("Line 1\nLine 2\nLine 3\n", result.get(0));
    }

    @Test
    void testGetTextWordsDTO() {
        UUID identifier = UUID.randomUUID();
        TextFileInfo textFileInfo = new TextFileInfo(identifier, UploadStatus.COMPLETED, 1);
        Mockito.when(textFileWordRepository.getAllByTextFileInfosIdOrderByCounterDesc(Mockito.anyLong()))
                .thenReturn(new ArrayList<>());
        Mockito.when(textFileInfoRepository.getFirstByIdentifierEquals(any(UUID.class)))
                .thenReturn(textFileInfo);

        TextWordsDTO result = fileUploadService.getTextWordsDTO(textFileInfo);

        assertNotNull(result);
        assertEquals(identifier, result.getIdentifier());
        assertEquals(UploadStatus.COMPLETED, result.getUploadStatus());
        assertEquals(0, result.getWordCounts().size());
    }

    @Test
    void testGetUploadStatus() {
        UUID identifier = UUID.randomUUID();
        Mockito.when(textFileInfoRepository.getUploadStatusByIdentifier(identifier))
                .thenReturn(UploadStatus.COMPLETED);

        UploadStatus result = fileUploadService.getUploadStatus(identifier);

        assertEquals(UploadStatus.COMPLETED, result);
    }

    @Test
    void testGetUploadedWords() {
        UUID identifier = UUID.randomUUID();
        TextFileInfo textFileInfo = new TextFileInfo(identifier, UploadStatus.COMPLETED, 1);
        Mockito.when(textFileInfoRepository.getFirstByIdentifierEquals(identifier))
                .thenReturn(textFileInfo);
        Mockito.when(textFileWordRepository.getAllByTextFileInfosIdOrderByCounterDesc(Mockito.anyLong()))
                .thenReturn(new ArrayList<>());

        TextWordsDTO result = fileUploadService.getUploadedWords(identifier);

        assertNotNull(result);
        assertEquals(identifier, result.getIdentifier());
        assertEquals(UploadStatus.COMPLETED, result.getUploadStatus());
        assertEquals(0, result.getWordCounts().size());
    }

    @Test
    void testSaveFileInfo() {
        int totalChunks = 1;
        TextFileInfo textFileInfo = new TextFileInfo(UUID.randomUUID(), UploadStatus.PROCESSING, totalChunks);
        Mockito.when(textFileInfoRepository.save(any(TextFileInfo.class)))
                .thenReturn(textFileInfo);

        TextFileInfo result = fileUploadService.saveFileInfo(totalChunks);

        assertNotNull(result);
        assertEquals(UploadStatus.PROCESSING, result.getUploadStatus());
        assertEquals(totalChunks, result.getPartsAmount());
    }

    @Test
    void testUploadFile() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello,\n World!".getBytes()
        );
        UUID identifier = UUID.randomUUID();
        TextFileInfo textFileInfo = new TextFileInfo(identifier, UploadStatus.PROCESSING, 1);
        Mockito.when(textFileInfoRepository.save(any(TextFileInfo.class)))
                .thenReturn(textFileInfo);

        fileUploadService.uploadFile(file);

        Mockito.verify(rabbitMQProducer, Mockito.times(1))
                .sendTextFilePart(Mockito.any(TextDTO.class));
    }
}

