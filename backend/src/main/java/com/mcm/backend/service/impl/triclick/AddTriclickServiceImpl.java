package com.mcm.backend.service.impl.triclick;

import com.mcm.backend.mapper.TriclickMapper;
import com.mcm.backend.pojo.Triclick;
import com.mcm.backend.pojo.User;
import com.mcm.backend.service.impl.utils.UserDetailsImpl;
import com.mcm.backend.service.triclick.AddTriclickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AddTriclickServiceImpl implements AddTriclickService {
    @Autowired
    TriclickMapper triclickMapper;
    @Override
    public String addTriclick(Map<String, String> data) {
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

        Triclick triclick = new Triclick(null, userId, toId, operation, category, null);
        triclickMapper.insert(triclick);

        return "add triclick success";
    }
}
