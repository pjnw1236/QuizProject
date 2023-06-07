# 프로그래밍 퀴즈 사이트
* 파이썬, 자바 등 프로그래밍 퀴즈를 풀 수 있는 서비스로 선택한 프로그래밍 언어로 퀴즈를 풀고 채점하는 사이트입니다.  
* 퀴즈에 대해 궁금한 점이 있는 경우 게시판에 의견을 남기실 수 있습니다.

<!-- --- -->
**[배포 사이트](http://ec2-13-209-111-85.ap-northeast-2.compute.amazonaws.com:8080)**  
**[api 문서](https://pjw-1.gitbook.io/quiz-project-api-docs)**  

<!-- --- -->
## [아키텍처]
![Quiz_Project_Architecture](https://github.com/pjnw1236/QuizProject/assets/97827368/20478a15-f396-43d1-b978-990c83d2f236)

<!-- --- -->
## [기술 스택]
![Quiz_Project_Stack](https://github.com/pjnw1236/QuizProject/assets/97827368/b76af1c0-9248-4756-a729-369e941c3e0b)

<!-- --- -->
## [ERD 관계도]
> * 관리자의 퀴즈 관리용 테이블    
>> ![quiz](https://github.com/pjnw1236/QuizProject/assets/97827368/ffdf2a27-9253-4fbe-a0e5-956b43b8df69)

> * 유저가 퀴즈를 풀기 위한 테이블 연관관계  
>> ![memberQuiz](https://github.com/pjnw1236/QuizProject/assets/97827368/1afb5fbc-430a-45b9-ae98-81b543b5395e)

> * 유저가 게시글을 작성하기 위한 테이블 연관관계 (조회수 및 추천)  
>> ![memberQuestion](https://github.com/pjnw1236/QuizProject/assets/97827368/84e0be1d-53cd-4b15-966a-d28a2870d22c)

> * 유저가 댓글을 작성하기 위한 테이블 연관관계 (추천)  
>> ![memberAnswer](https://github.com/pjnw1236/QuizProject/assets/97827368/df9cb3b5-3340-44ec-841c-09aa502a956b)

> * 게시글과 댓글의 테이블 연관관계  
>> ![memberQuestionAnswer](https://github.com/pjnw1236/QuizProject/assets/97827368/046d6b18-b38e-4e57-9017-9aedaab3b288)
