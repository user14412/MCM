package com.mcm.backend.controller.exhibit;

import com.mcm.backend.pojo.Exhibit;
import com.mcm.backend.service.exhibit.GetlistExhibitLikeService;
import com.mcm.backend.service.exhibit.GetlistExhibitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetlistExhibitLikeController {
    @Autowired
    private GetlistExhibitLikeService getlistExhibitLikeService;

    @GetMapping("/exhibit/getlist/like/")
    public List<Exhibit> getlistExhibitLike(@RequestParam Integer userId){
        return getlistExhibitLikeService.getlistExhibitLike(userId);
    }
}
