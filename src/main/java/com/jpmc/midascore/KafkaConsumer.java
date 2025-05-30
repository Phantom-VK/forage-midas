package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import static com.jpmc.midascore.entity.AppConstants.GROUP_ID;
import static com.jpmc.midascore.entity.AppConstants.TOPIC;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void listen(Transaction transaction, ConsumerRecord<String, Transaction> record) {
        logger.info("Received Transaction: {}", transaction);
    }
}
