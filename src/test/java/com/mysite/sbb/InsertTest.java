package com.mysite.sbb;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Question;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.repository.AnswerRepository;
import com.mysite.sbb.repository.QuestionRepository;
import com.mysite.sbb.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InsertTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Test
    @DisplayName("유저 데이터 삽입 테스트")
    @Order(1)
    void insertUser() {
        SiteUser user1 = new SiteUser();
        user1.setUsername("test");
        user1.setPassword("$2a$10$MQAXZJtzb3l864JpjWmxzOuShMoFXu38pfsM7U3DrUB.50sYyLxwC");
        user1.setEmail("test@gmail.com");
        userRepository.save(user1);

        SiteUser user2 = new SiteUser();
        user2.setUsername("test1");
        user2.setPassword("$2a$10$JbB3xTe/wNRaTeWlRFs88e7mJhgwwzE23a42p8XQtoC6wgIbWEIM.");
        user2.setEmail("test1@gmail.com");
        userRepository.save(user2);

        SiteUser user3 = new SiteUser();
        user3.setUsername("test2");
        user3.setPassword("$2a$10$ndKsa0D8ftI0VaZQLBEYyu0h/gAECORFO9GrUaGa6T/5RUIJgGbMm");
        user3.setEmail("test2@gmail.com");
        userRepository.save(user3);

        for (int i=1; i<=99; i++) {
            Question question = new Question();
            question.setSubject("[" + i + "]번째 게시글 제목입니다.");
            question.setContent("[" + i + "]번째 게시글 내용입니다.");
            question.setCreateDate(LocalDateTime.now());
            question.setAuthor(user1);
            questionRepository.save(question);
        }

        Question question = new Question();
        question.setSubject("[100]번째 게시글 제목입니다.");
        question.setContent("[100]번째 게시글 내용입니다.");
        question.setCreateDate(LocalDateTime.now());
        question.setAuthor(user1);
        questionRepository.save(question);

        for (int i=1; i<=3; i++) {
            Answer answer = new Answer();
            answer.setContent("[" + i  + "]번째 게시글 댓글입니다.");
            answer.setCreateDate(LocalDateTime.now());
            answer.setAuthor(user2);
            answer.setQuestion(question);
            answerRepository.save(answer);
        }
    }

    @Test
    @DisplayName("질문 데이터 삽입 테스트")
    @Order(2)
    void insertQuestion() {

    }

    @Test
    @DisplayName("답변 데이터 삽입 테스트")
    @Order(3)
    void insertAnswer() {

    }
}
