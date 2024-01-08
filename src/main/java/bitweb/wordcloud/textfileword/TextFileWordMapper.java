package bitweb.wordcloud.textfileword;

import bitweb.wordcloud.word.Word;
import bitweb.wordcloud.word.WordRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextFileWordMapper {

    private final WordRepository wordRepository;

    public TextFileWordMapper(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public List<TextFileWordDTO> mapToDTO(List<TextFileWord> textFileWordList) {
        List<TextFileWordDTO> textFileWordDTOs = new ArrayList<>();
        for (TextFileWord textFileWord : textFileWordList) {
            Word word = wordRepository.getFirstById(textFileWord.getWordsId());
            textFileWordDTOs.add(new TextFileWordDTO(word.getWord(), textFileWord.getCounter()));
        }
        return textFileWordDTOs;
    }
}
