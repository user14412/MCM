package com.mcm.backend.service.impl.article;

import com.mcm.backend.mapper.ArticleMapper;
import com.mcm.backend.pojo.Article;
import com.mcm.backend.service.article.GetArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetArticleServiceImpl implements GetArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Override
    public Map<String, String> getarticle(Integer ArticleId) {
        // 根据文章id获取文章详情
        // user_id username photo title content createtime
        Article article = articleMapper.selectById(ArticleId);
        Map<String, String> map = new HashMap<>();
        if(article != null){
            // 文章存在
            map.put("username", article.getUsername());
            map.put("photo", article.getPhoto());
            map.put("title", article.getTitle());
            map.put("content", article.getContent());
            map.put("createtime", article.getCreatetime().toString());
        }
        System.out.println("resp返回：" + map.get("title"));
        return map;
    }
}
