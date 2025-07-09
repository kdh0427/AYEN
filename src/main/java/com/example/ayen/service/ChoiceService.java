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

    public ChoiceService(ScenarioPlayRepository scenarioPlayRepository, ScenarioItemRepository scenarioItemRepository, ScenarioPlayStatRepository scenarioPlayStatRepository,
                         ItemRepository itemRepository, SceneRepository sceneRepository, BossRepository bossRepository, EndingRepository endingRepository, UserEndingRepository userEndingRepository, ChoiceRepository choiceRepository, UserRepository userRepository) {
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
        int mana = effect.getMana();
        int intelligence = effect.getIntelligence();
        int agility = effect.getAgility();

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
                scenarioPlaystat.setMana(mana);
                scenarioPlaystat.setIntelligence(intelligence);
                scenarioPlaystat.setAgility(agility);
            } else {
                scenarioPlaystat = new ScenarioPlayStat(scenarioPlay, attack, defense, health, mana, intelligence, agility);
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

    @Transactional
    public void updateCurrentScenarioIdById(Long id, Long currentSceneId) {
        ScenarioPlay scenarioPlay = scenarioPlayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ScenarioPlay not found"));

        Scene currentScene = sceneRepository.findById(currentSceneId)
                .orElseThrow(() -> new RuntimeException("Scene not found"));

        // 엔딩 Scene인 경우
        if (Boolean.TRUE.equals(currentScene.getIsEnding())) {
            scenarioPlay.setEnding_at(LocalDateTime.now());
            scenarioPlay.setUpdated_at(LocalDateTime.now());
            scenarioPlay.setFinished(true);

            // 조건 분기로 엔딩 Scene 지정 (해피/노말/배드)
            Scene endingScene = conditionBranch(scenarioPlay.getId(), scenarioPlay.getScenario());
            scenarioPlay.setScene(endingScene); // 실제 엔딩 씬 설정

            Ending ending = endingRepository.findBySceneId(endingScene.getId())
                    .orElseThrow(() -> new RuntimeException("Ending not found"));
            UserEnding userEnding = new UserEnding(scenarioPlay.getUser(), ending);

            Choice choice = choiceRepository.findBySceneId(currentSceneId)
                    .orElseThrow(() -> new RuntimeException("Choice not found"));
            choice.setNextScene(endingScene);

            User expuser = scenarioPlay.getUser();
            expuser.setExp(ending.getExp());

            userRepository.save(expuser);
            choiceRepository.save(choice);
            userEndingRepository.save(userEnding);
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
        ScenarioPlayStat stat = scenarioPlayStatRepository.findById(playId)
                .orElseThrow(() -> new RuntimeException("Stat not found"));

        Boss boss = bossRepository.findByScenarioId(scenario.getId())
                .orElseThrow(() -> new RuntimeException("Boss not found"));

        int ppower = stat.getAttack() + stat.getAgility() + stat.getIntelligence();
        int pdefense = stat.getDefense();
        int phealth = stat.getHealth();

        int bpower = boss.getAttack();
        int bdefense = boss.getDefense();
        int bhealth = boss.getHealth();

        int playerAttack = Math.max(1, ppower - bdefense);
        int bossAttack = Math.max(1, bpower - pdefense);

        int playerTurns = phealth / bossAttack;
        int bossTurns = bhealth / playerAttack;

        if (bossTurns < playerTurns) {
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
}
