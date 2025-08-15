package me.jeremymegyesi.CharonCommon.kafka.topics;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.config.TopicBuilder;


@Configuration
public class ScheduleKafkaTopicConfig {
    @Bean
    NewTopic topic() {
        return TopicBuilder.name("transit-schedule")
                .partitions(10)
                .replicas(1)
                .build();
    }
}
