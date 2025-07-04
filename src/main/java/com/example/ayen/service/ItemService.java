package com.example.ayen.service;

import com.example.ayen.dto.entity.Item;
import com.example.ayen.repository.ItemRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<Item> findIdByName(String itemName) {
        return itemRepository.findIdByName(itemName);
    }
}
