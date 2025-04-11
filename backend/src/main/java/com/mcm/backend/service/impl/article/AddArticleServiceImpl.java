package com.mcm.backend.service.impl.article;

import com.mcm.backend.mapper.ArticleMapper;
import com.mcm.backend.pojo.Article;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.article.AddArticleService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class AddArticleServiceImpl implements AddArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public String addarticle(Article data) {
        /*{
            title: "xxx",
            content: "xxx",
        }*/
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        String title = data.getTitle();
        String content = data.getContent();

        Integer userId = user.getId();
        String username = user.getUsername();
        String photo = user.getPhoto();
        Date now = new Date();
        Article article = new Article(null, userId, username, photo, title, content, now);
        articleMapper.insert(article);
//        System.out.println("插入文章：" + article);
        return "add article success";
    }
}
