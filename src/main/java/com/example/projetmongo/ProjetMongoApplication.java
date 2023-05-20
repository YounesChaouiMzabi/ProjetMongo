package com.example.projetmongo;

import com.example.projetmongo.models.User;
import com.example.projetmongo.repositories.TodoItemRepository;
import com.example.projetmongo.repositories.TodoRepository;
import com.example.projetmongo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ProjetMongoApplication {

    private UserRepository userRepository;

    private TodoRepository todoRepository;

    private TodoItemRepository todoItemRepository;

    public ProjetMongoApplication(UserRepository userRepository, TodoRepository todoRepository, TodoItemRepository todoItemRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
        this.todoItemRepository = todoItemRepository;
    }

    @Bean
    public CommandLineRunner console(){
        return (args)->{
            System.out.println("-------Debut de mon test---------");
            User user = new User();
            user.setUsername("younes");
            user.setPassword("test");
            userRepository.save(user);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ProjetMongoApplication.class, args);
    }

}
