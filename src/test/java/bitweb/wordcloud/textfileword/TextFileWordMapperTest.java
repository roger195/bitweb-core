package bitweb.wordcloud.textfileword;

import bitweb.wordcloud.word.Word;
import bitweb.wordcloud.word.WordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class TextFileWordMapperTest {

    @Autowired
    private TextFileWordMapper textFileWordMapper;

    @MockBean
    private WordRepository wordRepository;

    @Test
    public void testMapToDTO() {
        TextFileWord textFileWord1 = new TextFileWord(5, 1L, 1L);
        TextFileWord textFileWord2 = new TextFileWord(10, 2L, 2L);

        when(wordRepository.getFirstById(1L)).thenReturn(new Word(1L, "hello"));
        when(wordRepository.getFirstById(2L)).thenReturn(new Word(2L, "world"));

        List<TextFileWord> textFileWordList = Arrays.asList(textFileWord1, textFileWord2);

        List<TextFileWordDTO> result = textFileWordMapper.mapToDTO(textFileWordList);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getWord()).isEqualTo("hello");
        assertThat(result.get(0).getCount()).isEqualTo(5);
        assertThat(result.get(1).getWord()).isEqualTo("world");
        assertThat(result.get(1).getCount()).isEqualTo(10);
    }
}
