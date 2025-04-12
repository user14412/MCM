package com.mcm.backend.controller.article;

import com.mcm.backend.pojo.Article;
import com.mcm.backend.service.article.GetlistArticleLikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetlistArticleLikeController {
    @Autowired
    GetlistArticleLikeService getlistArticleLikeService;
    @GetMapping("/article/getlist/like/")
    List<Article> getlistArticleLike(@RequestParam Integer userId){
        return getlistArticleLikeService.getlistArticleLike(userId);
    }
}
