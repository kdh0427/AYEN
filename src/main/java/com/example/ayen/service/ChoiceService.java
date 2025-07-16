package com.example.ayen.service;

import com.example.ayen.dto.entity.*;
import com.example.ayen.dto.response.ChoiceRequest;
import com.example.ayen.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChoiceService {

    private final ScenarioPlayRepository scenarioPlayRepository;
    private final ScenarioItemRepository scenarioItemRepository;
    private final ScenarioPlayStatRepository scenarioPlayStatRepository;
    private final ItemRepository itemRepository;
    private final SceneRepository sceneRepository;
    private final BossRepository bossRepository;
    private final EndingRepository endingRepository;
    private final UserEndingRepository userEndingRepository;
    private final ChoiceRepository choiceRepository;
    private final UserRepository userRepository;
    private final UserAchievementRepository userAchievementRepository;
    private final AchievementRepository achievementRepository;

    public ChoiceService(ScenarioPlayRepository scenarioPlayRepository, ScenarioItemRepository scenarioItemRepository, ScenarioPlayStatRepository scenarioPlayStatRepository,
                         ItemRepository itemRepository, SceneRepository sceneRepository, BossRepository bossRepository, EndingRepository endingRepository,
                         UserEndingRepository userEndingRepository, ChoiceRepository choiceRepository, UserRepository userRepository,
                         UserAchievementRepository userAchievementRepository, AchievementRepository achievementRepository) {
        this.scenarioPlayRepository = scenarioPlayRepository;
        this.scenarioItemRepository = scenarioItemRepository;
        this.scenarioPlayStatRepository = scenarioPlayStatRepository;
        this.itemRepository = itemRepository;
        this.sceneRepository = sceneRepository;
        this.bossRepository = bossRepository;
        this.endingRepository = endingRepository;
        this.userEndingRepository = userEndingRepository;
        this.choiceRepository = choiceRepository;
        this.userRepository = userRepository;
        this.userAchievementRepository = userAchievementRepository;
        this.achievementRepository = achievementRepository;
    }

    public Long findActiveScenarioPlayIdByUserId(Long userId) {
        return scenarioPlayRepository.findActiveScenarioPlayIdByUserId(userId);
    }

    //유저 아이디로 최근 씬 검색
    public Long findCurrentSceneIdByUserId(Long userId) {
        return scenarioPlayRepository.findCurrentSceneIdByUserId(userId);
    }
    
    // 선택지 선택했을때 - 테이블이 없는 경우 - 추가 ( 시나리오 아이템, 시나리오 플레이 스텟 )
    public void insertScenarioItemAndScenarioPlayStatIfNotExists(Long id, ChoiceRequest.Effect effect, List<String> itemNames) {
        int attack = effect.getAttack();
        int defense = effect.getDefense();
        int health = effect.getHealth();

        Optional<ScenarioPlay> optionalPlay = scenarioPlayRepository.findById(id);

        if (optionalPlay.isPresent()) {
            ScenarioPlay scenarioPlay = optionalPlay.get();

            // ScenarioPlayStat 처리
            Optional<ScenarioPlayStat> optionalStat = scenarioPlayStatRepository.findByScenarioPlay(scenarioPlay);
            ScenarioPlayStat scenarioPlaystat;
            if(optionalStat.isPresent()) {
                scenarioPlaystat = optionalStat.get();
                scenarioPlaystat.setAttack(attack);
                scenarioPlaystat.setDefense(defense);
                scenarioPlaystat.setHealth(health);
            } else {
                scenarioPlaystat = new ScenarioPlayStat(scenarioPlay, attack, defense, health);
            }
            scenarioPlayStatRepository.save(scenarioPlaystat);

            // 아이템 여러 개 처리
            if (itemNames != null && !itemNames.isEmpty()) {
                for (String itemName : itemNames) {
                    Optional<Item> optionalItem = itemRepository.findIdByName(itemName);
                    if (optionalItem.isPresent()) {
                        Item item = optionalItem.get();
                        Optional<ScenarioItem> existing = scenarioItemRepository.findByScenarioPlayAndItem(scenarioPlay, item);
                        if (existing.isEmpty()) {
                            ScenarioItem scenarioItem = new ScenarioItem(scenarioPlay, item, 1);
                            scenarioItemRepository.save(scenarioItem);
                        }
                    }
                }
            }
        }
    }

    public boolean hasRequiredItem(Long scenarioPlayId, String requiredItemName) {
        List<ScenarioItem> scenarioItems = scenarioItemRepository.findByScenarioPlayId(scenarioPlayId);

        List<String> itemNames = scenarioItems.stream()
                .map(item -> item.getItem().getName())
                .collect(Collectors.toList());

        return itemNames.stream()
                .anyMatch(name -> name.trim().equalsIgnoreCase(requiredItemName.trim()));

    }

    @Transactional
    public void updateCurrentScenarioIdById(Long id, Long currentSceneId, List<String> itemNames, ChoiceRequest.Effect stats) { // 시나리오 id, 최근 씬 id
        ScenarioPlay scenarioPlay = scenarioPlayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ScenarioPlay not found"));

        Scene currentScene = sceneRepository.findById(currentSceneId)
                .orElseThrow(() -> new RuntimeException("Scene not found"));

        ScenarioPlayStat scenarioPlayStat = scenarioPlayStatRepository.findByScenarioPlayId(id)
                .orElseThrow(() -> new RuntimeException("ScenarioPlayStat not found"));

        scenarioPlayStat = setStats(scenarioPlayStat, stats);
        scenarioPlayStatRepository.save(scenarioPlayStat);

        // 아이템 추가
        if (itemNames != null && !itemNames.isEmpty()) {
            for (String itemName : itemNames) {
                Optional<Item> optionalItem = itemRepository.findIdByName(itemName);
                if (optionalItem.isPresent()) {
                    Item item = optionalItem.get();
                    Optional<ScenarioItem> existing = scenarioItemRepository.findByScenarioPlayAndItem(scenarioPlay, item);
                    if (existing.isEmpty()) {
                        ScenarioItem scenarioItem = new ScenarioItem(scenarioPlay, item, 1);
                        scenarioItemRepository.save(scenarioItem);
                    }
                }
            }
        }
        
        // 엔딩 Scene인 경우
        if (Boolean.TRUE.equals(currentScene.getIsEnding())) {
            scenarioPlay.setEnding_at(LocalDateTime.now()); // 엔딩날짜
            scenarioPlay.setUpdated_at(LocalDateTime.now()); // 최근 업데이트 날짜
            scenarioPlay.setFinished(true); // 엔딩 여부

            // 조건 분기로 엔딩 Scene 지정 (해피/노말/배드)
            Scene endingScene = conditionBranch(scenarioPlay.getId(), scenarioPlay.getScenario());
            scenarioPlay.setScene(endingScene); // 실제 엔딩 씬 설정

            Ending ending = endingRepository.findBySceneId(endingScene.getId())
                    .orElseThrow(() -> new RuntimeException("Ending not found"));

            // 엔딩 -> 유저 엔딩에 추가
            // 엔딩에 관한 업적만 추가
            UserEnding userEnding = new UserEnding(scenarioPlay.getUser(), ending);
            userEndingRepository.save(userEnding);
            
            Choice choice = choiceRepository.findBySceneId(currentSceneId)
                    .orElseThrow(() -> new RuntimeException("Choice not found"));
            choice.setNextScene(endingScene);

            // 유저 경험치 및 레벨 계산
            User expuser = scenarioPlay.getUser();
            int totalExp = expuser.getLevel() * 1500 + expuser.getExp() + ending.getExp();
            int newLevel = totalExp / 1500;
            int newExp = totalExp % 1500;

            expuser.setLevel(newLevel);
            expuser.setExp(newExp);

            // 업적 id 가져오기
            EndingType endingType = ending.getEndingType();
            Achievement achievement = achievementRepository.findByConditionValues(scenarioPlay.getChosen_role(), endingType.name());

            // 엔딩 업적 없다면 엔딩 업적 추가
            if (achievement != null) {
                boolean exists = userAchievementRepository.existsByUser_IdAndAchievement_Id(
                        userEnding.getUser().getId(),
                        achievement.getId()
                );

                if (!exists) {
                    UserAchievement userAchievement = new UserAchievement(scenarioPlay.getUser(), achievement);
                    userAchievementRepository.save(userAchievement);

                    // 업적 카운트 + 1
                    int cnt = expuser.getAchievementCount();
                    expuser.setAchievementCount(++cnt);
                }
            }

            // 정보 업데이트
            userRepository.save(expuser);
            choiceRepository.save(choice);
            scenarioPlayRepository.save(scenarioPlay);
        } else{
            Long nextSceneId = currentSceneId + 1;

            Scene nextScene = sceneRepository.findById(nextSceneId)
                    .orElseThrow(() -> new RuntimeException("Next scene not found"));

            // 일반 Scene인 경우
            scenarioPlay.setScene(nextScene);
            scenarioPlay.setUpdated_at(LocalDateTime.now());
            scenarioPlayRepository.save(scenarioPlay);
        }
    }

    // 결말 분기
    public Scene conditionBranch(Long playId, Scenario scenario) {
        ScenarioPlayStat stat = scenarioPlayStatRepository.findByScenarioPlayId(playId)
                .orElseThrow(() -> new RuntimeException("Stat not found"));

        Boss boss = bossRepository.findByScenarioId(scenario.getId())
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        int battack = boss.getAttack();
        int bdefence = boss.getDefense();
        int bhealth = boss.getHealth();

        Long scenarioId = scenario.getId();
        if(scenarioId == 1){
            int pattack = stat.getAttack();
            int pdefence = stat.getDefense();
            int phealth = stat.getHealth();

            int bossTurns = phealth / (battack - pdefence);
            int playerTurns = bhealth / (pattack - bdefence);

            if (bossTurns > playerTurns) {
                return sceneRepository.findByScenarioAndContent(scenario, "해피엔딩")
                        .orElseThrow(() -> new RuntimeException("해피엔딩 없음"));
            } else if (bossTurns == playerTurns) {
                return sceneRepository.findByScenarioAndContent(scenario, "노말엔딩")
                        .orElseThrow(() -> new RuntimeException("노말엔딩 없음"));
            } else {
                return sceneRepository.findByScenarioAndContent(scenario, "배드엔딩")
                        .orElseThrow(() -> new RuntimeException("배드엔딩 없음"));
            }
        }
        else if(scenarioId == 2){
            int power = stat.getAttack();
            int morality = stat.getDefense();
            int health = stat.getHealth();

            int bossTurns = health / battack;
            int playerTurns = bhealth / power;

            List<ScenarioItem> items = scenarioItemRepository.findByScenarioPlayId(playId);
            List<String> itemNames = items.stream()
                    .map(item -> item.getItem().getName())
                    .collect(Collectors.toList());

            if (bossTurns > playerTurns) {
                return sceneRepository.findByScenarioAndContent(scenario, "해피엔딩")
                        .orElseThrow(() -> new RuntimeException("해피엔딩 없음"));
            }
            else if(itemNames.contains("과거의 기억") && itemNames.contains("용서의 힘") && morality >= 40){
                return sceneRepository.findByScenarioAndContent(scenario, "히든엔딩")
                        .orElseThrow(() -> new RuntimeException("히든엔딩 없음"));
            }
            else if(itemNames.contains("진실의 조각") && itemNames.contains("미래의 편린") && power + morality + health >= 120){
                return sceneRepository.findByScenarioAndContent(scenario, "진엔딩")
                        .orElseThrow(() -> new RuntimeException("진엔딩 없음"));
            }
            else if (bossTurns == playerTurns) {
                return sceneRepository.findByScenarioAndContent(scenario, "노말엔딩")
                        .orElseThrow(() -> new RuntimeException("노말엔딩 없음"));
            }
            else if (bossTurns < playerTurns) {
                return sceneRepository.findByScenarioAndContent(scenario, "배드엔딩")
                        .orElseThrow(() -> new RuntimeException("배드엔딩 없음"));
            }
        }
        return null;
    }

    // stat 적용
    public ScenarioPlayStat setStats(ScenarioPlayStat stat, ChoiceRequest.Effect stats) {
        stat.setAttack(stats.getAttack());
        stat.setDefense(stats.getDefense());
        stat.setHealth(stats.getHealth());

        return stat;
    }
}
