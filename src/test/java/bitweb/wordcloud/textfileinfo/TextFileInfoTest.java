package bitweb.wordcloud.textfileinfo;

import bitweb.wordcloud.upload.UploadStatus;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.UUID;


public class TextFileInfoTest {

    @Test
    public void testAllArgsConstructor() {
        long id = 1L;
        UUID identifier = UUID.randomUUID();
        UploadStatus uploadStatus = UploadStatus.PROCESSING;
        int partsAmount = 5;

        TextFileInfo textFileInfo = new TextFileInfo(id, identifier, uploadStatus, partsAmount);

        assertThat(textFileInfo.getId()).isEqualTo(id);
        assertThat(textFileInfo.getIdentifier()).isEqualTo(identifier);
        assertThat(textFileInfo.getUploadStatus()).isEqualTo(uploadStatus);
        assertThat(textFileInfo.getPartsAmount()).isEqualTo(partsAmount);
    }

    @Test
    public void testNoArgsConstructor() {
        TextFileInfo textFileInfo = new TextFileInfo();

        assertThat(textFileInfo).isNotNull();
    }

    @Test
    public void testConstructorWithParameters() {
        UUID identifier = UUID.randomUUID();
        UploadStatus uploadStatus = UploadStatus.PROCESSING;
        int partsAmount = 5;

        TextFileInfo textFileInfo = new TextFileInfo(identifier, uploadStatus, partsAmount);

        assertThat(textFileInfo.getId()).isEqualTo(0L);
        assertThat(textFileInfo.getIdentifier()).isEqualTo(identifier);
        assertThat(textFileInfo.getUploadStatus()).isEqualTo(uploadStatus);
        assertThat(textFileInfo.getPartsAmount()).isEqualTo(partsAmount);
    }
}
