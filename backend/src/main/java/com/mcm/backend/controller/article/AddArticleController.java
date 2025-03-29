package com.mcm.backend.controller.article;

import com.mcm.backend.service.article.AddArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddArticleController {
    @Autowired
    AddArticleService addArticleService;

    @PostMapping("/article/add/")
    public String addArticle(@RequestParam Map<String, String> data){
        System.out.println("收到文章" + data);
        return addArticleService.addarticle(data);
    }
}
