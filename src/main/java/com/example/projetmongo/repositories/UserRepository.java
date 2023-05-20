package com.example.projetmongo.repositories;

import com.example.projetmongo.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    @Query("{ 'username': ?0}")
    User findOneUser(String username);
}
