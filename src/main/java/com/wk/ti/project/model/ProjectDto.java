package com.wk.ti.project.model;

public record ProjectDto(
        Long id,
        String name
) {
    public static ProjectDto of(Project project) {
        return new ProjectDto(project.getId(), project.getProjectName());
    }
}
