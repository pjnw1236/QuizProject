INSERT INTO question (subject, content, create_date) values
    ("첫번째 제목입니다.", "첫번째 내용입니다.", now()),
    ("두번째 제목입니다.", "두번째 내용입니다.", now()),
    ("세번째 제목입니다.", "세번째 내용입니다.", now()),
    ("네번째 제목입니다.", "네번째 내용입니다.", now()),
    ("다섯번째 제목입니다.", "다섯번째 내용입니다.", now());

INSERT INTO answer (content, create_date, question_id) values
    ("첫번째 댓글입니다.", now(), 1),
    ("두번째 댓글입니다.", now(), 1),
    ("세번째 댓글입니다.", now(), 1),
    ("첫번째 댓글입니다.", now(), 2),
    ("두번째 댓글입니다.", now(), 2),
    ("세번째 댓글입니다.", now(), 2);