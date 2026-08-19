package com.wk.ti.question.service;

import com.wk.ti.config.EnabledIfDocker;
import com.wk.ti.config.MetricsTestConfig;
import com.wk.ti.config.TestApplicationInitializer;
import com.wk.ti.question.model.Question;
import com.wk.ti.question.repository.QuestionDetailsRepository;
import com.wk.ti.question.repository.QuestionRepository;

import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MetricsTestConfig.class)
@ContextConfiguration(initializers = TestApplicationInitializer.class)
@EnabledIfDocker
@ActiveProfiles("test")
class QuestionServiceTest {

    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionDetailsRepository detailsRepository;
    @Autowired
    private MeterRegistry meterRegistry;

    @BeforeEach
    void setUp() {
        questionService = new QuestionService(
                questionRepository,
                detailsRepository,
                meterRegistry
        );
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSaveQuestion() {
        Question question = Question.builder()
                .questionLevelId(1L)
                .question("What is Java?")
                .shortAnswer("Java is a programming language.")
                .detailedAnswer("Detailed answer")
                .build();

        Question result = questionRepository.save(question); //questionService.modify(question);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();

        Question persisted = questionRepository
                .findById(result.getId())
                .orElseThrow();

        assertThat(persisted.getQuestion())
                .isEqualTo("What is Java?");
    }

    @Test
    void shouldModifyQuestion() {
        // given
        Question question = Question.builder()
                .questionLevelId(1L)
                .question("Original question")
                .shortAnswer("Original answer")
                .detailedAnswer("Original details")
                .build();

        Question saved = questionRepository.save(question);

        // when
        saved.setQuestion("Updated question");
        saved.setShortAnswer("Updated answer");

        Question updated = questionRepository.save(saved);

        // then
        assertThat(updated.getId()).isEqualTo(saved.getId());

        Question persisted = questionRepository
                .findById(saved.getId())
                .orElseThrow();

        assertThat(persisted.getQuestion()).isEqualTo("Updated question");
        assertThat(persisted.getShortAnswer()).isEqualTo("Updated answer");
    }

    @Test
    void shouldRemoveQuestion() {
        // given
        setupAuthenticatedUser("john_doe");

        Question question = questionRepository.saveAndFlush(
                Question.builder()
                        .questionLevelId(1L)
                        .question("Question to delete")
                        .shortAnswer("Answer")
                        .detailedAnswer("Details")
                        .build()
        );

        Long id = question.getId();

        // when
        //questionService.remove(id);
        questionRepository.deleteById(id);

        // then
        assertThat(questionRepository.findById(id)).isEmpty();
    }

    private void setupAuthenticatedUser(String nickname) {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("nickname", nickname)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(jwt, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}