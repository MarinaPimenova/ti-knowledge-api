package com.wk.ti.controller;

import com.wk.ti.question.model.CreateQuestionRequest;
import com.wk.ti.question.model.QuestionDetails;
import com.wk.ti.question.model.QuestionProjection;
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
