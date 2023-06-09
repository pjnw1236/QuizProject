package com.quiz.repository;

import com.quiz.entity.Question;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String subject);
    Question findBySubjectAndContent(String subject, String content);
    List<Question> findBySubjectLike(String subject);
    Page<Question> findAll(Pageable pageable);

    // 제목+내용, 제목, 내용, 댓글, 닉네임
    @Query("select distinct q from Question q where q.subject like %:kw% or q.content like %:kw%")
    Page<Question> findAllBySubjectAndContent(@Param("kw") String kw, Pageable pageable);

    @Query("select distinct q from Question q where q.subject like %:kw%")
    Page<Question> findAllBySubject(@Param("kw") String kw, Pageable pageable);

    @Query("select distinct q from Question q where q.content like %:kw%")
    Page<Question> findAllByContent(@Param("kw") String kw, Pageable pageable);

    @Query("select distinct q from Question q left outer join Answer a on a.question=q where a.content like %:kw%")
    Page<Question> findAllByAnswerContent(@Param("kw") String kw, Pageable pageable);

    @Query("select distinct q from Question q left outer join Member u on q.author=u where u.username like %:kw%")
    Page<Question> findAllByAuthor(@Param("kw") String kw, Pageable pageable);
}
