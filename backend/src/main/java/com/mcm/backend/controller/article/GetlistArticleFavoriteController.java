package com.mcm.backend.controller.article;

import com.mcm.backend.pojo.Article;
import com.mcm.backend.service.article.GetlistArticleFavoriteService;
import com.mcm.backend.service.article.GetlistArticleLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetlistArticleFavoriteController {
    @Autowired
    GetlistArticleFavoriteService getlistArticleFavoriteService;
    @GetMapping("/article/getlist/favorite/")
    List<Article> getlistArticleLike(){
        return getlistArticleFavoriteService.getlistArticleFavorite();
    }
}
