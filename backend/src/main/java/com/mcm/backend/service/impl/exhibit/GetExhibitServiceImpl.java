package com.mcm.backend.service.impl.exhibit;

import com.alibaba.fastjson2.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.ExhibitMapper;
import com.mcm.backend.mapper.ImageMapper;
import com.mcm.backend.mapper.TriclickMapper;
import com.mcm.backend.pojo.Exhibit;
import com.mcm.backend.pojo.Image;
import com.mcm.backend.pojo.Triclick;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.exhibit.GetExhibitService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetExhibitServiceImpl implements GetExhibitService {
    @Autowired
    ExhibitMapper exhibitMapper;
    @Autowired
    ImageMapper imageMapper;
    @Autowired
    TriclickMapper triclickMapper;
    @Override
    public Map<String, String> getexhibit(Integer ExhibitId) {
        // 根据藏品id获取藏品详情
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Exhibit exhibit = exhibitMapper.selectById(ExhibitId); // 藏品

        QueryWrapper<Image> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("exhibit_id", exhibit.getId());
        List<Image> images = imageMapper.selectList(queryWrapper); // 藏品的image列表
        JSONArray images_jsonarray = new JSONArray(images);
        String images_string = images_jsonarray.toString();

        Map<String, String> map = new HashMap<>();
        if(exhibit != null){
            map.put("name", exhibit.getName());
            map.put("comment", exhibit.getComment());
            map.put("images_string", images_string);
            // 查询是否点赞
            Triclick likeTriclick = triclickMapper.selectOne(
                    new QueryWrapper<Triclick>()
                            .eq("user_id", user.getId())
                            .eq("to_id", ExhibitId)
                            .eq("operation", "like")
                            .eq("category", "exhibit")
            );
            map.put("isLiked", likeTriclick != null ? "true" : "false");
            // 查询是否收藏
            Triclick favoriteTriclick = triclickMapper.selectOne(
                    new QueryWrapper<Triclick>()
                            .eq("user_id", user.getId())
                            .eq("to_id", ExhibitId)
                            .eq("operation", "favorite")
                            .eq("category", "exhibit")
            );
            map.put("isFavorited", favoriteTriclick != null ? "true" : "false");
        }
        return map;
    }
}
