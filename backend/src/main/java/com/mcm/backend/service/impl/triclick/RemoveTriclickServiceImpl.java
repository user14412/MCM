package com.mcm.backend.service.impl.triclick;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mcm.backend.mapper.TriclickMapper;
import com.mcm.backend.pojo.Triclick;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import com.mcm.backend.service.triclick.RemoveTriclickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RemoveTriclickServiceImpl implements RemoveTriclickService {
    @Autowired
    TriclickMapper triclickMapper;
    @Override
    public String removeTriclick(Map<String, String> data) {
        /* example:
            to_id: this.articleId.toString(),
            operation: "like",
            category: "article"
         */
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Integer userId = user.getId();
        Integer toId = Integer.parseInt(data.get("to_id"));
        String operation = data.get("operation");
        String category = data.get("category");

        //
        Integer rowsRemoved = triclickMapper.delete(
                new QueryWrapper<Triclick>()
                        .eq("user_id", userId)
                        .eq("to_id", toId)
                        .eq("operation", operation)
                        .eq("category", category)
        );
        if(rowsRemoved == 0){
            return "不存在要删除的triclick记录";
        }else if(rowsRemoved > 1){
            return "要删除的triclick记录数大于1";
        }
        return "remove triclick success";
    }
}
