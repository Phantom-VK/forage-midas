package com.jpmc.midascore.services;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {

    @Autowired
    UserRepository userRepository;

    public float getBalanceById(int id) {
        UserRecord userRecord = userRepository.findById(id);
        if(userRecord == null){
            return 0;
        }
        return userRecord.getBalance();
    }
}
