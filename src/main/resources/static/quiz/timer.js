function getCookie(quizType) {
    var cookieName = quizType + "EndTime";
    var cookieInfos = document.cookie.split(";");
    for (cookieInfo of cookieInfos) {
        var key = cookieInfo.split("=")[0].trim();
        var value = parseInt(cookieInfo.split("=")[1].trim());
        if (key === cookieName) {
            return value;
        }
    }
    return null;
}
function setCookie(quizType) {
    var cookieName = quizType + "EndTime";
    var startTime = Math.floor(Date.now() / 1000);
    var endTime = startTime + 900;
    document.cookie = cookieName + "=" + endTime;
}
function timer(quizType) {
    var endTime = getCookie(quizType);
    if (endTime === null) {
        setCookie(quizType);
    }

    var countdown = setInterval(function () {
        var currentTime = Math.floor(Date.now() / 1000);
        var remainTime = getCookie(quizType) - currentTime;
        var minutes = Math.floor(remainTime / 60);
        var seconds = remainTime % 60;
        if (remainTime >= 0) {
            document.getElementById("countdown").innerHTML = "" + minutes + " m " + seconds + " s";
        } else {
            clearInterval(countdown);
            alert("퀴즈 시간이 종료되었습니다. 채점 링크로 이동합니다.");
            location.href = "/quiz/" + quizType + "/result";
            deleteCookie(quizType);
        }
    }, 1000);
}
function deleteCookie(quizType) {
    var cookieName = quizType + "EndTime";
    document.cookie = cookieName + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC;";
}

timer(quizType);
