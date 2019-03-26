package com.javainiaisuzspringom.tripperis.services;

import com.javainiaisuzspringom.tripperis.domain.ChecklistItem;
import com.javainiaisuzspringom.tripperis.repositories.ChecklistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChecklistItemService {

    @Autowired
    private ChecklistItemRepository checklistItemRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public ChecklistItem save(ChecklistItem checklistItem) {
        return checklistItemRepository.save(checklistItem);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ChecklistItem> getAllChecklistItems() {
        return checklistItemRepository.findAll();
    }
}
