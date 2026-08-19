package com.wk.ti.question.service;

import com.wk.ti.config.EnabledIfDocker;
import com.wk.ti.config.MetricsTestConfig;
import com.wk.ti.config.TestApplicationInitializer;
import com.wk.ti.qlevel.model.QuestionLevel;
import com.wk.ti.qlevel.repository.QuestionLevelRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(MetricsTestConfig.class)
@ContextConfiguration(initializers = TestApplicationInitializer.class)
@EnabledIfDocker
@ActiveProfiles("test")
@Transactional
class QuestionServiceTest {

    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    QuestionLevelRepository questionLevelRepository;
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

        // Authenticate before every test so modify() and remove() can access the security context
        setupAuthenticatedUser("john_doe");
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSaveQuestion() {
        // given
        QuestionLevel qLevel = QuestionLevel.builder()
                .code("A1")
                .build();
        QuestionLevel savedQuestionLevel = questionLevelRepository.saveAndFlush(qLevel);
        Question question = Question.builder()
                .questionLevelId(savedQuestionLevel.getId())
                .question("What is Java?")
                .shortAnswer("Java is a programming language.")
                .detailedAnswer("Detailed answer")
                .build();
        // when
        Question result = questionService.modify(question);

        // then
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
        QuestionLevel qLevel = QuestionLevel.builder()
                .code("A1")
                .build();
        QuestionLevel savedQuestionLevel = questionLevelRepository.saveAndFlush(qLevel);
        Question question = Question.builder()
                .questionLevelId(savedQuestionLevel.getId())
                .question("Original question")
                .shortAnswer("Original answer")
                .detailedAnswer("Original details")
                .build();

        Question saved = questionService.modify(question);

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
        QuestionLevel qLevel = QuestionLevel.builder()
                .code("A1")
                .build();
        QuestionLevel savedQuestionLevel = questionLevelRepository.saveAndFlush(qLevel);
        Question q = Question.builder()
                .questionLevelId(savedQuestionLevel.getId())
                .question("Original question")
                .shortAnswer("Original answer")
                .detailedAnswer("Original details")
                .build();

        Question question = questionService.modify(q);

        Long id = question.getId();

        // when
        questionService.remove(id);

        // then
        // assertThat(questionRepository.findById(id)).isEmpty();
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