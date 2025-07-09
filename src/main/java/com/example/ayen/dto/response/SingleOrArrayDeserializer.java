package com.example.ayen.dto.response;

import com.example.ayen.dto.response.UserScene;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SingleOrArrayDeserializer extends JsonDeserializer<List<UserScene.AddItemDto>> {
    @Override
    public List<UserScene.AddItemDto> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);
        List<UserScene.AddItemDto> result = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode itemNode : node) {
                UserScene.AddItemDto item = codec.treeToValue(itemNode, UserScene.AddItemDto.class);
                result.add(item);
            }
        } else if (node.isObject()) {
            UserScene.AddItemDto item = codec.treeToValue(node, UserScene.AddItemDto.class);
            result.add(item);
        } else {
            // 필요시 예외 처리
        }
        return result;
    }
}
