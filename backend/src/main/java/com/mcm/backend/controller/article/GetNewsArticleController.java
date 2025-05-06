package com.mcm.backend.controller.article;

import com.mcm.backend.pojo.Article;
import com.mcm.backend.service.article.GetNewsArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class GetNewsArticleController {
    @Autowired
    private GetNewsArticleService getNewsArticleService;

    @GetMapping("/article/get/news/")
    public Map<String, Object> getNewsArticles(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        Map<String, Object> result = new HashMap<>();
        List<Article> articles = getNewsArticleService.getNewsArticles(page, size, keyword);
        Long total = getNewsArticleService.getNewsArticlesCount(keyword);  // 改为 Long 类型
        result.put("articles", articles);
        result.put("total", total);
        return result;
    }
}