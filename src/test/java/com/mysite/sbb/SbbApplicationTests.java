package com.mysite.sbb;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@AfterEach
	void afterEach() {
		Question q1 = questionRepository.findById(1).get();
		if (q1 != null) {
			if (!q1.getSubject().equals("첫번째 제목입니다.")) {
				q1.setSubject("첫번째 제목입니다.");
			}
			questionRepository.save(q1);
		}
		Question q5 = questionRepository.findById(5).get();
		if (q5 == null) {
			q5.setId(5);
			q5.setSubject("다섯번째 제목입니다.");
			q5.setContent("다섯번째 내용입니다.");
			q5.setCreateDate(LocalDateTime.now());
			questionRepository.save(q5);
		}
	}

	@Test
	@Order(1)
	void testFindAll() {
		List<Question> all = questionRepository.findAll();
		assertEquals(5, all.size());

		Question q = all.get(0);
		assertEquals("첫번째 제목입니다.", q.getSubject());
	}

	@Test
	@Order(2)
	void testFindById() {
		Optional<Question> oq = questionRepository.findById(1);
		if (oq.isPresent()) {
			Question q = oq.get();
			assertEquals("첫번째 제목입니다.", q.getSubject());
		}
	}

	@Test
	@Order(3)
	void testFindBySubject() {
		Question q = questionRepository.findBySubject("첫번째 제목입니다.");
		assertEquals(1, q.getId());
	}

	@Test
	@Order(4)
	void testFindBySubjectAndContent() {
		Question q = questionRepository.findBySubjectAndContent("첫번째 제목입니다.", "첫번째 내용입니다.");
		assertEquals(1, q.getId());
	}

	@Test
	@Order(5)
	void testFindBySubjectLike() {
		List<Question> qList = questionRepository.findBySubjectLike("첫번째%");
		Question q = qList.get(0);
		assertEquals("첫번째 제목입니다.", q.getSubject());
	}

	@Test
	@Order(6)
	void testSetSubject() {
		Optional<Question> oq = questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 첫번째 제목입니다.");
		questionRepository.save(q);
		Question findQuestion = questionRepository.findById(1).get();
		assertEquals("수정된 첫번째 제목입니다.", findQuestion.getSubject());
	}

	@Test
	@Order(8)
	void testFindByAnswerToQuestion() {
		Optional<Answer> oa = answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(1, a.getQuestion().getId());
	}

	@Test
	@Order(8)
	@Transactional
	void testFindByQuestionToAnswer() {
		Optional<Question> oq = questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(3, answerList.size());
		assertEquals("첫번째 댓글입니다.", answerList.get(0).getContent());
	}
}
