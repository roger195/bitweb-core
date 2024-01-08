package bitweb.wordcloud.upload;

import lombok.Data;

@Data
public class TextDTO {
    private long id;
    private int partsTotal;
    private int partNumber;
    private String text;

    public TextDTO(long id, int partsTotal, int partNumber, String text) {
        this.id = id;
        this.partsTotal = partsTotal;
        this.partNumber = partNumber;
        this.text = text;
    }
}
