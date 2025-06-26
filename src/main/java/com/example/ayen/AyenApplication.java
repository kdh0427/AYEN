package com.example.ayen;

import com.example.ayen.dto.Scenario;
import com.example.ayen.repository.ScenarioRepository;
import com.example.ayen.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.support.Repositories;

@SpringBootApplication
public class AyenApplication implements CommandLineRunner {

    private final UserRepository urepository;
    private final ScenarioRepository screpository;

    public AyenApplication(UserRepository urepository, ScenarioRepository screpository, Repositories repositories) {
        this.urepository = urepository;
        this.screpository = screpository;
    }
    public static void main(String[] args) {
        SpringApplication.run(AyenApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        screpository.save(new Scenario("기억의 숲", "깨어나보니 숲 한가운데. 잃어버린 기억을 찾아야 합니다.", "null"));
        screpository.save(new Scenario("도심 속의 늑대", "도시의 그림자 속, 당신은 진실을 추적합니다.", "null"));
        screpository.save(new Scenario("붉은 달의 전설", "붉은 달이 뜨는 밤, 저주받은 성으로 향합니다.", "null"));
    }
}