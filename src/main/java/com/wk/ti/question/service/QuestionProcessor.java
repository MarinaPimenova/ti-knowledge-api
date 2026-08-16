package com.wk.ti.question.service;

import com.wk.ti.code.example.model.CodeExampleDto;
import com.wk.ti.code.example.service.CodeExampleService;
import com.wk.ti.knowledge.tag.model.TagDto;
import com.wk.ti.knowledge.tag.service.TagService;
import com.wk.ti.project.model.ProjectDto;
import com.wk.ti.project.service.ProjectService;
import com.wk.ti.qlevel.model.QuestionLevelDto;
import com.wk.ti.question.model.CreateQuestionRequest;
import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionDetails;
import com.wk.ti.question.model.QuestionProjection;
import com.wk.ti.resource.model.ResourceDto;
import com.wk.ti.resource.service.ResourceService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static com.wk.ti.observability.MetricsContract.*;

@Service
public class QuestionProcessor {
    private final QuestionService questionService;
    private final CodeExampleService codeExampleService;
    private final ProjectService projectService;
    private final ResourceService resourceService;
    private final TagService tagService;
    private final Timer createQuestionTimer;
    private final Timer updateQuestionTimer;
    private final Counter createQuestionSuccess;
    private final Counter createQuestionFailed;

    private final Counter updateQuestionSuccess;
    private final Counter updateQuestionFailed;

    public QuestionProcessor(
            QuestionService questionService,
            CodeExampleService codeExampleService,
            ProjectService projectService,
            ResourceService resourceService,
            TagService tagService,
            MeterRegistry meterRegistry) {
        this.questionService = questionService;
        this.codeExampleService = codeExampleService;
        this.projectService = projectService;
        this.resourceService = resourceService;
        this.tagService = tagService;
        this.createQuestionTimer = meterRegistry.timer(METRIC_QUESTION_CREATE_DURATION);
        this.updateQuestionTimer = meterRegistry.timer(METRIC_QUESTION_UPDATE_DURATION);
        this.createQuestionSuccess =
                meterRegistry.counter(METRIC_QUESTION_CREATED_TOTAL);

        this.createQuestionFailed =
                meterRegistry.counter(METRIC_QUESTION_CREATE_FAILED_TOTAL);
        this.updateQuestionSuccess =
                meterRegistry.counter(METRIC_QUESTION_UPDATED_TOTAL);

        this.updateQuestionFailed =
                meterRegistry.counter(METRIC_QUESTION_UPDATE_FAILED_TOTAL);
    }

    @Transactional
    public QuestionDetails create(CreateQuestionRequest request) {

        return createQuestionTimer.record(() -> {

            try {
                Question question = questionService.modify(
                        Question.builder()
                                .question(request.question())
                                .shortAnswer(request.shortAnswer())
                                .detailedAnswer(request.detailedAnswer())
                                .questionLevelId(request.questionLevelId())
                                .build()
                );

                QuestionDetails details = toQuestionDetails(request);

                tagService.save(details, question);
                projectService.save(details, question);
                resourceService.save(details, question);
                codeExampleService.save(details, question);

                createQuestionSuccess.increment();

                return questionService.find(question.getId());

            } catch (Exception ex) {

                createQuestionFailed.increment();

                throw ex;
            }
        });
    }

    @Transactional
    public QuestionDetails modify(QuestionDetails details) {

        return updateQuestionTimer.record(() -> {

            try {
                Question question = questionService.modify(
                        Question.builder()
                                .id(details.getId())
                                .question(details.getQuestion())
                                .shortAnswer(details.getShortAnswer())
                                .detailedAnswer(details.getDetailedAnswer())
                                .questionLevelId(details.getQuestionLevel().questionLevelId())
                                .build()
                );

                QuestionDetails result =
                        QuestionDetails.of(question.getId(), details);

                tagService.save(result, question);
                resourceService.save(result, question);
                codeExampleService.save(result, question);
                projectService.save(result, question);

                updateQuestionSuccess.increment();

                return questionService.find(question.getId());

            } catch (Exception ex) {
                updateQuestionFailed.increment();
                throw ex;
            }
        });
    }

    @Transactional
    public void delete(Long id) {
        questionService.remove(id);
    }

    public QuestionDetails find(Long id) {
        return questionService.find(id);
    }

    public List<QuestionProjection> findAll() {
        return questionService.findAll();
    }

    private QuestionDetails toQuestionDetails(
            CreateQuestionRequest request) {

        List<TagDto> tags =
                request.tagIds() == null
                        ? Collections.emptyList()
                        : request.tagIds()
                        .stream()
                        .map(id -> new TagDto(id, null))
                        .toList();

        List<ProjectDto> projects =
                request.projectIds() == null
                        ? Collections.emptyList()
                        : request.projectIds()
                        .stream()
                        .map(id -> new ProjectDto(id, null))
                        .toList();

        List<ResourceDto> resources =
                request.resources() == null
                        ? Collections.emptyList()
                        : request.resources()
                        .stream()
                        .map(resource ->
                             new ResourceDto(
                                     null,
                                     resource.url(),
                                     resource.description()
                             )
                        )
                        .toList();

        CodeExampleDto codeExample = null;

        if (request.codeExample() != null) {
            codeExample = new CodeExampleDto(
                    request.codeExample().language(),
                    request.codeExample().sourceCode()
            );
        }

        QuestionLevelDto questionLevelDto = null;
        if (request.questionLevelId() != null) {
            questionLevelDto = new QuestionLevelDto(request.questionLevelId(), null);
        }

        return new QuestionDetails(
                null,
                request.question(),
                request.shortAnswer(),
                request.detailedAnswer(),
                questionLevelDto,
                codeExample,
                tags,
                resources,
                projects,
                null,
                null
        );
    }
}
