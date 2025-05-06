package com.mcm.backend.service.article;

import com.mcm.backend.pojo.Article;
import java.util.List;

public interface GetNewsArticleService {
    List<Article> getNewsArticles(Integer page, Integer size, String keyword);
    Long getNewsArticlesCount(String keyword);  // 改为 Long 类型
}