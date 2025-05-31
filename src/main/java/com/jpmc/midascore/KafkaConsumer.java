package com.jpmc.midascore;

import com.jpmc.midascore.foundation.Incentive;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import static com.jpmc.midascore.foundation.AppConstants.GROUP_ID;
import static com.jpmc.midascore.foundation.AppConstants.TOPIC;

@Component
public class KafkaConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TransactionRecordRepository txRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void listen(Transaction transaction) {
        //Find sender by id from our db
        UserRecord sender = userRepo.findById(transaction.getSenderId());
        //Find recipient by id from our db
        UserRecord recipient = userRepo.findById(transaction.getRecipientId());


        //Validate transaction
        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {

            //Get incentive from the incentive service
            Incentive incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive",
                    transaction,
                    Incentive.class
            );

            //Get incentive
            assert incentive != null;
            float incentiveAmount = incentive.getAmount();

            //Update sender and recipient balance
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);

            //Save users
            userRepo.save(sender);
            userRepo.save(recipient);

            //We create a new transaction record, and add details to it
            TransactionRecord txRecord = new TransactionRecord();
            txRecord.setSender(sender);
            txRecord.setIncentive(incentiveAmount);
            txRecord.setRecipient(recipient);
            txRecord.setAmount(transaction.getAmount());

            //Finally, we save the transaction
            txRepo.save(txRecord);


        }

//        UserRecord wilbur = userRepo.findByname("wilbur");
//        System.out.println("Wilbur's final balance: " + wilbur.getBalance());
    }

}
