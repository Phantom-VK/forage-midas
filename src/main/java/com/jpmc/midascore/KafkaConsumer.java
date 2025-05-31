package com.jpmc.midascore;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import static com.jpmc.midascore.entity.AppConstants.GROUP_ID;
import static com.jpmc.midascore.entity.AppConstants.TOPIC;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TransactionRecordRepository txRepo;

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void listen(Transaction transaction) {
        UserRecord sender = userRepo.findById(transaction.getSenderId());
        UserRecord recipient = userRepo.findById(transaction.getRecipientId());

        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());

            TransactionRecord txRecord = new TransactionRecord();
            txRecord.setSender(sender);
            txRecord.setRecipient(recipient);
            txRecord.setAmount(transaction.getAmount());

            txRepo.save(txRecord);
            userRepo.save(sender);
            userRepo.save(recipient);
        }
    }

}
