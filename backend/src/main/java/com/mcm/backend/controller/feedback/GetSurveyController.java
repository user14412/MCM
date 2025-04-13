package com.mcm.backend.controller.feedback;

import com.mcm.backend.pojo.Survey;
import com.mcm.backend.service.feedback.GetSurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetSurveyController {
    @Autowired
    GetSurveyService getSurveyService;
    @GetMapping("/survey/get/")
    Survey getSurvey(@RequestParam Integer surveyId){
        return getSurveyService.getSurvey(surveyId);
    }
}
