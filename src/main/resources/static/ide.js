let editor;

window.onload = function() {
  editor = ace.edit("editor");
  editor.setTheme("ace/theme/xcode");
  editor.session.setMode("ace/mode/python");
}

function changeLanguage() {
  let language = $("#languages").val();
  if(language == 'Python')editor.session.setMode("ace/mode/python");
  else if(language == 'Java')editor.session.setMode("ace/mode/java");
}

function executeCode() {
  $.ajax({
    url: "/ide/processing",
    method: "POST",
    data: {
      language: $("#language").val(),
      code: editor.getSession().getValue(),
      input: $(".input textarea").val()
    },
    success: function(response) {
      $(".output").text(response)
    }
  })
}