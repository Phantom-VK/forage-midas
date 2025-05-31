package com.jpmc.midascore.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class TransactionService {

    @Autowired
    private RestTemplate restTemplate;

}
