package com.mcm.backend.service.article;

import com.mcm.backend.pojo.Article;

import java.util.List;

public interface GetUserArticlerService {
    List<Article> getUserArticle(Integer userId);
}
