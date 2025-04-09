package com.mcm.backend.controller.exhibit;

import com.mcm.backend.pojo.Exhibit;
import com.mcm.backend.service.exhibit.GetlistExhibitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetlistExhibitController {
    @Autowired
    private GetlistExhibitService getlistExhibitService;

    @GetMapping("/exhibit/getlist/")
    public List<Exhibit> getlistExhibit(){
        return getlistExhibitService.getlistExhibit();
    }
}
