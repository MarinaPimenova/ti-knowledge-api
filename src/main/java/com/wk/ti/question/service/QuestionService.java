package com.wk.ti.question.service;

import com.wk.ti.question.model.QuestionProjection;
import com.wk.ti.question.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<QuestionProjection> findAllQuestion() {
        String user = auth() == null ? "SYSTEM" : auth().getName().toLowerCase();
        return questionRepository.findAllQuestion(user);
    }

    private Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
