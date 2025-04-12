package com.mcm.backend.service.impl.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.ArticleMapper;
import com.mcm.backend.mapper.TriclickMapper;
import com.mcm.backend.pojo.Article;
import com.mcm.backend.pojo.Triclick;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.article.GetlistArticleLikeService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetlistArticleLikeServiceImpl implements GetlistArticleLikeService {
    @Autowired
    TriclickMapper triclickMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Override
    public List<Article> getlistArticleLike(Integer userId) {
//        UsernamePasswordAuthenticationToken authenticationToken =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
//        User user = loginUser.getUser();
//        Integer userId = user.getId();

        QueryWrapper<Triclick> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                    .eq("operation", "like")
                    .eq("category", "article")
                    .orderByAsc("createtime");
        List<Triclick> triclickList = triclickMapper.selectList(queryWrapper);

        // 创建articleId到triclick记录的映射关系
        Map<Integer, Triclick> triclickMap = new HashMap<>();
        for(Triclick triclick : triclickList){
            triclickMap.put(triclick.getToId(), triclick);
        }

        // 提取to_id字段
        List<Integer> articleIdList = triclickList.stream()
                                                    .map(Triclick::getToId)
                                                    .collect(Collectors.toList());
        List<Article> articlesList = articleMapper.selectBatchIds(articleIdList);
        articlesList.sort(Comparator.comparing(article -> triclickMap.get(article.getId()).getCreatetime()));
        Collections.reverse(articlesList);
        return articlesList;
    }
}
