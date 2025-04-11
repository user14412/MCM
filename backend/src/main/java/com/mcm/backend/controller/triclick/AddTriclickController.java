package com.mcm.backend.controller.triclick;

import com.mcm.backend.service.triclick.AddTriclickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddTriclickController {
    @Autowired
    AddTriclickService addTriclickService;

    @PostMapping("/triclick/add/")
    public String addTriclick(@RequestParam Map<String, String> data){
        return addTriclickService.addTriclick(data);
    }
}
