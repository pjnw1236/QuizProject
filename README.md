# 프로그래밍 퀴즈 사이트
* 파이썬, 자바 등 프로그래밍 퀴즈를 풀 수 있는 서비스로 선택한 프로그래밍 언어로 퀴즈를 풀고 채점하는 사이트입니다.  
* 퀴즈에 대해 궁금한 점이 있는 경우 게시판에 의견을 남기실 수 있습니다.

---
**[배포 사이트](http://ec2-13-209-111-85.ap-northeast-2.compute.amazonaws.com:8080)**

---
**[api 문서](https://pjw-1.gitbook.io/quiz-project-api-docs)**  

---
## [아키텍처]
![](../../Quiz_Project_Architecture.png)  

---
## [기술 스택]
![](../../Quiz_Project_Stack.png)

---
## [ERD 관계도]
> * 관리자의 퀴즈 관리용 테이블  
> ![](../../quiz.png)  

> * 유저가 퀴즈를 풀기 위한 테이블 연관관계
> ![](../../memberQuiz.png)

> * 유저가 게시글을 작성하기 위한 테이블 연관관계 (조회수 및 추천)
> ![](../../memberQuestion.png)


> * 유저가 댓글을 작성하기 위한 테이블 연관관계 (추천)
> ![](../../memberAnswer.png)


> * 게시글과 댓글의 테이블 연관관계
> ![](../../memberQuestionAnswer.png)