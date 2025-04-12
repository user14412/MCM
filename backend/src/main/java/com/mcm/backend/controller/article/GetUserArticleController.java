package com.mcm.backend.controller.article;

import com.mcm.backend.pojo.Article;
import com.mcm.backend.service.article.GetUserArticlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetUserArticleController {
    @Autowired
    private GetUserArticlerService getArticleByUserService;

    @GetMapping("/article/get/user/")
    public List<Article> getArticleByUser(@RequestParam Integer userId){
        return getArticleByUserService.getUserArticle(userId);
    }
}
