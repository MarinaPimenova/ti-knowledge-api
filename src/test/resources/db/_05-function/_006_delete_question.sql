DROP FUNCTION IF EXISTS knowledge.delete_question;

CREATE OR REPLACE PROCEDURE knowledge.delete_question(
    p_question_id BIGINT,
    p_user varchar
)
    LANGUAGE plpgsql
AS $$
BEGIN

    IF NOT EXISTS (SELECT 1
                   FROM knowledge.question q
                   WHERE q.id = p_question_id and (q.created_by = p_user or p_user = 'SYSTEM')) THEN
        RAISE EXCEPTION 'Question with id % does not exist.', p_question_id;
    END IF;

    DELETE FROM knowledge.question_tag
           WHERE question_id = p_question_id
             and (created_by = p_user or p_user = 'SYSTEM');

    DELETE
    FROM knowledge.question_resource
    WHERE question_id = p_question_id
      and (created_by = p_user or p_user = 'SYSTEM');

    DELETE
    FROM knowledge.question_code_example
    WHERE question_id = p_question_id and (created_by = p_user or p_user = 'SYSTEM');

    DELETE
    FROM knowledge.project_question
    WHERE question_id = p_question_id and (created_by = p_user or p_user = 'SYSTEM');

    DELETE FROM knowledge.question
    WHERE id = p_question_id
       and (created_by = p_user or p_user = 'SYSTEM');
END;
$$;
