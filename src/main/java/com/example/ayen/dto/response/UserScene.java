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

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChoiceDto {
        private Long nextSceneId;
        private String description;
        private EffectDto effect;      // JSON 컬럼 파싱 결과
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EffectDto {
        private Integer attack;
        private Integer defense;
        private Integer health;
        private Integer mana;
        private Integer intelligence;
        private Integer agility;

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
}