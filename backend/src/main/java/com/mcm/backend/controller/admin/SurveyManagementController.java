package com.mcm.backend.controller.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mcm.backend.service.admin.SurveyManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/surveys/")
public class SurveyManagementController {
    @Autowired
    private SurveyManagementService surveyManagementService;

    @GetMapping("list/")
    public Map<String, Object> getSurveyList(@RequestParam(defaultValue = "1") Integer page,
                                           @RequestParam(defaultValue = "10") Integer size) {
        return surveyManagementService.getSurveyList(page, size);
    }

    @GetMapping("{id}/")
    public Map<String, Object> getSurvey(@PathVariable Integer id) {
        return surveyManagementService.getSurvey(id);
    }

    @PostMapping("create/")
    public Map<String, String> createSurvey(@RequestBody Map<String, Object> request) throws JsonProcessingException {
        String title = (String) request.get("title");
        Object content = request.get("content");
        String contentJson = content instanceof String ? (String) content : new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(content);
        return surveyManagementService.createSurvey(title, contentJson);
    }

    @PutMapping("{id}/")
    public Map<String, String> updateSurvey(@PathVariable Integer id,
                                          @RequestBody Map<String, Object> request) throws JsonProcessingException {
        String title = (String) request.get("title");
        Object content = request.get("content");
        String contentJson = content instanceof String ? (String) content : new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(content);
        return surveyManagementService.updateSurvey(id, title, contentJson);
    }

    @DeleteMapping("{id}/")
    public Map<String, String> deleteSurvey(@PathVariable Integer id) {
        return surveyManagementService.deleteSurvey(id);
    }

    @GetMapping("{id}/answers/")
    public Map<String, Object> getSurveyAnswers(@PathVariable Integer id,
                                              @RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer size) {
        return surveyManagementService.getSurveyAnswers(id, page, size);
    }

    @GetMapping("answers/{answerId}/")
    public Map<String, Object> getSurveyAnswer(@PathVariable Integer answerId) {
        return surveyManagementService.getSurveyAnswer(answerId);
    }
} 