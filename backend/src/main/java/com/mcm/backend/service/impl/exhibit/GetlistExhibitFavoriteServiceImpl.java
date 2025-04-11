package com.mcm.backend.service.impl.exhibit;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.ExhibitMapper;
import com.mcm.backend.mapper.TriclickMapper;
import com.mcm.backend.pojo.Exhibit;
import com.mcm.backend.pojo.Triclick;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.exhibit.GetlistExhibitFavoriteService;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GetlistExhibitFavoriteServiceImpl implements GetlistExhibitFavoriteService {
    @Autowired
    ExhibitMapper exhibitMapper;
    @Autowired
    TriclickMapper triclickMapper;

    @Override
    public List<Exhibit> getlistExhibitFavorite() {
        // 取出当前用户收藏的所有藏品信息作为一个list返回给前端
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Integer userId = user.getId();
        /* TODO: 先查询triclick表中所有user_id==userId && operation=="like" && category=="exhibit"按 createtime从新到旧的顺序返回记录的to_id
                再查询exhibit表中所有id==to_id的记录，返回为List，并使得List中的数据按triclick的createtime从新到旧排序。
        * */
        // 查询triclick表
        QueryWrapper<Triclick> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("operation", "favorite")
                .eq("category", "exhibit")
                .orderByAsc("createtime");
        List<Triclick> triclickList = triclickMapper.selectList(queryWrapper);

        // 创建exhibitId到triclick记录的映射关系
        Map<Integer, Triclick> triclickMap = new HashMap<>();
        for(Triclick triclick : triclickList){
            triclickMap.put(triclick.getToId(), triclick);
        }

        // 查询exhibit表
        List<Integer> exhibitIds = triclickList.stream()
                .map(Triclick::getToId)
                .collect(Collectors.toList());
        List<Exhibit> exhibitsList = exhibitMapper.selectBatchIds(exhibitIds);
        // 根据triclick中的createtime对exhibit列表进行排序
        exhibitsList.sort(Comparator.comparing(exhibit -> triclickMap.get(exhibit.getId()).getCreatetime()));
        Collections.reverse(exhibitsList);
        return exhibitsList;
    }
}
