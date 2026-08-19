package com.wk.ti.util;

import com.wk.ti.question.model.QuestionProjection;

public class TestQuestionProjection implements QuestionProjection {

    private final long id;
    private final String tag;
    private final String question;
    private final String shortAnswer;
    private final String resourceUrl;
    private final String description;
    private final String projectName;

    public TestQuestionProjection(
            long id,
            String tag,
            String question,
            String shortAnswer,
            String resourceUrl,
            String description,
            String projectName) {

        this.id = id;
        this.tag = tag;
        this.question = question;
        this.shortAnswer = shortAnswer;
        this.resourceUrl = resourceUrl;
        this.description = description;
        this.projectName = projectName;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public String getShortAnswer() {
        return shortAnswer;
    }

    @Override
    public String getResourceUrl() {
        return resourceUrl;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }
}
