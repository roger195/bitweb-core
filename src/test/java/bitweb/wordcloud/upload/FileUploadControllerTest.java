package bitweb.wordcloud.upload;

import bitweb.wordcloud.textfileword.TextWordsDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@WebMvcTest(FileUploadController.class)
@ActiveProfiles("test")
class FileUploadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileUploadService fileUploadService;

    @Test
    void testUploadFileSuccess() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        UUID identifier = UUID.randomUUID();
        Mockito.when(fileUploadService.uploadFile(Mockito.any())).thenReturn(identifier);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(identifier.toString()));

        Mockito.verify(fileUploadService, Mockito.times(1)).uploadFile(Mockito.any());
    }

    @Test
    void testUploadFileEmpty() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "".getBytes());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("File is empty"));

        Mockito.verify(fileUploadService, Mockito.never()).uploadFile(Mockito.any());
    }

    @Test
    void testUploadFileException() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        Mockito.when(fileUploadService.uploadFile(Mockito.any())).thenThrow(new RuntimeException("Test exception"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/upload")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Failed to upload file: Test exception"));

        Mockito.verify(fileUploadService, Mockito.times(1)).uploadFile(Mockito.any());
    }

    @Test
    void testGetUploadedWords() throws Exception {
        UUID uuid = UUID.randomUUID();
        TextWordsDTO mockDTO = new TextWordsDTO(uuid, UploadStatus.COMPLETED, null);

        Mockito.when(fileUploadService.getUploadedWords(uuid)).thenReturn(mockDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/upload")
                        .param("identifier", uuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.identifier").value(uuid.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.uploadStatus").value(UploadStatus.COMPLETED.name()));

        Mockito.verify(fileUploadService, Mockito.times(1)).getUploadedWords(uuid);
    }

    @Test
    void testGetFileUploadStatus() throws Exception {
        UUID uuid = UUID.randomUUID();
        UploadStatus mockStatus = UploadStatus.COMPLETED;

        Mockito.when(fileUploadService.getUploadStatus(uuid)).thenReturn(mockStatus);

        mockMvc.perform(MockMvcRequestBuilders.get("/upload/status")
                        .param("identifier", uuid.toString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("\"" + mockStatus.name() + "\""));

        Mockito.verify(fileUploadService, Mockito.times(1)).getUploadStatus(uuid);
    }
}

