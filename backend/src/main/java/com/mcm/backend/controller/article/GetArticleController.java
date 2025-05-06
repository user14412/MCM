package com.mcm.backend.controller.article;

import com.mcm.backend.service.article.GetArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GetArticleController {
    @Autowired
    private GetArticleService getArticleService;

    @GetMapping("/article/get/")
    public Map<String, String> getArticle(@RequestParam Map<String, String> data) {
        Map<String, String> response = new HashMap<>();
        try {
            String articleIdStr = data.get("ArticleId");
            if (articleIdStr == null || articleIdStr.trim().isEmpty()) {
                response.put("error_message", "文章ID不能为空");
                return response;
            }
            
            // 检查是否为有效数字
            if (!articleIdStr.matches("\\d+")) {
                response.put("error_message", "无效的文章ID");
                return response;
            }
            
            Integer articleId = Integer.parseInt(articleIdStr);
            return getArticleService.getarticle(articleId);
        } catch (NumberFormatException e) {
            response.put("error_message", "文章ID格式错误");
            return response;
        } catch (Exception e) {
            response.put("error_message", "服务器内部错误");
            return response;
        }
    }
}
