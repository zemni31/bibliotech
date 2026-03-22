package com.bibliotech.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@Profile("mongo")   // ← chargé uniquement avec le profil mongo
@EnableReactiveMongoRepositories(
        basePackages = "com.bibliotech.data.repository.mongo"
)
public class MongoConfig {
}