package com.mcm.backend.controller.stat;

import com.mcm.backend.service.stat.CountPanoramaService;
import com.mcm.backend.service.stat.VisitPanoramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CountPanoramaController {
    @Autowired
    CountPanoramaService countPanoramaService;
    @PostMapping("/stat/count/panorama/")
    Integer visitPanorama(){
        return countPanoramaService.countPanorama();
    }
}
