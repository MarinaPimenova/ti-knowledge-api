package com.wk.ti.question.model;

@SuppressWarnings("unused")
public interface QuestionProjection {

    long getId();

    String getTag();

    String getQuestion();

    String getShortAnswer();

    String getResourceUrl();

    String getDescription();

    String getProjectName();

}
