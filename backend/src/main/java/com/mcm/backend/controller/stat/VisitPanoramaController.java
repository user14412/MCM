package com.mcm.backend.controller.stat;

import com.mcm.backend.service.stat.VisitPanoramaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VisitPanoramaController {
    @Autowired
    VisitPanoramaService visitPanoramaService;
    @PostMapping("/stat/visit/panorama/")
    String visitPanorama(@RequestParam String page){
        return visitPanoramaService.visitPanorama(page);
    }
}
