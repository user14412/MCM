package com.mcm.backend.controller.feedback;

import com.mcm.backend.mapper.AddSurveyAnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
public class AddSurveyAnsController {
    @Autowired
    AddSurveyAnsService addSurveyAnsService;
    @PostMapping("/surveyans/add/")
    String addSurveyAns(@RequestParam Map<String, String> data) throws UnsupportedEncodingException {
        String encodedContent = data.get("content");

        String decodedContent = URLDecoder.decode(encodedContent, StandardCharsets.UTF_8.toString());
        data.put("decodedContent", decodedContent);
        return addSurveyAnsService.addSurveyAns(data);
    }
}
