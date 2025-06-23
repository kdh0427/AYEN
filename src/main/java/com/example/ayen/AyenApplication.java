package com.example.ayen;

import com.example.ayen.domain.*;
import com.example.ayen.domain.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class AyenApplication{

    private final UserRepository urepository;
    
    public AyenApplication(UserRepository urepository) {
        this.urepository = urepository;
    }
    public static void main(String[] args) {
        SpringApplication.run(AyenApplication.class, args);
    }

}