package com.mcm.backend.service.impl.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.ArticleMapper;
import com.mcm.backend.pojo.Article;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.article.GetUserArticlerService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserArticleServiceImpl implements GetUserArticlerService {
    @Autowired
    ArticleMapper articleMapper;
    @Override
    public List<Article> getUserArticle(Integer userId) {
        // 取出所有 user_id == userId 的文章，组织成一个JSON数组返回给前端
////        List<Article> articles = articleMapper.selectList(new QueryWrapper<Article>().eq("user_id", userId));
//        UsernamePasswordAuthenticationToken authenticationToken =
//                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
//        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
//        User user = loginUser.getUser();

        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return articleMapper.selectList(queryWrapper);
    }
}
