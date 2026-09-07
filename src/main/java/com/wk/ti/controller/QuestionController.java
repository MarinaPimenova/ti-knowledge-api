package com.wk.ti.controller;

import com.wk.ti.question.model.*;
import com.wk.ti.question.service.QuestionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionProcessor questionProcessor;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionProjection>> findAll() {
        return ResponseEntity.ok(questionProcessor.findAll());
    }


    @GetMapping(value = "/search",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuestionProjection>> findByPattern(
            @RequestParam(defaultValue = "") String pattern
    ) {
        return ResponseEntity.ok(questionProcessor.findByPattern(pattern));
    }

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

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDetails> find(@PathVariable Long id) {
        return ResponseEntity.ok(questionProcessor.find(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDetails> create(
            @RequestBody CreateQuestionRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(questionProcessor.create(request));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<QuestionDetails> update(
            @PathVariable Long id,
            @RequestBody QuestionDetails questionDetails) {
        return ResponseEntity.ok(questionProcessor.modify(QuestionDetails.of(id, questionDetails)));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionProcessor.delete(id);
        return new ResponseEntity<>(HttpStatusCode.valueOf(204));
    }
}
