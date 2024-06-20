package org.example.banking.bankingapi.controllers;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping()
public class StaticContentController {

    private final ResourceLoader resourceLoader;

    @Autowired
    public StaticContentController(@Nonnull final ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public Resource serveStaticContent() {
        return resourceLoader.getResource("classpath:/static/index.html");
    }
}