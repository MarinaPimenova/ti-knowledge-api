package com.wk.ti.util;

import com.wk.ti.question.model.QuestionProjection;

public class TestQuestionProjection implements QuestionProjection {

    private final long id;
    private final String tags;
    private final String question;
    private final String shortAnswer;
    private final String resources;
    private final String projectName;

    public TestQuestionProjection(
            long id,
            String tags,
            String question,
            String shortAnswer,
            String resources,
            String projectName) {

        this.id = id;
        this.tags = tags;
        this.question = question;
        this.shortAnswer = shortAnswer;
        this.resources = resources;
        this.projectName = projectName;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getTags() {
        return tags;
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
    public String getResources() {
        return resources;
    }

    @Override
    public String getProjectName() {
        return projectName;
    }
}
