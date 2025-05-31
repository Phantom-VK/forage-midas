package com.jpmc.midascore.services;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

public class TransactionService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private TransactionRecordRepository txRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void processTransaction(Transaction transaction) {

        //Find sender by id from our db
        UserRecord sender = userRepo.findById(transaction.getSenderId());
        //Find the recipient by id from our db
        UserRecord recipient = userRepo.findById(transaction.getRecipientId());

        //Validate transaction
        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {

            //Get incentive
            float incentiveAmount = getIncentive(transaction);
            //Update sender balance
            sender.setBalance(sender.getBalance() - transaction.getAmount());

            //Add incentive to recipient's balance only'
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


    }

    private float getIncentive(Transaction transaction) {
        //Get incentive from the incentive service
        Incentive incentive = restTemplate.postForObject(
                "http://localhost:8080/incentive",
                transaction,
                Incentive.class
        );

        //Get incentive
        assert incentive != null;
        return incentive.getAmount();
    }
}
