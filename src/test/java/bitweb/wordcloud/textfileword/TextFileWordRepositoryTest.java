package bitweb.wordcloud.textfileword;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TextFileWordRepositoryTest {

    @Autowired
    private TextFileWordRepository textFileWordRepository;

    @Test
    public void testGetAllByTextFileInfosIdOrderByCounterDesc() {
        long textFileInfosId = 1L;

        textFileWordRepository.save(new TextFileWord(5, textFileInfosId, 1L));
        textFileWordRepository.save(new TextFileWord(10, textFileInfosId, 2L));
        textFileWordRepository.save(new TextFileWord(8, textFileInfosId, 3L));

        List<TextFileWord> result = textFileWordRepository.getAllByTextFileInfosIdOrderByCounterDesc(textFileInfosId);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getCounter()).isEqualTo(10);
        assertThat(result.get(1).getCounter()).isEqualTo(8);
        assertThat(result.get(2).getCounter()).isEqualTo(5);
    }
}
