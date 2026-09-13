package com.wk.ti.controller;

import com.wk.ti.knowledge.tag.model.TagDto;
import com.wk.ti.knowledge.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/v1/questions/tags")
@RequiredArgsConstructor
public class PublicTagController {
    private final TagService tagService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TagDto>> findAll() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(tagService.count());
    }
}
