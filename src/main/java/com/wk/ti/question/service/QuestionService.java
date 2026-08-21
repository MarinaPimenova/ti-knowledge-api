package com.wk.ti.question.service;

import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionDetails;
import com.wk.ti.question.model.QuestionProjection;
import com.wk.ti.question.repository.QuestionDetailsRepository;
import com.wk.ti.question.repository.QuestionRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wk.ti.observability.MetricsContract.*;
import static com.wk.ti.user.service.UserDetailExtractor.getUser;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionDetailsRepository detailsRepository;
    private final Counter questionRequestCounter;

    public QuestionService(
            QuestionRepository questionRepository,
            QuestionDetailsRepository detailsRepository,
            MeterRegistry meterRegistry) {
        this.questionRepository = questionRepository;
        this.detailsRepository = detailsRepository;
        this.questionRequestCounter = meterRegistry.counter(METRIC_QUESTIONS_REQUEST_COUNT);
    }

    public QuestionDetails find(Long id) {
        return detailsRepository.findById(id)
                .orElseThrow();
    }

    public List<QuestionProjection> findAll() {
        questionRequestCounter.increment();
        return questionRepository.findAllQuestion();
    }

    @Transactional
    public Question modify(Question question) {
        return questionRepository.saveAndFlush(question);
    }

    @Transactional
    public void remove(Long id) {
        String user = getUser();
        questionRepository.remove(id, user);
    }

}
