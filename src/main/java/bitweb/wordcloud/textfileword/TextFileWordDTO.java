package bitweb.wordcloud.textfileword;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextFileWordDTO {
    private String word;
    private int count;
}
