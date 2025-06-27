package com.example.ayen;

import com.example.ayen.repository.AchievementRepository;
import com.example.ayen.repository.ScenarioRepository;
import com.example.ayen.repository.UserAchievementRepository;
import com.example.ayen.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AyenApplication implements CommandLineRunner {

    private final UserRepository urepository;
    private final ScenarioRepository screpository;
    private final AchievementRepository arepository;
    private final UserAchievementRepository uarepository;

    public AyenApplication(UserRepository urepository, ScenarioRepository screpository, AchievementRepository aRepository, UserAchievementRepository uarepository) {
        this.urepository = urepository;
        this.screpository = screpository;
        this.arepository = aRepository;
        this.uarepository = uarepository;
    }
    public static void main(String[] args) {
        SpringApplication.run(AyenApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        /*
        screpository.save(new Scenario("기억의 숲", "깨어나보니 숲 한가운데. 잃어버린 기억을 찾아야 합니다.", "null"));
        screpository.save(new Scenario("도심 속의 늑대", "도시의 그림자 속, 당신은 진실을 추적합니다.", "null"));
        screpository.save(new Scenario("붉은 달의 전설", "붉은 달이 뜨는 밤, 저주받은 성으로 향합니다.", "null"));

        arepository.save(new Achievement("노 맨즈 랜드", "업적 노 맨즈 랜드 달성", "이업적은 특별한 도전을 통해 달성되었습니다 ", "null", 100));
        arepository.save(new Achievement("블랙 아웃", "업적 블랙 아웃 달성", "이업적은 특별한 도전을 통해 달성되었습니다 ", "null", 100));
        arepository.save(new Achievement("신인 작가 단편선", "업적 신인 작가 단편선 달성", "이업적은 특별한 도전을 통해 달성되었습니다 ", "null", 100));
        arepository.save(new Achievement("미. 연. 시", "업적 미. 연. 시 달성", "이업적은 특별한 도전을 통해 달성되었습니다 ", "null", 100));
        arepository.save(new Achievement("잠실 갈리파", "업적 잠실 갈리파 달성", "이업적은 특별한 도전을 통해 달성되었습니다 ", "null", 100));
        arepository.save(new Achievement("분노의 도로", "업적 분노의 도로 달성", "이업적은 특별한 도전을 통해 달성되었습니다 ", "null", 100));

        var user = new com.example.ayen.dto.entity.User();
        user.setId(1L);

        var achievement = new Achievement();
        achievement.setId(1L);

        var userAchievement = new UserAchievement(user, achievement, java.time.LocalDateTime.now());
        uarepository.save(userAchievement);
        */
    }
}