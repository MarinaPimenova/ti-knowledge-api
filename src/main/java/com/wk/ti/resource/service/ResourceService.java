package com.wk.ti.resource.service;

import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionDetails;
import com.wk.ti.resource.model.Resource;
import com.wk.ti.resource.model.ResourceDto;
import com.wk.ti.resource.model.ResourceQuestion;
import com.wk.ti.resource.model.ResourceQuestionKey;
import com.wk.ti.resource.repository.ResourceQuestionRepository;
import com.wk.ti.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {
    private final ResourceRepository resourceRepository;
    private final ResourceQuestionRepository resourceQuestionRepository;

    public void save(QuestionDetails result, Question question) {
        if (result.getResources() == null || result.getResources().isEmpty()) {
            return;
        }
        List<ResourceQuestion> resourceQuestions = new ArrayList<>();
        for (ResourceDto resourceDto : result.getResources()) {
            Resource resource = Resource.builder()
                    .resourceUrl(resourceDto.url())
                    .description(resourceDto.description())
                    .build();
            Resource savedResource = resourceRepository.saveAndFlush(resource);
            ResourceQuestionKey key = ResourceQuestionKey.builder()
                    .questionId(question.getId())
                    .resourceId(savedResource.getId())
                    .build();
            ResourceQuestion rq = ResourceQuestion.builder()
                    .resourceQuestionKey(key)
                    .question(question)
                    .resource(savedResource)
                    .build();
            resourceQuestions.add(rq);
        }
        resourceQuestionRepository.saveAll(resourceQuestions);
    }
}
