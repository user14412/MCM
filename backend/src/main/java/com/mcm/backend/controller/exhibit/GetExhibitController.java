package com.mcm.backend.controller.exhibit;

import com.mcm.backend.service.exhibit.GetExhibitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetExhibitController {
    @Autowired
    GetExhibitService getExhibitService;

    @GetMapping("/exhibit/get/")
    public Map<String, String> getExhibit(@RequestParam Map<String, String> data){
        Integer ExhibitId = Integer.parseInt(data.get("ExhibitId"));
        return getExhibitService.getexhibit(ExhibitId);
    }
}
