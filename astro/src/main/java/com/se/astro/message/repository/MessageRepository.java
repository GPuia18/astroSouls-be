package com.se.astro.message.repository;

import com.se.astro.message.dto.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends MongoRepository<Message, String> {
    @Query("{ $or: [ { 'senderUsername': ?0, 'receiverUsername': ?1 }, { 'senderUsername': ?1, 'receiverUsername': ?0 } ] }")
    Optional<List<Message>> findAllByUsers(String user1, String user2);

    @Query("{ $or: [ { 'senderUsername': ?0 }, { 'receiverUsername': ?0 } ] }")
    Optional<List<Message>> findAllByUser(String user);
}
