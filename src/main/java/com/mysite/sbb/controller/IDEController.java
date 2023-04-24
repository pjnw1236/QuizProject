package com.mysite.sbb.controller;

import com.mysite.sbb.form.IDEForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class IDEController {
    @GetMapping("/ide")
    public String ide(Model model) {
        return "ide";
    }

//    @PostMapping("/ide/processing")
//    public String ideProcess(@RequestParam("language") String language, @RequestParam("code") String code, @RequestParam("input") String input) {
//        log.info("========================================");
//        log.info("===== language, code, input =====");
//        log.info(language);
//        log.info(code);
//        log.info(input);
//        log.info("===== language, code, input =====");
//        return "ide";
//    }

    @PostMapping("/ide/processing")
    public String ideProcess(@RequestBody IDEForm ideForm) {
        String language = ideForm.getLanguage();
        String code = ideForm.getCode();
        String input = ideForm.getInput();
        log.info("test ok");
        log.info(language);
        log.info(code);
        log.info(input);
        String output = "some output";

        return "ide";
    }
}
