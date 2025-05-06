package com.mcm.backend.service.impl.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.ArticleMapper;
import com.mcm.backend.pojo.Article;
import com.mcm.backend.service.article.GetNewsArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetNewsArticleServiceImpl implements GetNewsArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Article> getNewsArticles(Integer page, Integer size, String keyword) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "news");
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("title", keyword);
        }
        queryWrapper.orderByDesc("createtime");
        queryWrapper.last("LIMIT " + (page - 1) * size + "," + size);
        return articleMapper.selectList(queryWrapper);
    }

    @Override
    public Long getNewsArticlesCount(String keyword) {  // 改为 Long 类型
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", "news");
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryWrapper.like("title", keyword);
        }
        return articleMapper.selectCount(queryWrapper);
    }
}