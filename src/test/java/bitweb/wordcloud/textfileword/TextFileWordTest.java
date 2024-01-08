package bitweb.wordcloud.textfileword;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TextFileWordTest {

    @Test
    public void testConstructorAndGetters() {
        int count = 5;
        long textFileInfosId = 1L;
        long wordsId = 2L;

        TextFileWord textFileWord = new TextFileWord(count, textFileInfosId, wordsId);

        assertThat(textFileWord.getCounter()).isEqualTo(count);
        assertThat(textFileWord.getTextFileInfosId()).isEqualTo(textFileInfosId);
        assertThat(textFileWord.getWordsId()).isEqualTo(wordsId);
    }

    @Test
    public void testSetters() {
        TextFileWord textFileWord = new TextFileWord(5, 1L, 2L);

        textFileWord.setCounter(10);
        textFileWord.setTextFileInfosId(3L);
        textFileWord.setWordsId(4L);

        assertThat(textFileWord.getCounter()).isEqualTo(10);
        assertThat(textFileWord.getTextFileInfosId()).isEqualTo(3L);
        assertThat(textFileWord.getWordsId()).isEqualTo(4L);
    }
}
