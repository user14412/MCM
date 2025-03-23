package com.mcm.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mcm.backend.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
