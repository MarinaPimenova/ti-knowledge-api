package com.wk.ti.question.model;

import java.util.List;

public record CreateQuestionRequest(

        String question,

        String shortAnswer,

        String detailedAnswer,

        Long questionLevelId,

        CodeExampleRequest codeExample,

        List<Long> tagIds,

        List<Long> projectIds,

        List<ResourceRequest> resources
) {
}
