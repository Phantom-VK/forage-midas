package com.jpmc.midascore.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import static com.jpmc.midascore.entity.AppConstants.TOPIC;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic topic() {

        return TopicBuilder
                .name(TOPIC)
                .build();
    }
}
