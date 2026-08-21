package com.wk.ti.knowledge.tag.service;

import com.wk.ti.knowledge.tag.model.Tag;
import com.wk.ti.knowledge.tag.model.TagDto;
import com.wk.ti.knowledge.tag.model.TagQuestion;
import com.wk.ti.knowledge.tag.model.TagQuestionKey;
import com.wk.ti.knowledge.tag.repository.TagQuestionRepository;
import com.wk.ti.knowledge.tag.repository.TagRepository;
import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final TagQuestionRepository tagQuestionRepository;

    public void save(QuestionDetails result, Question question) {
        if (result.getTags() == null) {
            return;
        }
        List<TagQuestion> tagQuestions = new ArrayList<>();
        for (TagDto tagDto : result.getTags()) {
            Tag tag = tagRepository.findById(tagDto.id())
                    .orElseThrow(() -> new IllegalArgumentException(
                            format("Tag with id [%s] not found", tagDto.id())
                    ));
            TagQuestionKey key = TagQuestionKey.builder()
                    .questionId(question.getId())
                    .tagId(tag.getId())
                    .build();
            TagQuestion tagQuestion = TagQuestion.builder()
                    .tagQuestionKey(key)
                    .question(question)
                    .tag(tag)
                    .build();
            tagQuestions.add(tagQuestion);
        }
        tagQuestionRepository.saveAll(tagQuestions);
    }

    public List<TagDto> findAll() {
        return tagRepository.findAll()
                .stream()
                .map(TagDto::of)
                .toList();
    }
}
