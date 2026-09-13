package com.wk.ti.question.model;

import java.time.Instant;

public record RecentQuestionResponse(
        Long id,
        String question,
        String tag,
        String level,
        Instant updatedAt,
        String snippetPreview
) {}
