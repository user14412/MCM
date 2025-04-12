package com.mcm.backend.controller.exhibit;

import com.mcm.backend.pojo.Exhibit;
import com.mcm.backend.service.exhibit.GetlistExhibitFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetlistExhibitFavoriteController {
    @Autowired
    GetlistExhibitFavoriteService getlistExhibitFavoriteService;
    @GetMapping("/exhibit/getlist/favorite/")
    public List<Exhibit> getlistExhibitLike(@RequestParam Integer userId){
        return getlistExhibitFavoriteService.getlistExhibitFavorite(userId);
    }
}
