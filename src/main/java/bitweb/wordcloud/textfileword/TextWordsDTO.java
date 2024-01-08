package bitweb.wordcloud.textfileword;

import bitweb.wordcloud.upload.UploadStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TextWordsDTO {
    private UUID identifier;
    private UploadStatus uploadStatus;
    List<TextFileWordDTO> wordCounts;
}
