var i = 0;
var txt = "Coding Quiz 사이트에 오신 것을 환영합니다.";
function welcome() {
    if (i < txt.length) {
        document.getElementById("welcome").innerHTML += txt.charAt(i);
        i++;
        setTimeout(welcome, 50);
    }
}
welcome();