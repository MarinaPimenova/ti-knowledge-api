package com.wk.ti.controller;

import com.wk.ti.question.model.*;
import com.wk.ti.question.service.QuestionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/v1/questions")
@RequiredArgsConstructor
public class PublicQuestionController {
    private final QuestionProcessor questionProcessor;

    // To back "Recently Added Questions" UI component, you need an endpoint
    // that fetches a fixed limit of recently created/updated questions
    // and maps them to a tailored DTO containing the title, primary tag/category,
    // level (e.g., "A2"),
    // relative update date string (or timestamp), and a snippet preview.
    @GetMapping(value = "/recent", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecentQuestionResponse>> findRecent(
            @RequestParam(defaultValue = "3") int limit) {
        return ResponseEntity.ok(questionProcessor.findRecent(limit));
    }

    @GetMapping(value = "/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(questionProcessor.count());
    }

    // To query the count of questions aggregated by tags
    @GetMapping(value = "/tags/count", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionTagCountResponse>> countByTag() {
        return ResponseEntity.ok(questionProcessor.countByTags());
    }

}
