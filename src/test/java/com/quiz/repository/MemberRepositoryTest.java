package com.quiz.repository;

import com.quiz.entity.Member;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestMethodOrder(OrderAnnotation.class)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Order(1)
    @Test
    @DisplayName("MemberRepository 유저 삽입 및 삭제 테스트")
    public void insertAndDeleteMemberTest() {
        int startCount = memberRepository.findAll().size();

        Member member1 = new Member();
        member1.setUsername("user01");
        member1.setPassword("1234");
        member1.setEmail("user01@gmail.com");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setUsername("user02");
        member2.setPassword("5678");
        member2.setEmail("user02@gmail.com");
        memberRepository.save(member2);

        Optional<Member> findMember1 = memberRepository.findById(member1.getId());
        Optional<Member> findMember2 = memberRepository.findById(member2.getId());

        Assertions.assertThat(findMember1.isPresent());
        Assertions.assertThat(findMember1.get().getUsername()).isEqualTo("user01");
        Assertions.assertThat(findMember1.get().getPassword()).isEqualTo("1234");
        Assertions.assertThat(findMember1.get().getEmail()).isEqualTo("user01@gmail.com");
        Assertions.assertThat(findMember1.get().getIsOauth()).isEqualTo(false);

        Assertions.assertThat(findMember2.isPresent());
        Assertions.assertThat(findMember2.get().getUsername()).isEqualTo("user02");
        Assertions.assertThat(findMember2.get().getPassword()).isEqualTo("5678");
        Assertions.assertThat(findMember2.get().getEmail()).isEqualTo("user02@gmail.com");
        Assertions.assertThat(findMember2.get().getIsOauth()).isEqualTo(false);

        memberRepository.deleteById(findMember1.get().getId());
        Assertions.assertThat(memberRepository.findById(findMember1.get().getId())).isEmpty();

        memberRepository.deleteById(findMember2.get().getId());
        Assertions.assertThat(memberRepository.findById(findMember2.get().getId())).isEmpty();

        int endCount = memberRepository.findAll().size();

        Assertions.assertThat(startCount).isEqualTo(endCount);
    }

    @Order(2)
    @Test
    @DisplayName("MemberRepository 메소드 테스트")
    public void memberRepositoryMethodTest() {
        int startCount = memberRepository.findAll().size();

        Member member3 = new Member();
        member3.setUsername("user03");
        member3.setPassword("abcd");
        member3.setEmail("user02@gmail.com");
        memberRepository.save(member3);

        Member member4 = new Member();
        member4.setUsername("user04");
        member4.setPassword("efgh");
        member4.setEmail("user04@gmail.com");
        member4.setIsOauth(true);
        memberRepository.save(member4);

        Optional<Member> findMember1 = memberRepository.findById(member3.getId());
        Optional<Member> findMember2 = memberRepository.findById(member4.getId());
        List<Member> findMember3 = memberRepository.findByUsernameAndIsOauth("user03", false);
        List<Member> findMember4 = memberRepository.findByEmailAndIsOauth("user04@gmail.com", true);

        Assertions.assertThat(findMember3).hasSize(1);
        Assertions.assertThat(findMember4).hasSize(1);
        Assertions.assertThat(findMember1.get()).isEqualTo(findMember3.get(0));
        Assertions.assertThat(findMember2.get()).isEqualTo(findMember4.get(0));

        memberRepository.deleteById(findMember1.get().getId());
        Assertions.assertThat(memberRepository.findById(findMember1.get().getId())).isEmpty();

        memberRepository.deleteById(findMember2.get().getId());
        Assertions.assertThat(memberRepository.findById(findMember2.get().getId())).isEmpty();

        int endCount = memberRepository.findAll().size();

        Assertions.assertThat(startCount).isEqualTo(endCount);
    }
}