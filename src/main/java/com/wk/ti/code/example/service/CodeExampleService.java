package com.wk.ti.code.example.service;

import com.wk.ti.code.example.model.CodeExample;
import com.wk.ti.code.example.model.CodeExampleDto;
import com.wk.ti.code.example.model.CodeExampleQuestion;
import com.wk.ti.code.example.model.CodeExampleQuestionKey;
import com.wk.ti.code.example.repository.CodeExampleQuestionRepository;
import com.wk.ti.code.example.repository.CodeExampleRepository;
import com.wk.ti.question.model.Question;
import com.wk.ti.question.model.QuestionDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CodeExampleService {
    private final CodeExampleRepository codeExampleRepository;
    private final CodeExampleQuestionRepository codeExampleQuestionRepository;

    public void save(QuestionDetails result, Question question) {
        if (result.getCodeExample() == null ) {
            return;
        }
        CodeExampleDto codeExampleDto = result.getCodeExample();

        CodeExample codeExample = codeExampleRepository
                .saveAndFlush( CodeExample.builder()
                        .language(codeExampleDto.language())
                        .source_code(codeExampleDto.sourceCode())
                        .build());
        CodeExampleQuestionKey key = CodeExampleQuestionKey.builder()
                .questionId(question.getId())
                .codeExampleId(codeExample.getId())
                .build();
        CodeExampleQuestion codeExampleQuestion = CodeExampleQuestion.builder()
                .codeExampleQuestionKey(key)
                .question(question)
                .codeExample(codeExample)
                .build();
        codeExampleQuestionRepository.save(codeExampleQuestion);
    }
}
