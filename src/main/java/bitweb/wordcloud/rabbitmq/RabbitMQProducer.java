package bitweb.wordcloud.rabbitmq;

import bitweb.wordcloud.upload.TextDTO;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Value("${rabbitmq.exchange.words}")
    private String exchange;
    @Value("${rabbitmq.routing.key.words}")
    private String routingKey;
    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendTextFilePart(TextDTO text){
        rabbitTemplate.convertAndSend(exchange, routingKey, text);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }
}
