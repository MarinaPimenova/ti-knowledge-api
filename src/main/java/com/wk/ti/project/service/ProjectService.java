package com.wk.ti.project.service;

import com.wk.ti.project.model.Project;
import com.wk.ti.project.model.ProjectDto;
import com.wk.ti.project.model.ProjectQuestion;
import com.wk.ti.project.model.ProjectQuestionKey;
import com.wk.ti.project.repository.ProjectQuestionRepository;
import com.wk.ti.project.repository.ProjectRepository;
import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.wk.ti.user.service.UserDetailExtractor.auth;
import static com.wk.ti.user.service.UserDetailExtractor.getUser;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectQuestionRepository projectQuestionRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public void save(QuestionDetails details, Question question) {

        if (details.getProjects() == null) {
            return;
        }
        List<ProjectQuestion> projectQuestions = new ArrayList<>();
        for (ProjectDto projectDto : details.getProjects()) {
            ProjectQuestionKey key = ProjectQuestionKey.builder()
                    .projectId(projectDto.id())
                    .questionId(details.getId())
                    .build();
            Project project = projectRepository.findById(projectDto.id())
                    .orElseThrow(() -> new IllegalArgumentException(
                            format("Project with id [%s] not found", projectDto.id())
                    ));
            ProjectQuestion projectQuestion = ProjectQuestion.builder()
                    .projectQuestionKey(key)
                    .question(question)
                    .project(project)
                    .build();
            projectQuestions.add(projectQuestion);
        }
        projectQuestionRepository.saveAll(projectQuestions);
    }

    public List<ProjectDto> findAll() {
        List<Project> projects;
        if (auth() == null) {
            projects = projectRepository.findAllProjects(null);
        } else {
            projects = projectRepository.findAllProjects(getUser());
        }

        return projects.stream()
                .map(ProjectDto::of)
                .toList();
    }

    public Long count() {
        if (auth() == null) {
            return projectRepository.projectCount(null);
        }
        return projectRepository.projectCount(getUser());
    }
}
