package br.com.futurehub.futurehubgs.web;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
public class HelloController {

    private final MessageSource ms;

    public HelloController(MessageSource ms) {
        this.ms = ms;
    }

    @GetMapping("/hello")
    public String hello(Locale locale) {
        return ms.getMessage("app.title", null, locale);
    }
}
