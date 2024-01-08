package bitweb.wordcloud.word;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WordTest {

    @Test
    void testWordEntity() {
        Word word = new Word();
        word.setId(1L);
        word.setWord("TestWord");

        assertThat(word.getId()).isEqualTo(1L);
        assertThat(word.getWord()).isEqualTo("TestWord");

        Word emptyWord = new Word();
        assertThat(emptyWord).isNotNull();

        Word anotherWord = new Word(2L, "AnotherWord");
        assertThat(anotherWord.getId()).isEqualTo(2L);
        assertThat(anotherWord.getWord()).isEqualTo("AnotherWord");
    }
}

