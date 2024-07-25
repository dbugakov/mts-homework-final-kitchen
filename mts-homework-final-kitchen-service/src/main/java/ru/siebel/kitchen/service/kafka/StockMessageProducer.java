package ru.siebel.kitchen.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import ru.siebel.order.dto.Order;
import ru.siebel.order.dto.kafka.OrderMessage;
import ru.siebel.stock.dto.Stock;
import ru.siebel.stock.dto.kafka.StockMessage;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockMessageProducer {

    private final StreamBridge streamBridge;

    @Transactional
    public void sendRequest(Stock dto) {
        StockMessage stockRequest = StockMessage.builder()
                .stock(dto)
                .build();

        Message<StockMessage> message =
                MessageBuilder
                        .withPayload(stockRequest)
                        .setHeader(KafkaHeaders.MESSAGE_KEY, dto.getId().toString().getBytes(StandardCharsets.UTF_8))
                        .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                        .build();

        String bindingName = "final.stock";
        streamBridge.send(bindingName, message);
    }
}