package com.mcm.backend.controller.article;

import com.mcm.backend.service.article.GetArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class GetArticleController {
    @Autowired
    private GetArticleService getArticleService;

    @GetMapping("/article/get/")
    public Map<String, String> getArticle(@RequestParam Map<String, String> data){
        Integer ArticleId = Integer.parseInt(data.get("ArticleId"));
        return getArticleService.getarticle(ArticleId);
    }
}
