package com.mcm.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class UploadImageController {
    @PostMapping("/upload/image/")
    String test(@RequestParam("file") MultipartFile file) throws IOException{
        if(file.isEmpty()){
            return "文件为空";
        }
        String staticDir = "src/main/resources/static/";
        String uploadDir = "images/";
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(staticDir + uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        System.out.println("收到文件: " + fileName);

        String fileUrl = "http://localhost:3000/" + uploadDir + fileName;
        return fileUrl;
    }
}
