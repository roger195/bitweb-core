package bitweb.wordcloud.textfileinfo;

import bitweb.wordcloud.upload.UploadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TextFileInfoRepository extends JpaRepository<TextFileInfo, Integer> {
    TextFileInfo getFirstByIdentifierEquals(UUID identifier);

    @Query(value = "select status from text_file_infos where identifier = ?1", nativeQuery = true)
    UploadStatus getUploadStatusByIdentifier(UUID identifier);
}