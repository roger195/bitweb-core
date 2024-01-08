package bitweb.wordcloud.textfileinfo;

import bitweb.wordcloud.upload.UploadStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TextFileInfoRepositoryTest {

    @Autowired
    private TextFileInfoRepository textFileInfoRepository;

    @Test
    public void testGetFirstByIdentifierEquals() {
        UUID identifier = UUID.randomUUID();
        UploadStatus uploadStatus = UploadStatus.PROCESSING;
        int partsAmount = 5;

        TextFileInfo savedTextFileInfo = textFileInfoRepository.save(new TextFileInfo(identifier, uploadStatus, partsAmount));

        TextFileInfo retrievedTextFileInfo = textFileInfoRepository.getFirstByIdentifierEquals(identifier);

        assertThat(retrievedTextFileInfo).isNotNull();
        assertThat(retrievedTextFileInfo.getId()).isEqualTo(savedTextFileInfo.getId());
        assertThat(retrievedTextFileInfo.getIdentifier()).isEqualTo(identifier);
        assertThat(retrievedTextFileInfo.getUploadStatus()).isEqualTo(uploadStatus);
        assertThat(retrievedTextFileInfo.getPartsAmount()).isEqualTo(partsAmount);
    }
}

