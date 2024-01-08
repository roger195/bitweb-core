package bitweb.wordcloud.word;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class WordRepositoryTest {

    @Autowired
    private WordRepository wordRepository;

    @Test
    void testGetFirstById() {
        Word savedWord = wordRepository.save(new Word(1L, "TestWord"));

        Word retrievedWord = wordRepository.getFirstById(savedWord.getId());

        assertThat(retrievedWord).isNotNull();
        assertThat(retrievedWord.getId()).isEqualTo(savedWord.getId());
        assertThat(retrievedWord.getWord()).isEqualTo(savedWord.getWord());
    }
}

