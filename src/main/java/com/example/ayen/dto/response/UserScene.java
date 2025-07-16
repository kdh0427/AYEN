package com.example.ayen.dto.response;

import com.example.ayen.dto.entity.Choice;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserScene {

    private String content;
    private String imageUrl;
    private boolean Ending;
    private Long next_scene_id;
    private List<ChoiceDto> choices;  // 씬에 속한 다수의 선택지
    private StatDto stats;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChoiceDto {
        private Long nextSceneId;
        private String description;
        private String requiredItem;
        private EffectDto effect;      // JSON 컬럼 파싱 결과
        private StatDto stat;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EffectDto {
        private Integer attack;
        private Integer defense;
        private Integer health;

        @JsonDeserialize(using = SingleOrArrayDeserializer.class)
        private List<AddItemDto> addItem;  // ✅ 단일 객체 → List로 변경
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddItemDto {
        private String name;
        private String description;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StatDto {
        private int attack;
        private int defense;
        private int health;
    }
}