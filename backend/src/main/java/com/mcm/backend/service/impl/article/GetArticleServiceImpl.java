package com.mcm.backend.service.impl.article;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.ArticleMapper;
import com.mcm.backend.mapper.TriclickMapper;
import com.mcm.backend.pojo.Article;
import com.mcm.backend.pojo.Triclick;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.article.GetArticleService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetArticleServiceImpl implements GetArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    TriclickMapper triclickMapper;

    @Override
    public Map<String, String> getarticle(Integer ArticleId) {
        // 根据文章id获取文章详情
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Article article = articleMapper.selectById(ArticleId);
        /* TODO：在triclick表中查询是否存在user_id==user.getId() && to_id == ArticleId && operation == "like" && category == "article"的项，如果有，isLiked put "true" 否则 "false"
                在triclick表中查询是否存在user_id==user.getId() && to_id == ArticleId && operation == "favorite" && category == "article"的项，如果有，isFavorited put "true" 否则 "false"
        * */

        Map<String, String> map = new HashMap<>();
        if(article != null){
            // 文章存在
            map.put("username", article.getUsername());
            map.put("photo", article.getPhoto());
            map.put("title", article.getTitle());
            map.put("content", article.getContent());
            map.put("createtime", article.getCreatetime().toString());
            // 查询是否点赞
            Triclick likeTriclick = triclickMapper.selectOne(
                    new QueryWrapper<Triclick>()
                            .eq("user_id", user.getId())
                            .eq("to_id", ArticleId)
                            .eq("operation", "like")
                            .eq("category", "article")
            );
            // 查询是否收藏
            Triclick favoriteTriclick = triclickMapper.selectOne(
                    new QueryWrapper<Triclick>()
                            .eq("user_id", user.getId())
                            .eq("to_id", ArticleId)
                            .eq("operation", "favorite")
                            .eq("category", "article")
            );

            map.put("isLiked", likeTriclick != null ? "true" : "false");
            map.put("isFavorited", favoriteTriclick != null ? "true" : "false");
        }
        System.out.println("resp返回：" + map);
        return map;
    }
}
