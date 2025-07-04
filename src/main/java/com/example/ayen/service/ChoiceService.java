package com.example.ayen.service;

import com.example.ayen.dto.entity.*;
import com.example.ayen.dto.response.ChoiceRequest;
import com.example.ayen.repository.*;
import jakarta.transaction.Transactional;
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

    public ChoiceService(ScenarioPlayRepository scenarioPlayRepository, ScenarioItemRepository scenarioItemRepository,
                         ScenarioPlayStatRepository scenarioPlayStatRepository, ItemRepository itemRepository, SceneRepository sceneRepository) {
        this.scenarioPlayRepository = scenarioPlayRepository;
        this.scenarioItemRepository = scenarioItemRepository;
        this.scenarioPlayStatRepository = scenarioPlayStatRepository;
        this.itemRepository = itemRepository;
        this.sceneRepository = sceneRepository;
    }

    public Long findActiveScenarioPlayIdByUserId(Long userId) {
        return scenarioPlayRepository.findActiveScenarioPlayIdByUserId(userId);
    }

    //유저 아이디로 최근 씬 검색
    public Long findCurrentSceneIdByUserId(Long userId) {
        return scenarioPlayRepository.findCurrentSceneIdByUserId(userId);
    }
    
    // 선택지 선택했을때 - 테이블이 없는 경우 - 추가 ( 시나리오 아이템, 시나리오 플레이 스텟 )
    public void insertScenarioItemAndScenarioPlayStatIfNotExists(Long id, ChoiceRequest.Effect effect, String itemName) {
        int attack = effect.getAttack();
        int defense = effect.getDefense();
        int health = effect.getHealth();
        int mana = effect.getMana();
        int intelligence = effect.getIntelligence();
        int agility = effect.getAgility();

        Optional<ScenarioPlay> optionalPlay = scenarioPlayRepository.findById(id);
        Optional<Item> optionalItem = itemRepository.findIdByName(itemName);

        if (optionalPlay.isPresent()) {
            ScenarioPlay scenarioPlay = optionalPlay.get();

            // 기존에 ScenarioPlayStat 있는지 확인
            Optional<ScenarioPlayStat> optionalStat = scenarioPlayStatRepository.findByScenarioPlay(scenarioPlay);

            ScenarioPlayStat scenarioPlaystat;
            if(optionalStat.isPresent()) {
                // 있으면 update
                scenarioPlaystat = optionalStat.get();
                scenarioPlaystat.setAttack(attack);
                scenarioPlaystat.setDefense(defense);
                scenarioPlaystat.setHealth(health);
                scenarioPlaystat.setMana(mana);
                scenarioPlaystat.setIntelligence(intelligence);
                scenarioPlaystat.setAgility(agility);
            } else {
                // 없으면 새로 생성
                scenarioPlaystat = new ScenarioPlayStat(scenarioPlay, attack, defense, health, mana, intelligence, agility);
            }
            scenarioPlayStatRepository.save(scenarioPlaystat);

            if (optionalItem.isPresent()) {
                Item item = optionalItem.get();
                Optional<ScenarioItem> existing = scenarioItemRepository.findByScenarioPlayAndItem(scenarioPlay, item);

                if (existing.isEmpty()) {
                    ScenarioItem scenarioItem = new ScenarioItem(scenarioPlay, item, 1);
                    scenarioItemRepository.save(scenarioItem);  // ✅ 이때만 저장
                }
            }

        }
    }

    @Transactional
    public void updateCurrentScenarioIdById(Long id, Long currentScenarioId) {
        ScenarioPlay scenarioPlay = scenarioPlayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ScenarioPlay not found"));

        Scene currentScene = sceneRepository.findById(currentScenarioId)
                .orElseThrow(() -> new RuntimeException("Scene not found"));

        if (currentScene.getIsEnding()) {
            scenarioPlay.setEnding_at(LocalDateTime.now());
            scenarioPlay.set_finished(true);
        } else {
            // 그냥 currentScene을 설정
            scenarioPlay.setScene(currentScene);
        }

        scenarioPlay.setUpdated_at(LocalDateTime.now());
        scenarioPlayRepository.save(scenarioPlay);
    }

}
