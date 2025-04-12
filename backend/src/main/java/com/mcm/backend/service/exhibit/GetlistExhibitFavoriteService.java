package com.mcm.backend.service.exhibit;

import com.mcm.backend.pojo.Exhibit;

import java.util.List;

public interface GetlistExhibitFavoriteService {
    List<Exhibit> getlistExhibitFavorite(Integer userId);
}
