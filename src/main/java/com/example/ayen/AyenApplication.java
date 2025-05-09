package com.example.ayen;

import com.example.ayen.domain.*;
import com.example.ayen.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class AyenApplication implements CommandLineRunner {

    private final UserRepository urepository;
    
    public AyenApplication(UserRepository urepository) {
        this.urepository = urepository;
    }
    public static void main(String[] args) {
        SpringApplication.run(AyenApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User("user1@gmail.com", "usertoken1", "user1", User.SocialType.GOOGLE, 0, 0, 0, 0);
        User user2 = new User("user2@gmail.com", "usertoken2", "user2", User.SocialType.GOOGLE, 0, 0, 0, 0);
        urepository.saveAll(Arrays.asList(user1, user2));
    }
}