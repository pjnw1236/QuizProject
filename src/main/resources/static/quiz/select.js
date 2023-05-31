function selectAnswer(memberQuizAnswer) {
    alert("선택한 답변 : " + memberQuizAnswer);
    var data = {
        "quizNumber": quizNumber,
        "quizAnswer": quizAnswer,
        "memberQuizAnswer": memberQuizAnswer,
        "quizType" : (quizType === "python" ? "Python" : (quizType === "java" ? "Java" : ""))
    };
    console.log(data);
    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            'X-XSRF-TOKEN': csrfToken
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if (response.ok) {return response.json();}
        else {throw new Error("요청이 실패했습니다.");}
    }).then(responseData => {console.log(responseData);
    }).catch(error => {console.error(error);});
}