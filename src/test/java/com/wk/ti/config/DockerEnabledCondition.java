package com.wk.ti.config;

import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.DockerClientFactory;

public class DockerEnabledCondition implements ExecutionCondition {

    @Override
    public @NonNull ConditionEvaluationResult evaluateExecutionCondition(@NonNull ExtensionContext context) {
        try {
            boolean dockerAvailable = DockerClientFactory.instance().isDockerAvailable();
            if (dockerAvailable) {
                return ConditionEvaluationResult.enabled("Docker daemon is available.");
            }
            return ConditionEvaluationResult.disabled("Docker daemon is not available. Skipping Testcontainers tests.");
        } catch (Throwable e) {
            return ConditionEvaluationResult.disabled("Docker environment check failed: " + e.getMessage());
        }
    }
}
