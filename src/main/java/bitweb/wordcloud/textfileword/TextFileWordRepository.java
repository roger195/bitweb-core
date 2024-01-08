package bitweb.wordcloud.textfileword;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextFileWordRepository extends JpaRepository<TextFileWord, Integer> {
    List<TextFileWord> getAllByTextFileInfosIdOrderByCounterDesc(long textFileInfosId);
}