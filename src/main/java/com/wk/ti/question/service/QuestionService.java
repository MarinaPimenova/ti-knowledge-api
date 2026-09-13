package com.wk.ti.question.service;

import com.wk.ti.knowledge.tag.model.TagDto;
import com.wk.ti.question.model.*;
import com.wk.ti.question.repository.QuestionDetailsRepository;
import com.wk.ti.question.repository.QuestionRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.wk.ti.observability.MetricsContract.*;
import static com.wk.ti.user.service.UserDetailExtractor.auth;
import static com.wk.ti.user.service.UserDetailExtractor.getUser;

@SuppressWarnings("SameParameterValue")
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

    public List<RecentQuestionResponse> findRecent(int limit) {
        //Pageable pageable = PageRequest.of(0, Math.min(limit, 50));
        String user = null;
        if (auth() != null) {
            user = getUser();
        }
        return detailsRepository.findAllByOrderByUpdatedDateDesc(limit, user)
                .stream()
                .map(q -> new RecentQuestionResponse(
                        q.getId(),
                        q.getQuestion(),
                        q.getTags() != null
                                ? String.join(", ", q.getTags().stream().map(TagDto::tag).toList())
                                : "",
                        q.getQuestionLevel() != null ? q.getQuestionLevel().difficultyCode() : null,
                        q.getUpdatedDate() != null ? q.getUpdatedDate().toInstant() : null,
                        truncateSnippet(q.getShortAnswer(), 60)
                ))
                .toList();
    }

    private String truncateSnippet(String content, int maxLength) {
        if (content == null || content.length() <= maxLength) {
            return content;
        }
        return content.substring(0, maxLength) + "...";
    }

    public QuestionDetails find(Long id) {
        return detailsRepository.findById(id)
                .orElseThrow();
    }

    public List<QuestionProjection> findAll() {
        questionRequestCounter.increment();
        if (auth() == null) {
            return questionRepository.findAllQuestion(null);
        }
        return questionRepository.findAllQuestion(getUser());
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

    public Long questionCount() {
        if (auth() == null) {
            return questionRepository.questionCount(null);
        }
        return questionRepository.questionCount(getUser());
    }

    public List<QuestionTagCountResponse> countByTags() {
        return questionRepository.countByTags();
    }

    public List<QuestionProjection> findByPattern(String pattern) {
        if (auth() == null) {
            return questionRepository.findByPattern(
                    pattern,
                    null);
        }
        return questionRepository.findByPattern(pattern, getUser());
    }
}
