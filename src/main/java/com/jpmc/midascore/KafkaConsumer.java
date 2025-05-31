package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.jpmc.midascore.foundation.AppConstants.GROUP_ID;
import static com.jpmc.midascore.foundation.AppConstants.TOPIC;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    TransactionService transactionService;

    @Transactional
    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void listen(Transaction transaction) {

        transactionService.processTransaction(transaction);

    }

}

