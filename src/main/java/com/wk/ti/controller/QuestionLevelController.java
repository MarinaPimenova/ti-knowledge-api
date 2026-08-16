package com.wk.ti.controller;

import com.wk.ti.qlevel.model.QuestionLevelDto;
import com.wk.ti.qlevel.service.QuestionLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/qlevels")
@RequiredArgsConstructor
public class QuestionLevelController {
    private final QuestionLevelService questionLevelService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionLevelDto>> findAll() {
        return ResponseEntity.ok(questionLevelService.findAll());
    }
}
