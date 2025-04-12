package com.mcm.backend.service.article;

import com.mcm.backend.pojo.Article;

import java.util.List;

public interface GetlistArticleLikeService {
    List<Article> getlistArticleLike(Integer userId);
}
