package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserRecord sender;

    @ManyToOne
    private UserRecord recipient;

    private float amount;

    private float incentive;

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }
    public void setRecipient(UserRecord recipient) {
        this.recipient = recipient;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public void setIncentive(float incentive) {
        this.incentive = incentive;
    }
}

