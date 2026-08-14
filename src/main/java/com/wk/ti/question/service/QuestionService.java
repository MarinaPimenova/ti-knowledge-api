package com.wk.ti.question.service;

import com.wk.ti.question.model.QuestionProjection;
import com.wk.ti.question.repository.QuestionRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.wk.ti.observability.MetricsContract.*;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final Counter questionRequestCounter;
    //private final Counter questionUpdatedCounter;

    public QuestionService(
            QuestionRepository questionRepository,
            MeterRegistry meterRegistry) {
        this.questionRepository = questionRepository;
        this.questionRequestCounter = meterRegistry.counter(METRIC_QUESTIONS_REQUEST_COUNT);
    }

    public List<QuestionProjection> findAllQuestion() {
        String user = auth() == null ? "SYSTEM" : auth().getName().toLowerCase();
        questionRequestCounter.increment();
        return questionRepository.findAllQuestion(user);
    }

    private Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
