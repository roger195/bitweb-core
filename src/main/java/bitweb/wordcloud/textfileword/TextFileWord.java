package bitweb.wordcloud.textfileword;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "text_files_words")
public class TextFileWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "counter", nullable = false)
    private int counter;

    @JoinColumn(name = "text_file_infos_id", nullable = false)
    private long textFileInfosId;

    @JoinColumn(name = "words_id", nullable = false)
    private long wordsId;

    public TextFileWord(int counter, long textFileInfosId, long wordsId) {
        this.counter = counter;
        this.textFileInfosId = textFileInfosId;
        this.wordsId = wordsId;
    }
}
