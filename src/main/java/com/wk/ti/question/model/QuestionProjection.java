package com.wk.ti.question.model;

@SuppressWarnings("unused")
public interface QuestionProjection {

    long getId();

    String getTags();

    String getQuestion();

    String getShortAnswer();

    String getResources();

    String getProjectName();

}
