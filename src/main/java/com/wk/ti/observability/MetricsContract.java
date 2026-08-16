package com.wk.ti.observability;

@SuppressWarnings("unused")
public class MetricsContract {
    public static final String METRIC_QUESTIONS_REQUEST_COUNT = "ti.question.request";
    public static final String METRIC_QUESTION_CREATED_TOTAL = "ti.question.created.total";
    public static final String METRIC_QUESTION_CREATE_FAILED_TOTAL = "ti.question.created.failed.total";

    public static final String METRIC_IMPORTS_REQUESTS_COUNT = "ti.import.request";
    public static final String METRIC_SUCCESSFUL_IMPORTS_COUNT = "ti.import.successful";
    public static final String METRIC_FAILED_IMPORTS_COUNT = "ti.import.failed";

    public static final String METRIC_EXPORTS_REQUESTS_COUNT = "ti.export.request";
    public static final String METRIC_SUCCESSFUL_EXPORTS_COUNT = "ti.export.successful";
    public static final String METRIC_FAILED_EXPORTS_COUNT = "ti.export.failed";

    public static final String METRIC_QUESTION_CREATE_DURATION =
            "ti.question.create.duration";

    public static final String METRIC_QUESTION_UPDATE_DURATION ="ti.question.update.duration";
    public static final String METRIC_QUESTION_UPDATED_TOTAL = "ti.question.updated.total";
    public static final String METRIC_QUESTION_UPDATE_FAILED_TOTAL = "ti.question.updated.failed.total";
}
