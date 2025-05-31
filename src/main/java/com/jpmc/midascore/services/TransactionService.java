package com.jpmc.midascore.services;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRecordRepository txRepo;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public void processTransaction(Transaction transaction) {

        // Try to find sender and recipient from DB
        UserRecord sender = userRepo.findById(transaction.getSenderId());
        UserRecord recipient = userRepo.findById(transaction.getRecipientId());

        // Validate that both users exist and sender has enough balance
        if (sender == null || recipient == null) return;
        if (sender.getBalance() < transaction.getAmount()) return;

        // Fetch incentive amount from external incentive service
        float incentiveAmount = fetchIncentive(transaction);

        // Deduct amount from sender
        sender.setBalance(sender.getBalance() - transaction.getAmount());

        // Add amount + incentive to recipient
        recipient.setBalance((float) (recipient.getBalance() + transaction.getAmount() + incentiveAmount));

        // Persist updated user balances
        userRepo.save(sender);
        userRepo.save(recipient);

        // Log the transaction in the transaction table
        TransactionRecord txRecord = new TransactionRecord(
                sender, recipient, transaction.getAmount(),  incentiveAmount);

        txRepo.save(txRecord);
    }


    private float fetchIncentive(Transaction transaction) {
        try {
            Incentive incentive = restTemplate.postForObject(
                    "http://localhost:8080/incentive",
                    transaction,
                    Incentive.class
            );
            return (incentive != null) ? incentive.getAmount() : 0f;
        } catch (Exception e) {
            // Log the failure and continue with 0 incentive
            System.err.println("Failed to fetch incentive: " + e.getMessage());
            return 0f;
        }
    }
}
