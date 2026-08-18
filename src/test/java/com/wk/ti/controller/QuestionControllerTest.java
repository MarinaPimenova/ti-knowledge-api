package com.wk.ti.controller;

import com.wk.ti.qlevel.model.QuestionLevelDto;
import com.wk.ti.question.model.CreateQuestionRequest;
import com.wk.ti.question.model.QuestionDetails;
import com.wk.ti.question.model.QuestionProjection;
import com.wk.ti.question.service.QuestionProcessor;
import com.wk.ti.user.service.UserDetailExtractor;
import com.wk.ti.util.TestQuestionProjection;
import com.wk.ti.util.TestUtils;
import io.micrometer.core.instrument.MeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.oauth2.server.resource.autoconfigure.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.wk.ti.util.TestUtils.sessionOidc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = QuestionController.class,
        excludeAutoConfiguration = {
                OAuth2ResourceServerAutoConfiguration.class
        })
@ActiveProfiles("test")
class QuestionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    MeterRegistry meterRegistry;

    @MockitoBean
    UserDetailExtractor userDetailExtractor;

    @MockitoBean
    QuestionProcessor questionProcessor;

    @Test
    @WithJwtUser
    void shouldFindAllQuestions() throws Exception {
        // given
        QuestionProjection question = new TestQuestionProjection(
                1L,
                "java",
                "What is Java?",
                "Java is a programming language.",
                "https://example.com/java",
                "Java description",
                "TI Knowledge"
        );

        when(questionProcessor.findAll())
                .thenReturn(List.of(question));

        // when / then
        mockMvc.perform(
                        get("/api/v1/questions")
                                .with(sessionOidc())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].tag").value("java"))
                .andExpect(jsonPath("$[0].question").value("What is Java?"))
                .andExpect(jsonPath("$[0].shortAnswer")
                        .value("Java is a programming language."))
                .andExpect(jsonPath("$[0].resourceUrl")
                        .value("https://example.com/java"))
                .andExpect(jsonPath("$[0].description")
                        .value("Java description"))
                .andExpect(jsonPath("$[0].projectName")
                        .value("TI Knowledge"));

        verify(questionProcessor).findAll();
    }

    @Test
    @WithJwtUser
    void shouldFindQuestion() throws Exception {

        QuestionDetails details =
                QuestionDetails.builder()
                        .id(1L)
                        .question("question")
                        .shortAnswer("answer")
                        .questionLevel(new QuestionLevelDto(1L, "A1"))
                        .build();

        when(questionProcessor.find(1L))
                .thenReturn(details);

        mockMvc.perform(
                        get("/api/v1/questions/1")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("question"))
                .andExpect(jsonPath("$.shortAnswer").value("answer"))
                .andExpect(jsonPath("$.questionLevel.questionLevelId").value(1))
                .andExpect(jsonPath("$.questionLevel.difficultyCode").value("A1"));

        verify(questionProcessor).find(1L);
    }

    @Test
    @WithJwtUser
    void shouldCreateQuestion() throws Exception {
        // given

        QuestionDetails createdQuestion = createQuestionDetails(10L);

        when(questionProcessor.create(any(CreateQuestionRequest.class)))
                .thenReturn(createdQuestion);

        String requestJson = """
                {
                  "question": "What is Java?",
                  "shortAnswer": "Java is a programming language.",
                  "detailedAnswer": "Java is a high-level, object-oriented programming language.",
                  "questionLevelId": 1,
                  "codeExample": null,
                  "tagIds": [1, 2],
                  "projectIds": [1],
                  "resources": []
                }
                """;

        // when / then
        MvcResult result = mockMvc.perform(
                        post("/api/v1/questions")
                                .with(sessionOidc())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        QuestionDetails actual =
                TestUtils.byte2Object(result.getResponse(), QuestionDetails.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(10L);

        verify(questionProcessor).create(any(CreateQuestionRequest.class));
    }

    @Test
    @WithJwtUser
    void shouldUpdateQuestion() throws Exception {
        // given

        QuestionDetails updatedQuestion = QuestionDetails.builder()
                .id(1L)
                .question("Updated question")
                .shortAnswer("Updated answer")
                .detailedAnswer("Updated detailed answer")
                .questionLevel(new QuestionLevelDto(2L, "A2"))
                .build();

        when(questionProcessor.modify(any(QuestionDetails.class)))
                .thenReturn(updatedQuestion);

        String requestJson = """
                {
                  "question": "Updated question",
                  "shortAnswer": "Updated answer",
                  "detailedAnswer": "Updated detailed answer",
                  "questionLevel": {
                    "questionLevelId": 2,
                    "difficultyCode": "A2"
                  },
                  "codeExample": null,
                  "tags": [],
                  "resources": [],
                  "projects": []
                }
                """;

        // when / then
        MvcResult result = mockMvc.perform(
                        put("/api/v1/questions/1")
                                .with(sessionOidc())
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestJson)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        // then
        QuestionDetails actual =
                TestUtils.byte2Object(result.getResponse(), QuestionDetails.class);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isEqualTo(1L);
        assertThat(actual.getQuestion()).isEqualTo("Updated question");
        assertThat(actual.getShortAnswer()).isEqualTo("Updated answer");

        verify(questionProcessor).modify(any(QuestionDetails.class));
    }

    @Test
    @WithJwtUser
    void shouldDeleteQuestion() throws Exception {
        // given
        doNothing().when(questionProcessor).delete(1L);

        // when / then
        mockMvc.perform(
                        delete("/api/v1/questions/1")
                                .with(sessionOidc())
                                .with(csrf())
                )
                .andExpect(status().isNoContent());

        // then
        verify(questionProcessor).delete(1L);
    }

    private QuestionDetails createQuestionDetails(Long id) {
        return QuestionDetails.builder()
                .id(id)
                .question("question")
                .shortAnswer("answer")
                .detailedAnswer("detailed answer")
                .questionLevel(new QuestionLevelDto(1L, "A1"))
                .build();
    }

}