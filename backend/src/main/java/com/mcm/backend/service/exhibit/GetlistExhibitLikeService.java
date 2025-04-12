package com.mcm.backend.service.exhibit;

import com.mcm.backend.pojo.Exhibit;

import java.util.List;

public interface GetlistExhibitLikeService {
    List<Exhibit> getlistExhibitLike(Integer userId);
}
