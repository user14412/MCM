package com.mcm.backend.controller.article;

import com.mcm.backend.pojo.Article;
import com.mcm.backend.service.article.GetArticleByUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GetArticleByIdController {
    @Autowired
    private GetArticleByUserService getArticleByUserService;

    @GetMapping("/article/get/user/")
    public List<Article> getArticleByUser(){
        return getArticleByUserService.getarticlebyuser();
    }
}
