package bitweb.wordcloud.upload;

import bitweb.wordcloud.rabbitmq.RabbitMQProducer;
import bitweb.wordcloud.textfileword.TextFileWord;
import bitweb.wordcloud.textfileword.TextFileWordDTO;
import bitweb.wordcloud.textfileword.TextFileWordMapper;
import bitweb.wordcloud.textfileword.TextFileWordRepository;
import bitweb.wordcloud.textfileinfo.TextFileInfo;
import bitweb.wordcloud.textfileword.TextWordsDTO;
import bitweb.wordcloud.textfileinfo.TextFileInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {

    private final TextFileInfoRepository textFileInfoRepository;
    private final TextFileWordRepository textFileWordRepository;
    private final TextFileWordMapper textFileWordMapper;
    private final RabbitMQProducer rabbitMQProducer;


    public FileUploadService(TextFileInfoRepository textFileInfoRepository,
                             TextFileWordRepository textFileWordRepository,
                             TextFileWordMapper textFileWordMapper,
                             RabbitMQProducer rabbitMQProducer) {
        this.textFileInfoRepository = textFileInfoRepository;
        this.textFileWordRepository = textFileWordRepository;
        this.textFileWordMapper = textFileWordMapper;
        this.rabbitMQProducer = rabbitMQProducer;
    }

    public ArrayList<String> getTextFromMultipartFile(MultipartFile multipartFile) throws IOException {
        ArrayList<String> textChunks = new ArrayList<>();
        StringBuilder textContent = new StringBuilder();
        int maxsize = 100;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()))) {
            String line;
            int currentSize = 0;
            while ((line = reader.readLine()) != null) {
                textContent.append(line).append("\n");
                currentSize++;
                if (currentSize == maxsize){
                    textChunks.add(textContent.toString());
                    textContent = new StringBuilder();
                    currentSize = 0;
                }
            }
            if (!textContent.isEmpty()) textChunks.add(textContent.toString());
        }
        return textChunks;
    }

    public TextWordsDTO getTextWordsDTO(TextFileInfo textFileInfo) {
        List<TextFileWord> textFileWords = textFileWordRepository
                .getAllByTextFileInfosIdOrderByCounterDesc(textFileInfo.getId());
        List<TextFileWordDTO> textFileWordDTOList = null;
        if (textFileInfo.getUploadStatus().equals(UploadStatus.COMPLETED)){
            textFileWordDTOList = textFileWordMapper.mapToDTO(textFileWords);
        }
        return new TextWordsDTO(textFileInfo.getIdentifier(), textFileInfo.getUploadStatus(), textFileWordDTOList);
    }

    public UploadStatus getUploadStatus(UUID indentifier) {
        return textFileInfoRepository.getUploadStatusByIdentifier(indentifier);
    }

    public TextWordsDTO getUploadedWords(UUID indentifier) {
        TextFileInfo textFileInfo = textFileInfoRepository.getFirstByIdentifierEquals(indentifier);
        return getTextWordsDTO(textFileInfo);
    }

    public TextFileInfo saveFileInfo(int totalChunks){
        return textFileInfoRepository.save(new TextFileInfo(UUID.randomUUID(), UploadStatus.PROCESSING, totalChunks));
    }

    public UUID uploadFile(MultipartFile file) throws IOException {
        ArrayList<String> textChunks = getTextFromMultipartFile(file);
        int totalChunks = textChunks.size();

        TextFileInfo textFileInfo = saveFileInfo(totalChunks);

        for (int i = 1; i < totalChunks+1; i++) {
            rabbitMQProducer.sendTextFilePart(
                    new TextDTO(
                            textFileInfo.getId(),
                            totalChunks,
                            i,
                            textChunks.get(i-1)
                    )
            );
        }
        return textFileInfo.getIdentifier();
    }
}

