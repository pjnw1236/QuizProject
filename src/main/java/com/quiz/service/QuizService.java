package com.quiz.service;

import com.quiz.dto.MemberQuizDto;
import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.entity.Member;
import com.quiz.entity.MemberQuiz;
import com.quiz.entity.Quiz;
import com.quiz.exception.DataNotFoundException;
import com.quiz.repository.MemberQuizRepository;
import com.quiz.repository.QuizRepository;
import com.quiz.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class QuizService {
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;
    private final MemberQuizRepository memberQuizRepository;

    public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
        Quiz quiz = Quiz.dtoToEntity(quizRequestDto);
        quizRepository.save(quiz);
        QuizResponseDto quizResponseDto = Quiz.entityToDto(quiz);
        return quizResponseDto;
    }

    public List<QuizResponseDto> getQuizList() {
        List<Quiz> quizList = quizRepository.findAll();
        List<QuizResponseDto> quizResponseDtoList = Quiz.entityListToDtoList(quizList);
        return quizResponseDtoList;
    }

    public QuizResponseDto getQuizResponseDtoById(Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        return Quiz.entityToDto(quiz.get());
    }

    public QuizResponseDto getQuizResponseDtoByQuizNumber(Long id) {
        Optional<Quiz> quiz = quizRepository.findByQuizNumber(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        return Quiz.entityToDto(quiz.get());
    }

    public QuizRequestDto getQuizRequestDto(Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        return Quiz.entityToRequestDto(quiz.get());
    }

    public void patchQuiz(Long id, QuizRequestDto quizRequestDto) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        quiz.get().setTitle(quizRequestDto.getTitle());
        quiz.get().setPythonContent(quizRequestDto.getPythonContent());
        quiz.get().setJavaContent(quizRequestDto.getJavaContent());
        quiz.get().setFirst(quizRequestDto.getFirst());
        quiz.get().setSecond(quizRequestDto.getSecond());
        quiz.get().setThird(quizRequestDto.getThird());
        quiz.get().setFourth(quizRequestDto.getFourth());
        quiz.get().setQuizAnswer(quizRequestDto.getQuizAnswer());
    }

    public Quiz getQuizEntity(Long id) {
        Optional<Quiz> quiz = quizRepository.findByQuizNumber(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        return quiz.get();
    }

    public void solvingPythonQuiz(Member member, MemberQuizDto memberQuizDto) {
        Optional<MemberQuiz> OptionalMemberQuiz = memberQuizRepository.findByQuizNumberAndQuizType(memberQuizDto.getQuizNumber(), memberQuizDto.getQuizType());
        if (OptionalMemberQuiz.isPresent()) {
            MemberQuiz memberQuiz = OptionalMemberQuiz.get();
            memberQuiz.setMemberQuizAnswer(memberQuizDto.getMemberQuizAnswer());
            memberQuizRepository.save(memberQuiz);
        } else {
            MemberQuiz memberQuiz = new MemberQuiz();
            memberQuiz.setQuizNumber(memberQuizDto.getQuizNumber());
            memberQuiz.setQuizAnswer(memberQuizDto.getQuizAnswer());
            memberQuiz.setQuizType(memberQuizDto.getQuizType());
            memberQuiz.setMemberQuizAnswer(memberQuizDto.getMemberQuizAnswer());
            memberQuiz.setMember(member);
            member.getMemberQuizList().add(memberQuiz);
            memberQuizRepository.save(memberQuiz);
        }
    }

    public void solvingJavaQuiz(Member member, MemberQuizDto memberQuizDto) {
        Optional<MemberQuiz> OptionalMemberQuiz = memberQuizRepository.findByQuizNumberAndQuizType(memberQuizDto.getQuizNumber(), memberQuizDto.getQuizType());
        if (OptionalMemberQuiz.isPresent()) {
            MemberQuiz memberQuiz = OptionalMemberQuiz.get();
            memberQuiz.setMemberQuizAnswer(memberQuizDto.getMemberQuizAnswer());
            memberQuizRepository.save(memberQuiz);
        } else {
            MemberQuiz memberQuiz = new MemberQuiz();
            memberQuiz.setQuizNumber(memberQuizDto.getQuizNumber());
            memberQuiz.setQuizAnswer(memberQuizDto.getQuizAnswer());
            memberQuiz.setQuizType(memberQuizDto.getQuizType());
            memberQuiz.setMemberQuizAnswer(memberQuizDto.getMemberQuizAnswer());
            memberQuiz.setMember(member);
            member.getMemberQuizList().add(memberQuiz);
            memberQuizRepository.save(memberQuiz);
        }
    }
}
